package com.example.rezki.savingplan;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavingPlanFinalAct extends AppCompatActivity implements View.OnClickListener{

    private String mPostkey;
    private DatabaseReference databaseReference, user_db;
    private EditText etnabung;
    private Button btn_tabung;
    private TextView tv_namaplanfinal, tv_namatujuanfinal, tv_targetnabung, tv_tabungan;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String tabunganbaru1;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_plan_final);

        progressdialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plans");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        mPostkey = getIntent().getExtras().getString("post_id");

        tv_namaplanfinal = (TextView) findViewById(R.id.tv_namaplanfinal);
        tv_namatujuanfinal = (TextView) findViewById(R.id.tv_namatujuanfinal);
        tv_targetnabung = (TextView) findViewById(R.id.tv_targetnabung);
        tv_tabungan = (TextView) findViewById(R.id.tv_tabungan);

        etnabung = (EditText) findViewById(R.id.etnabung);
        btn_tabung = (Button) findViewById(R.id.btn_tabung);
        btn_tabung.setOnClickListener(this);

        databaseReference.child(mPostkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String namaplan = (String) dataSnapshot.child("nama_plan").getValue();
                String namatujuan = (String) dataSnapshot.child("tujuan_nabung").getValue();
                String targetnabung = (String) dataSnapshot.child("target").getValue();
                String tabungan = (String) dataSnapshot.child("tabungan").getValue();


                tv_namaplanfinal.setText(namaplan);
                tv_namatujuanfinal.setText(namatujuan);
                tv_targetnabung.setText(targetnabung);
                tv_tabungan.setText("Rp. "+tabungan);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void tabung(){
        String tabungan = tv_tabungan.getText().toString().trim();
        String nabung = etnabung.getText().toString().trim();

        if(!TextUtils.isEmpty(tabungan)){
            Integer inttabungan = Integer.parseInt(tabungan);
            Integer intnabung = Integer.parseInt(nabung);

            Integer tabunganbaru = intnabung+inttabungan;

            final String tabungan_baru = tabunganbaru.toString().trim();
            tabunganbaru1 = tabungan_baru;
        }
        //Proses Upload
        //Menampilkan Progress Bar
        progressdialog.show();
        //Database Push
        final DatabaseReference newPost = databaseReference.push();
        user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("tabungan").setValue(tabunganbaru1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressdialog.dismiss();
                            Toast.makeText(SavingPlanFinalAct.this, " Tabungan Berhasil Ditambahkan ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==btn_tabung){
            //tabung();
        }
    }
}

