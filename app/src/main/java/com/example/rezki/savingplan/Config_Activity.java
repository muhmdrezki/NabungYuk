package com.example.rezki.savingplan;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Config_Activity extends AppCompatActivity {

    private TextView tv_account;
    private Switch notif_switch;

    private DatabaseReference nameRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private Calendar calendar;

    private String nama_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        tv_account = (TextView) findViewById(R.id.tv_account);
        notif_switch = (Switch) findViewById(R.id.notif_switch);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String UserID = user.getUid().toString().trim();

        nameRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID);
        nameRef.keepSynced(true);

        //Mengambil data uang user
        //Mengambil Waktu Pengeluaran dan Pemasukan Terakhir User
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String namauser = (String) dataSnapshot.child("nama").getValue();
                tv_account.setText("Logged In As : "+namauser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();

        //Default Switch
        SharedPreferences sharedPrefs = getSharedPreferences("com.example.rezki.savingplan", MODE_PRIVATE);
        //notif_switch.setChecked(sharedPrefs.getBoolean("stat", false));
        notif_switch.setChecked(sharedPrefs.getBoolean(user.getUid(),false));

        //Listener Supaya Bisa di Switch ON/OFF
        notif_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(Config_Activity.this, "Notification Turned ON", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.rezki.savingplan", MODE_PRIVATE).edit();
                    editor.putBoolean(user.getUid(),true);
                    editor.commit();

                    calendar = Calendar.getInstance();
                    Intent intent = new Intent(getApplicationContext(),Receiver.class);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    //calendar.set(Calendar.DAY_OF_WEEK,1);
                    calendar.set(Calendar.HOUR_OF_DAY,11);
                    calendar.set(Calendar.MINUTE,58);
                    calendar.set(Calendar.SECOND,45);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

                } else if(!isChecked) {
                    Toast.makeText(Config_Activity.this, "Notification Turned OFF", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.rezki.savingplan", MODE_PRIVATE).edit();
                    editor.putBoolean(user.getUid(),false);
                    editor.commit();
                }
            }
        });

        //Cek Default Switch
        if(notif_switch.isChecked()){
            Toast.makeText(Config_Activity.this, "Notification is currently ON", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Config_Activity.this, "Notification is currently OFF", Toast.LENGTH_LONG).show();
        }
    }
}
