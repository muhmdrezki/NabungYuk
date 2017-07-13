package com.example.rezki.savingplan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rezki on 6/7/2017.
 */

public class CatatPengeluaranAct extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_date_now2, tv_date2;
    private EditText et_nominal_pengeluaran, et_detail_pengeluaran;
    private RadioGroup radiogroup_kategori;
    private RadioButton radioButton;
    private Button btn_simpan_pengeluaran, btn_pilih_tanggal;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db_Ref, db_RefNew;

    private ProgressDialog progressDialog;

    private String UserID, tanggal, uang_user;

    private String Tanggal_HariIni, duit;

    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catat_pengeluaran);

        //deklarasi Progress Dialog
        progressDialog = new ProgressDialog(this);
        //deklarasi calendar
        calendar = Calendar.getInstance();
        //deklarasi Firebase Auth
        auth = FirebaseAuth.getInstance();
        //mengambil user yang sedang login
        user = auth.getCurrentUser();
        String userid = user.getUid();

        //menentukan path untuk menyimpan data pengeluaran
        db_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Pengeluaran");
        db_Ref.keepSynced(true);
        //menentukan path child user
        db_RefNew = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        db_RefNew.keepSynced(true);

        //menampung id xml ke variable
        et_nominal_pengeluaran = (EditText) findViewById(R.id.et_nominal_pengeluaran);
        et_nominal_pengeluaran.addTextChangedListener(new CurrencyTextWatcher());
        et_detail_pengeluaran = (EditText) findViewById(R.id.et_detail_pengeluaran);
        tv_date_now2 = (TextView) findViewById(R.id.tv_date_now2);
        tv_date2 = (TextView) findViewById(R.id.tv_date2);
        radiogroup_kategori = (RadioGroup) findViewById(R.id.radiogroup_kategori);
        btn_pilih_tanggal = (Button) findViewById(R.id.btn_pilihtanggal);
        btn_simpan_pengeluaran = (Button) findViewById(R.id.btn_simpan_pengeluaran);

        //mengaktifkan button
        btn_simpan_pengeluaran.setOnClickListener(this);
        btn_pilih_tanggal.setOnClickListener(this);

        //memanggil fungsi untuk menampilkan tanggal hari ini
        ambilTanggalHariIni();

        //mengambil value dari child uang
        db_RefNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uang = (String) dataSnapshot.child("uang").getValue();
                uang_user = uang;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //fungsi penyimpanan data
    public void catatpengeluaran() {

        //menampung nilai dari text field ke variable sementara
        int SelectedId = radiogroup_kategori.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(SelectedId);
        if (radiogroup_kategori.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Pilih kategori pengeluaran", Toast.LENGTH_SHORT).show();
        } else {
            final String kategori = radioButton.getText().toString().trim();
            final String nominal = duit;
            final String detail = et_detail_pengeluaran.getText().toString().trim();

            //cek apakah form sudah terisi atau belum
            if (TextUtils.isEmpty(nominal)) {
                Toast.makeText(this, "Harap isi nominal pengeluaran", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(detail)) {
                Toast.makeText(this, "Harap isi detail pengeluaran", Toast.LENGTH_SHORT).show();
            } else {
                //menentukan mana tanggal yang diambil
                //jika tanggal tidak dipilih maka otomatis tanggal hari ini yang akan diambil
                if (tv_date2.getText().equals("Date")) {
                    final String date = tv_date_now2.getText().toString().trim();
                    tanggal = date;
                } else {
                    final String date = tv_date2.getText().toString().trim();
                    tanggal = date;
                }
                progressDialog.setMessage("Menyimpan Data..");
                progressDialog.show();
                //Proses Upload
                final DatabaseReference newPost = db_Ref.push();
                newPost.child("jumlah").setValue(nominal.toString().trim().replace("Rp","").replace(".",""));
                newPost.child("kategori").setValue(kategori);
                newPost.child("detail").setValue(detail);
                newPost.child("tgl_pengeluaran").setValue(tanggal);

                //proses perhitungan uang user
                Integer int_nominal = Integer.parseInt(nominal.toString().trim().replace("Rp","").replace(".",""));
                Integer int_uang_user = Integer.parseInt(uang_user);
                Integer isi_dompet = int_uang_user - int_nominal;
                final String dompet = isi_dompet.toString().trim();
                db_RefNew.child("uang").setValue(dompet);
                db_RefNew.child("tgl_pengeluaran_terakhir").setValue(tanggal);
                db_RefNew.child("kategori_pengeluaran_terakhir").setValue(kategori);
                progressDialog.dismiss();
                Toast.makeText(CatatPengeluaranAct.this, " Data Berhasil Disimpan ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CatatPengeluaranAct.this, Pengeluaran_List.class));
            }
        }
    }
        //fungsi ambil tanggal hari ini
    public void ambilTanggalHariIni(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Tanggal_HariIni = dateFormat.format(date);
        tv_date_now2.setText(Tanggal_HariIni);
    }

    //fungsi ambil tanggal dari tombol pilih tanggal
    public void ambilTanggal(){
        new DatePickerDialog(CatatPengeluaranAct.this, listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener listener  = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month_of_year, int day_of_month){
            tv_date2.setText(day_of_month+"/"+(month_of_year+ 1 )+ "/"+year);
        }
    };

    //fungsi klik back dua kali untuk keluar
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
        //jika btn pilih tanggal di tekan
        if(view==btn_pilih_tanggal){
            ambilTanggal();
        } else if(view==btn_simpan_pengeluaran){
            catatpengeluaran();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_catatpengeluaran, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.logout){

            logout();

        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(CatatPengeluaranAct.this, MainMenu.class));
        } else if ( item.getItemId() == R.id.pengeluaran_list){
            startActivity(new Intent(CatatPengeluaranAct.this, Pengeluaran_List.class));
        } else if ( item.getItemId() == R.id.My_Dompet){
            startActivity(new Intent(CatatPengeluaranAct.this, DompetActivity.class));
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
        public void afterTextChanged(Editable s) {
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
