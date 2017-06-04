package com.example.rezki.savingplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText etemail, etpassword;
    private TextView tv_registerlink;
    private Button btn_login;
    private FirebaseAuth auth;
    private DatabaseReference databaseref;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressdialog = new ProgressDialog(this);

        //Firebase Database Variable
        auth = FirebaseAuth.getInstance();
        databaseref = FirebaseDatabase.getInstance().getReference().child("Users");

        //Tampung Variable
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_registerlink = (TextView) findViewById(R.id.tv_registerlink);
        tv_registerlink.setOnClickListener(this);
    }

    private void checklogin() {

        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "Email tidak boleh kosong", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Password tidak boleh kosong", Toast.LENGTH_LONG).show();
        } else {
            progressdialog.show();
            progressdialog.setMessage("Harap Tunggu..");

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressdialog.dismiss();
                        Intent mainIntent = new Intent(Login.this, MainMenu.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }else {
                        progressdialog.dismiss();
                        Toast.makeText(Login.this, "Login Gagal", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
    public void onClick(View view) {
        if(view==btn_login){
            checklogin();
        } else if (view==tv_registerlink){
            Intent mainIntent = new Intent(Login.this, Register.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
    }
}
