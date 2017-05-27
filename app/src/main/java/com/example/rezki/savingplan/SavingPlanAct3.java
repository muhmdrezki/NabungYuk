package com.example.rezki.savingplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavingPlanAct3 extends AppCompatActivity implements View.OnClickListener{


    private TextView pengeluaranTV, pemasukanTV, targetnabungTV, sisauangTV, nabungandaTV;
    private TextView lamanabungTV;
    private String pengeluaran1, pemasukkan1, target1, targetwkt1, spn_targetwkt1, tujuan_nabung1, nama_plan1 ;
    private String pengeluaransave, pemasukkansave, targetsave, nabungbulansave, nabungmggsave, sisasave;
    private Spinner lamaspin;
    private Button Apply, btn_next_act3;
    private DatabaseReference databaseReference, user_db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savingplanact3);

        progressdialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plans");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        pengeluaranTV = (TextView) findViewById(R.id.pengeluaranTV);
        pemasukanTV = (TextView) findViewById(R.id.pemasukanTV);
        targetnabungTV = (TextView) findViewById(R.id.targetTV);
        sisauangTV = (TextView) findViewById(R.id.sisauangTV);
        nabungandaTV = (TextView) findViewById(R.id.nabungandaTV);
        lamanabungTV = (TextView) findViewById(R.id.lamanabungTV);

        lamaspin = (Spinner) findViewById(R.id.lamaspin);

        btn_next_act3 = (Button) findViewById(R.id.btn_next_act3);
        btn_next_act3.setOnClickListener(this);

        Apply = (Button) findViewById(R.id.apply);
        Apply.setOnClickListener(this);

        //pemanggilan dari mainact
        Bundle b = getIntent().getExtras();
        String pengeluaran = b.getString("pengeluaran");
        String pemasukkan = b.getString("pemasukkan");
        String target = b.getString("target");
        String targetwkt = b.getString("target_waktu");
        String spn_targetwkt = b.getString("spn_targetwaktu");
        String tujuan_nabung = b.getString("tujuan_nabung");
        String nama_plan = b.getString("nama_plan");

        pengeluaranTV.setText("Rp."+pengeluaran);
        pemasukanTV.setText("Rp."+pemasukkan);
        targetnabungTV.setText("Rp."+target);

        pengeluaran1 = pengeluaran;
        pemasukkan1 = pemasukkan;
        target1 = target;
        targetwkt1 = targetwkt;
        spn_targetwkt1 = spn_targetwkt;
        tujuan_nabung1 = tujuan_nabung;
        nama_plan1 = nama_plan;


        hitung();
    }

    public void hitung() {

        final String pengeluaran = pengeluaran1.toString().trim();
        final String pemasukkan  = pemasukkan1.toString().trim();
        final String target      = target1.toString().trim();
        final String lama        = lamaspin.getSelectedItem().toString().trim();
        if(targetwkt1!=null) {
            final String targetwkt = targetwkt1.toString().trim();

            //Jika Target Diisi
            final Integer inttarget = Integer.parseInt(target);
            final Integer intwaktu = Integer.parseInt(targetwkt);
            final Integer intpengeluaran = Integer.parseInt(pengeluaran);
            final Integer intpemasukan = Integer.parseInt(pemasukkan);

            if(lama.equals("Bulan")) {
                final Integer intsisa = intpemasukan - intpengeluaran;
                final Integer intnabungbln = inttarget / intwaktu;
                final Integer intlamabln = intwaktu;

                //Konversi Integer - String
                final String nabungbln = intnabungbln.toString().trim();
                final String sisa = intsisa.toString().trim();
                final String lamabln = intlamabln.toString().trim();

                sisauangTV.setText("Rp " + sisa);
                nabungandaTV.setText("Rp " + nabungbln + " /bulan");
                lamanabungTV.setText("Selama " + lamabln + " bulan");
                nabungbulansave = nabungbln;
                sisasave = sisa;

            } else if (lama.equals("Minggu")){
                final Integer intsisa = intpemasukan - intpengeluaran;
                final Integer intnabungbln = inttarget / intwaktu;
                final Integer intlamabln = intwaktu;
                final Integer intnabungmgg = intnabungbln/4;
                final Integer intlamamgg = inttarget*4/intnabungbln;

                //Konversi Integer - String
                final String nabungmgg = intnabungmgg.toString().trim();
                final String sisa = intsisa.toString().trim();
                final String lamamgg = intlamamgg.toString().trim();

                sisauangTV.setText("Rp " + sisa);
                nabungandaTV.setText("Rp " + nabungmgg + " /minggu");
                lamanabungTV.setText("Selama " + lamamgg + " minggu");
                sisasave = sisa;
            }

        } else if (targetwkt1==null){
            //Konversi Ke Integer
            final Integer intpengeluaran = Integer.parseInt(pengeluaran);
            final Integer intpemasukan = Integer.parseInt(pemasukkan);
            final Integer inttarget = Integer.parseInt(target);

            //Lama menabung dalam bulan

            if(lama.equals("Bulan")) {
                //Proses Perhitungan
                final Integer intsisa = intpemasukan - intpengeluaran;
                final Integer intnabungbln = intsisa * 20 / 100;
                final Integer intlamabln = inttarget / intnabungbln;
                //Konversi Integer - String
                final String nabungbln = intnabungbln.toString().trim();
                final String sisa = intsisa.toString().trim();
                final String lamabln = intlamabln.toString().trim();

                sisauangTV.setText("Rp " + sisa);
                nabungandaTV.setText("Rp " + nabungbln + " /bulan");
                lamanabungTV.setText("Selama " + lamabln + " bulan");
                nabungbulansave = nabungbln;
                sisasave = sisa;

                //Lama menabung dalam minggu
                    } else if (lama.equals("Minggu")){
                    //Proses Perhitungan
                    final Integer intsisa = intpemasukan - intpengeluaran;
                    final Integer intnabungbln = intsisa * 20 / 100;
                    final Integer intnabungmgg = intnabungbln / 4;
                    final Integer intlamamgg = inttarget * 4/intnabungbln;

                    //Konversi Integer - String
                    final String nabungmgg = intnabungmgg.toString().trim();
                    final String sisa = intsisa.toString().trim();
                    final String lamamgg = intlamamgg.toString().trim();

                    sisauangTV.setText("Rp " + sisa);
                    nabungandaTV.setText("Rp " + nabungmgg + " /minggu");
                    lamanabungTV.setText("Selama " + lamamgg + " minggu");
                    sisasave = sisa;
        }
      }
        targetsave = target;
        pengeluaransave = pengeluaran;
        pemasukkansave = pemasukkan;
   }

    //Fungsi Posting
    private void SavePlan() {
        //Deklarasi Variable
        final String target = targetsave.toString().trim();
        final String pengeluaran = pengeluaransave.toString().trim();
        final String pemasukkan = pemasukkansave.toString().trim();
        final String nabungbln = nabungbulansave.toString().trim();
        final String namaplan = nama_plan1.toString().trim();
        final String tujuannabung = tujuan_nabung1.toString().trim();
        final String sisa = sisasave.toString().trim();

        //Proses Upload
            //Menampilkan Progress Bar
            progressdialog.setMessage("Posting, Please Wait");
            progressdialog.show();
                    //Database Push
                    final DatabaseReference newPost = databaseReference.push();
                    user_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("uid").setValue(user.getUid());
                            newPost.child("nama_plan").setValue(namaplan);
                            newPost.child("tujuan_nabung").setValue(tujuannabung);
                            newPost.child("target").setValue(target);
                            newPost.child("tabungan").setValue("0");
                            newPost.child("pengeluaran").setValue(pengeluaran);
                            newPost.child("pemasukkan").setValue(pemasukkan);
                            newPost.child("nabungperbulan").setValue(nabungbln);
                            newPost.child("sisa").setValue(sisa);
                            newPost.child("nama").setValue(dataSnapshot.child("nama").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressdialog.dismiss();
                                        Toast.makeText(SavingPlanAct3.this, " Data Berhasil Disimpan ", Toast.LENGTH_LONG).show();
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
        if(view==Apply){
            hitung();
        } else if(view==btn_next_act3){
            SavePlan();
        }
    }
}
