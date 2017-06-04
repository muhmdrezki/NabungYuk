package com.example.rezki.savingplan;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button btn_logout;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;

    private TextView tv_savingplan;
    private TextView tv_dompet;

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

                    Intent loginIntent = new Intent(MainMenu.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        tv_dompet = (TextView) findViewById(R.id.tv_PEMASUKAN);
        tv_dompet.setOnClickListener(this);

        tv_savingplan = (TextView) findViewById(R.id.tv_savingplan);
        tv_savingplan.setOnClickListener(this);
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
       if(view==tv_savingplan){
            Intent loginIntent = new Intent(MainMenu.this, SavingPlan_List.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        } else if (view==tv_dompet){
           Intent loginIntent = new Intent(MainMenu.this, DompetActivity.class);
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
