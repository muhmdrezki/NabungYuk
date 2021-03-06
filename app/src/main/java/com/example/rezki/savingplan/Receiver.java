package com.example.rezki.savingplan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Receiver extends BroadcastReceiver {

    private DatabaseReference nameRef;
    private FirebaseUser user;
    private FirebaseAuth auth;


    @Override
    public void onReceive(final Context context, Intent intent) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String userid = user.getUid().toString().trim();

        nameRef = FirebaseDatabase.getInstance().getReference().child("Users");

        final NotificationManager notifMAnager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent rep_intent = new Intent(context, MainMenu.class);
        rep_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context,100,rep_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        nameRef.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama_user = (String) dataSnapshot.child("nama").getValue();
                long v[] = {500,1000};
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.nb_launcher)
                        .setContentTitle("Halo, "+nama_user)
                        .setContentText("Sudahkah anda menabung hari ini?")
                        .setVibrate(v)
                        .setSound(uri)
                        .setAutoCancel(true);
                notifMAnager.notify(100,builder.build());

                Notification notif = builder.build();
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notif.defaults |=Notification.DEFAULT_LIGHTS;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }
}
