package com.example.rezki.savingplan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CatatPemasukanAct extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_date_now, tv_date;
    private EditText et_nominal, et_detail_pemasukan;
    private RadioGroup radiogroup_kategori;
    private RadioButton radioButton;
    private Button btn_simpan, btn_pilih_tanggal;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db_Ref, uangkuRef, db_RefNew;

    private ProgressDialog progressDialog;

    private String UserID, tanggal, uang_user, nama, duit;

    private String Tanggal_HariIni;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catat_pemasukan);

        //Pendeklarasian Variable / Objek
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        calendar = Calendar.getInstance();

        UserID = user.getUid().toString().trim();

        db_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID).child("Pemasukan");
        uangkuRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID);

        db_RefNew = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID);
        db_RefNew.keepSynced(true);

        et_nominal = (EditText) findViewById(R.id.et_nominal_pemasukan);
        et_detail_pemasukan = (EditText) findViewById(R.id.et_detail_pemasukan);

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date_now = (TextView) findViewById(R.id.tv_date_now);

        radiogroup_kategori = (RadioGroup) findViewById(R.id.radiogroup_kategori);

        btn_pilih_tanggal = (Button) findViewById(R.id.btn_pilihtanggal);
        btn_pilih_tanggal.setOnClickListener(this);

        btn_simpan = (Button) findViewById(R.id.btn_simpan_pemasukan);
        btn_simpan.setOnClickListener(this);

        ambilTanggalHariIni();

        et_nominal.addTextChangedListener(new CurrencyTextWatcher());

        //Mengambil data dari FIREBASE
        db_RefNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uang = (String) dataSnapshot.child("uang").getValue();
                uang_user = uang;
                String nama_user = (String) dataSnapshot.child("nama").getValue();
                nama = nama_user;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Code untuk mengambil tanggal hari ini
    public void ambilTanggalHariIni(){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            Tanggal_HariIni = dateFormat.format(date);
            tv_date_now.setText(Tanggal_HariIni);
    }

    //code untuk pilih tanggal
    public void ambilTanggal(){
        new DatePickerDialog(CatatPemasukanAct.this, listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener listener  = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month_of_year, int day_of_month){
            tv_date.setText(day_of_month+"/"+(month_of_year+ 1 )+ "/"+year);
        }
    };

    //code untuk double back klik exit
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

    //code untuk mencatat pemasukkan // menyimpan pemasukan ke FIREBASE
    public void catatpemasukan() {
        //Menampung data dari TextField ke variable tampung
        final String nominal = duit;
        final String detail = et_detail_pemasukan.getText().toString().trim();
        int SelectedId = radiogroup_kategori.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(SelectedId);
        if (radiogroup_kategori.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Pilih kategori pengeluaran", Toast.LENGTH_SHORT).show();
        } else {
            final String kategori = radioButton.getText().toString().trim();
            if (TextUtils.isEmpty(nominal)) {
                Toast.makeText(this, "Harap isi nominal pemasukan", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(detail)) {
                Toast.makeText(this, "Harap isi detail pemasukan", Toast.LENGTH_SHORT).show();
            } else {

                if (tv_date.getText().equals("Date")) {
                    final String date = tv_date_now.getText().toString().trim();
                    tanggal = date;
                } else {
                    final String date = tv_date.getText().toString().trim();
                    tanggal = date;
                }

                //Menampilkan Progress Dialog
                progressDialog.setMessage("Menyimpan Data..");
                progressDialog.show();
                //Proses Upload
                //Database Push
                final DatabaseReference newPost = db_Ref.push();
                newPost.child("jumlah").setValue(nominal.toString().trim().replace("Rp","").replace(".",""));
                newPost.child("kategori").setValue(kategori);
                newPost.child("detail").setValue(detail);
                newPost.child("tgl_pemasukan").setValue(tanggal);

                //menghitung berapa uang yang ada di dompet
                Integer int_nominal = Integer.parseInt(nominal.toString().trim().replace("Rp","").replace(".",""));
                Integer int_uang_user = Integer.parseInt(uang_user);
                Integer isi_dompet = int_nominal + int_uang_user;
                final String dompet = isi_dompet.toString().trim();

                //menyimpan berapa jumlah uang setelah pemasukan // dan menyimpan tanggal dan kategori pemasukan terakhir
                uangkuRef.child("uang").setValue(dompet);
                db_RefNew.child("tgl_pemasukan_terakhir").setValue(tanggal);
                db_RefNew.child("kategori_pemasukan_terakhir").setValue(kategori);
                progressDialog.dismiss();
                Toast.makeText(CatatPemasukanAct.this, " Data Berhasil Disimpan ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CatatPemasukanAct.this, Pemasukan_List.class));
            }
        }
    }

    //mengaktifkan link dan tombol supaya bisa di klik
    @Override
    public void onClick(View view) {
        if(btn_simpan==view){
            catatpemasukan();
        } else if(btn_pilih_tanggal==view){
            ambilTanggal();
        }
    }

    //menampilkan menu.xmol // menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_catatpemasukan, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.logout) {

            logout();
        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(CatatPemasukanAct.this, MainMenu.class));
        } else if ( item.getItemId() == R.id.pemasukan_list){
            startActivity(new Intent(CatatPemasukanAct.this, Pemasukan_List.class));
        } else if ( item.getItemId() == R.id.My_Dompet){
            startActivity(new Intent(CatatPemasukanAct.this, DompetActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        auth.signOut();

    }

    private class CurrencyTextWatcher implements TextWatcher {

        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public synchronized void afterTextChanged(Editable s) {
            String formatted = null;
            if (!mEditing) {
                mEditing = true;

                Locale local = new Locale("in", "id");
                String digits = s.toString().replaceAll("\\D", "");
                NumberFormat nf = NumberFormat.getCurrencyInstance(local);
                try {
                    formatted = nf.format(Double.parseDouble(digits));
                    s.replace(0, s.length(), formatted);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }
                mEditing = false;
            }
            duit = formatted;
        }

    }
}
