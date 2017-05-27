package com.example.rezki.savingplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText etemail, etnama, etpassword;
    private Button btn_daftar;
    private FirebaseAuth auth;
    private DatabaseReference databaseref;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressdialog = new ProgressDialog(this);

        //Firebase Database Variable
        auth = FirebaseAuth.getInstance();
        databaseref = FirebaseDatabase.getInstance().getReference().child("Users");

        //Tampung Variable
        etemail = (EditText) findViewById(R.id.etemail);
        etnama = (EditText) findViewById(R.id.etnama);
        etpassword = (EditText) findViewById(R.id.etpassword);

        btn_daftar = (Button) findViewById(R.id.btn_daftar);
        btn_daftar.setOnClickListener(this);

    }

    private void Register() {
        final String nama = etnama.getText().toString().trim();
        final String email = etemail.getText().toString().trim();
        final String password = etpassword.getText().toString().trim();

        if(TextUtils.isEmpty(nama)) {
            Toast.makeText(Register.this, "Nama tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(email)) {
            Toast.makeText(Register.this, "Email tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(Register.this, "Password tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if ((TextUtils.isEmpty(nama)) && (TextUtils.isEmpty(email)) && (TextUtils.isEmpty(password))) {
            Toast.makeText(Register.this, "Data diri tidak boleh kosong", Toast.LENGTH_LONG).show();
        } else {
            progressdialog.show();
            progressdialog.setMessage("Pendaftaran sedang di proses..");

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String userid = auth.getCurrentUser().getUid();
                        DatabaseReference current_user  = databaseref.child(userid);
                        current_user.child("nama").setValue(nama);
                        progressdialog.dismiss();
                        Intent mainIntent = new Intent(Register.this, MainMenu.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }

                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        if(view==btn_daftar){
            Register();
        }
    }
}
