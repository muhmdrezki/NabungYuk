package com.example.rezki.savingplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button btn_logout;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;

    private TextView tv_savingplan;

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

        tv_savingplan = (TextView) findViewById(R.id.tv_savingplan);
        tv_savingplan.setOnClickListener(this);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authlistener);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_logout){
            auth.signOut();
        } else if(view==tv_savingplan){
            Intent loginIntent = new Intent(MainMenu.this, SavingPlanAct1.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }

    }
}
