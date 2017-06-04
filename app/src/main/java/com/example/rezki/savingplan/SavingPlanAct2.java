
package com.example.rezki.savingplan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SavingPlanAct2 extends AppCompatActivity implements View.OnClickListener{

    private EditText jmlpengeluaran, jmlpemasukan, targetnabung;
    private TextView targetwaktu;
    private Button kalkulasi, btn_date;

    private String nama_plan1, tujuan_nabung1;
    private Calendar calendar;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savingplanact2);

        calendar = Calendar.getInstance();

        jmlpengeluaran = (EditText) findViewById(R.id.jmlpengeluaran);
        jmlpemasukan = (EditText) findViewById(R.id.jmlpemasukkan);
        targetnabung = (EditText) findViewById(R.id.targetnabung);
        targetwaktu = (TextView) findViewById(R.id.targetwaktu);

        btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(this);
        kalkulasi = (Button) findViewById(R.id.kalkulasi);
        kalkulasi.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        String nama_plan = b.getString("nama_plan");
        String tujuan_nabung = b.getString("tujuan_nabung");

        nama_plan1 = nama_plan;
        tujuan_nabung1 = tujuan_nabung;
    }

    @Override
    public void onClick(View view) {
        if(view==kalkulasi) {
            proses();
        } else if(view==btn_date){
            ambilTanggal();
        }
    }

    public void ambilTanggal(){
        new DatePickerDialog(SavingPlanAct2.this, listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener listener  = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month_of_year, int day_of_month){
                targetwaktu.setText(year+"-"+(month_of_year+ 1 )+ "-"+day_of_month);
        }
    };

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_dompet, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.main_menu) {

            startActivity(new Intent(SavingPlanAct2.this,MainMenu.class));

        } else if ( item.getItemId() == R.id.logout){

            logout();

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        auth.signOut();

    }

    public void proses(){
        String pengeluaran = jmlpengeluaran.getText().toString().trim();
        String pemasukkan = jmlpemasukan.getText().toString().trim();
        String target = targetnabung.getText().toString().trim();
        String targetwkt = targetwaktu.getText().toString().trim();

        if(TextUtils.isEmpty(pengeluaran)) {
            Toast.makeText(this, "Jumlah Pengeluaran Harus Diisi",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(pemasukkan)){
            Toast.makeText(this, "Jumlah Pemasukkan Harus Diisi",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(target)) {
            Toast.makeText(this, "Target Menabung Harus Diisi",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(targetwkt)) {
                Intent pindah = new Intent(SavingPlanAct2.this, SavingPlanAct3.class);
                Bundle b = new Bundle();
                b.putString("pengeluaran", pengeluaran);
                b.putString("pemasukkan", pemasukkan);
                b.putString("target", target);
                b.putString("nama_plan", nama_plan1);
                b.putString("tujuan_nabung", tujuan_nabung1);

                pindah.putExtras(b);
                startActivity(pindah);
        } else {
                Intent pindah = new Intent(SavingPlanAct2.this, SavingPlanAct3.class);
                Bundle b = new Bundle();
                b.putString("pengeluaran", pengeluaran);
                b.putString("pemasukkan", pemasukkan);
                b.putString("target", target);
                b.putString("target_tanggal",targetwkt);
                b.putString("nama_plan", nama_plan1);
                b.putString("tujuan_nabung", tujuan_nabung1);

                pindah.putExtras(b);
                startActivity(pindah);
            }
        }
    }



