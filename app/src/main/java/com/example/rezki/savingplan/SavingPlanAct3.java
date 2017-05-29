package com.example.rezki.savingplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
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

import java.text.ParseException;
import java.util.Date;

public class SavingPlanAct3 extends AppCompatActivity implements View.OnClickListener{


    private TextView pengeluaranTV, pemasukanTV, targetnabungTV, sisauangTV, nabungandaTV;
    private TextView lamanabungTV, coba;
    private String pengeluaran1, pemasukkan1, target1, targetwkt1, spn_targetwkt1, tujuan_nabung1, nama_plan1 ;
    private String pengeluaransave, pemasukkansave, targetsave, nabungbulansave, nabungmggsave, sisasave,  tgl_sekarang;
    private Spinner lamaspin;
    private Button Apply, btn_next_act3;
    private DatabaseReference databaseReference, user_db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressdialog;

    private String target_tanggal;
    private String tanggal_skrng;

    private Integer bulan, hari, tahun;

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
        coba = (TextView) findViewById(R.id.coba);

        btn_next_act3 = (Button) findViewById(R.id.btn_next_act3);
        btn_next_act3.setOnClickListener(this);

        //pemanggilan dari mainact
        Bundle b = getIntent().getExtras();
        String pengeluaran = b.getString("pengeluaran");
        String pemasukkan = b.getString("pemasukkan");
        String target = b.getString("target");
        String targetwkt = b.getString("target_tanggal");
        String spn_targetwkt = b.getString("spn_targetwaktu");
        String tujuan_nabung = b.getString("tujuan_nabung");
        String nama_plan = b.getString("nama_plan");

        pengeluaranTV.setText("Rp."+pengeluaran);
        pemasukanTV.setText("Rp."+pemasukkan);
        targetnabungTV.setText("Rp."+target);

        pengeluaran1 = pengeluaran;
        pemasukkan1 = pemasukkan;
        target1 = target;
        spn_targetwkt1 = spn_targetwkt;
        tujuan_nabung1 = tujuan_nabung;
        nama_plan1 = nama_plan;



        ambilTanggalSkrng();
        tanggal_skrng = tgl_sekarang;
        target_tanggal = targetwkt;
        hitungHari();
        hitung();
    }


    public void ambilTanggalSkrng(){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            tgl_sekarang = dateFormat.format(date);

    }

    public void hitungHari() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar tanggaltarget = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        Date date2= null;
        try {
            date2 = format.parse(target_tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tanggaltarget.setTime(date2);


        Date date1= null;
        try {
            date1 = format.parse(tanggal_skrng);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now.setTime(date1);

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        now.set(mYear, mMonth, mDay);

        int years = tanggaltarget.get(Calendar.YEAR) - now.get(Calendar.YEAR);
        int months = tanggaltarget.get(Calendar.MONTH) - now.get(Calendar.MONTH);
        int days = tanggaltarget.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH);
        if (days < 0) {
            months--;
            days += now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (months < 0) {
            years--;
            months += 12;
        }
        String coba1 = years + " tahun " + months + " bulan " + days + " hari";
        bulan = months;
        hari = days;
        tahun = years;
    }

    public void hitung() {

        final String pengeluaran = pengeluaran1.toString().trim();
        final String pemasukkan  = pemasukkan1.toString().trim();
        final String target      = target1.toString().trim();
        final String satuan_bulan   = bulan.toString().trim();
        final String satuan_hari = hari.toString().trim();
        final String satuan_tahun = tahun.toString().trim();

        Integer minggu=0;

            final Integer inttarget = Integer.parseInt(target);
            final Integer intbulan = Integer.parseInt(satuan_bulan);
            final Integer intpengeluaran = Integer.parseInt(pengeluaran);
            final Integer intpemasukan = Integer.parseInt(pemasukkan);
            final Integer inthari = Integer.parseInt(satuan_hari);
            final Integer inttahun = Integer.parseInt(satuan_tahun);

                final Integer intsisa = intpemasukan - intpengeluaran;

                        if ((intbulan==0) && (inthari>=7) && (inttahun==0)){
                            final Integer intminggu = inthari / 7;
                            minggu = intminggu;
                        } else if((intbulan>0) && (inttahun==0) && (inthari>=0)){
                            final Integer intminggu = intbulan * 4;
                            minggu = intminggu;
                        } else if ((inttahun>0) && (intbulan>=0) && (inthari>=0)){
                            final Integer intminggu = inttahun * 52;
                            minggu = intminggu;
                        } else if ((intbulan<=0) && (inthari<7) && (inttahun<=0)){
                            final Integer intminggu=0;
                            minggu = intminggu;
                        }
                    if(minggu != 0) {
                        final Integer divnabung = inttarget / minggu;
                        final Integer modnabung = inttarget % minggu;
                        final Integer intnabung = divnabung + modnabung;
                        final Integer intlama = minggu;
                        final Integer uangjaga = intsisa - (intnabung * 4);

                        //Konversi Integer - String
                        final String nabung = intnabung.toString().trim();
                        final String pegangan = uangjaga.toString().trim();
                        final String lama = intlama.toString().trim();

                        sisauangTV.setText("Rp " + pegangan+" /bulan");
                        nabungandaTV.setText("Rp " + nabung + " /minggu");
                        lamanabungTV.setText("Selama " + lama + " minggu");

                        nabungbulansave = nabung;
                        sisasave = pegangan;
                        targetsave = target;
                        pengeluaransave = pengeluaran;
                        pemasukkansave = pemasukkan;
                    } else {
                        Toast.makeText(SavingPlanAct3.this, "Target Minimal 1 Minggu dari Hari ini", Toast.LENGTH_LONG).show();
                        Toast.makeText(SavingPlanAct3.this, "Silahkan Klik ULANGI DARI AWAL", Toast.LENGTH_LONG).show();
                    }
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
            progressdialog.setMessage("Menyimpan Data..");
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


