package com.example.rezki.savingplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SavingPlanAct extends AppCompatActivity implements View.OnClickListener{


    private TextView pengeluaranTV, pemasukanTV, targetnabungTV, sisauangTV, nabungandaTV;
    private TextView lamanabungTV;
    private String pengeluaran1, pemasukkan1, target1;
    private Spinner lamaspin;
    private Button Apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savingplan);

        pengeluaranTV = (TextView) findViewById(R.id.pengeluaranTV);
        pemasukanTV = (TextView) findViewById(R.id.pemasukanTV);
        targetnabungTV = (TextView) findViewById(R.id.targetTV);
        sisauangTV = (TextView) findViewById(R.id.sisauangTV);
        nabungandaTV = (TextView) findViewById(R.id.nabungandaTV);
        lamanabungTV = (TextView) findViewById(R.id.lamanabungTV);

        lamaspin = (Spinner) findViewById(R.id.lamaspin);

        Apply = (Button) findViewById(R.id.apply);
        Apply.setOnClickListener(this);

        //pemanggilan dari mainact
        Bundle b = getIntent().getExtras();
        String pengeluaran = b.getString("pengeluaran");
        String pemasukkan = b.getString("pemasukkan");
        String target = b.getString("target");

        pengeluaranTV.setText("Rp."+pengeluaran);
        pemasukanTV.setText("Rp."+pemasukkan);
        targetnabungTV.setText("Rp."+target);

        pengeluaran1 = pengeluaran;
        pemasukkan1 = pemasukkan;
        target1 = target;
        hitung();
    }

    public void hitung() {

        final String pengeluaran = pengeluaran1.toString().trim();
        final String pemasukkan = pemasukkan1.toString().trim();
        final String target = target1.toString().trim();
        final String lama = lamaspin.getSelectedItem().toString().trim();

        //Konversi Ke Integer
        final Integer intpengeluaran = Integer.parseInt(pengeluaran);
        final Integer intpemasukan = Integer.parseInt(pemasukkan);
        final Integer inttarget = Integer.parseInt(target);


        if(lama.equals("Bulan")){
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
        }
    }

    @Override
    public void onClick(View view) {
        if(view==Apply){
            hitung();
        }
    }
}
