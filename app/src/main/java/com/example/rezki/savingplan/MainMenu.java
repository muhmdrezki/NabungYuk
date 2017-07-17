package com.example.rezki.savingplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;

    private TextView tv_savingplan;
    private TextView tv_dompet;
    private TextView tv_logout;
    private TextView tv_settings;

    private ImageView iv_savingplan;
    private ImageView iv_dompet;
    private ImageView iv_logout;
    private ImageView iv_settings;

    private String nama_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Cek apakah user sudah login atau belum
        auth = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainMenu.this, FirstPage.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        tv_dompet = (TextView) findViewById(R.id.tv_dompet);
        tv_dompet.setOnClickListener(this);

        tv_savingplan = (TextView) findViewById(R.id.tv_savingplan);
        tv_savingplan.setOnClickListener(this);

        tv_logout = (TextView) findViewById(R.id.tv_Logout);
        tv_logout.setOnClickListener(this);

        iv_dompet = (ImageView) findViewById(R.id.iv_dompet);
        iv_dompet.setOnClickListener(this);

        iv_savingplan = (ImageView) findViewById(R.id.iv_savingplan);
        iv_savingplan.setOnClickListener(this);

        iv_logout = (ImageView) findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(this);

        tv_settings = (TextView) findViewById(R.id.tv_Settings);
        tv_settings.setOnClickListener(this);

        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        iv_settings.setOnClickListener(this);
    }



    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        final String TAG = this.getClass().getName();
        Log.d(TAG, "click");

        if (doubleBackToExitPressedOnce==true) {
            //super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        doubleBackToExitPressedOnce=true;
        Log.d(TAG, "twice "+ doubleBackToExitPressedOnce);
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                Log.d(TAG, "twice "+ doubleBackToExitPressedOnce);
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authlistener);
    }

    @Override
    public void onClick(View view) {
        if (view == tv_savingplan) {
            Intent loginIntent = new Intent(MainMenu.this, SavingPlan_List.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        if (view == tv_dompet) {
            Intent loginIntent = new Intent(MainMenu.this, DompetActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        if (view == tv_logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            logout();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.dismiss();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda yakin akan Logout?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        if (view == iv_logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            logout();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.dismiss();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda yakin akan Logout?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }
        if (view == iv_dompet) {
            Intent loginIntent = new Intent(MainMenu.this, DompetActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        if (view == iv_savingplan) {
            Intent loginIntent = new Intent(MainMenu.this, SavingPlan_List.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        if(view == iv_settings){
            Intent loginIntent = new Intent(MainMenu.this, Config_Activity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        if(view == tv_settings){
            Intent loginIntent = new Intent(MainMenu.this, Config_Activity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

}
