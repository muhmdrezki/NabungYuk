
package com.example.rezki.savingplan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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



