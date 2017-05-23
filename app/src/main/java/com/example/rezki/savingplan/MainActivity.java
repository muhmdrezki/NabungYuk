
package com.example.rezki.savingplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText jmlpengeluaran, jmlpemasukan, targetnabung;
    private TextView pengeluaranTV, pemasukanTV, targetnabungTV, sisauangTV, nabungandaTV;
    private Button kalkulasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = new Bundle();

        jmlpengeluaran = (EditText) findViewById(R.id.jmlpengeluaran);
        jmlpemasukan = (EditText) findViewById(R.id.jmlpemasukkan);
        targetnabung = (EditText) findViewById(R.id.targetnabung);


        pengeluaranTV = (TextView) findViewById(R.id.ViewPengeluaran);
        pemasukanTV = (TextView) findViewById(R.id.ViewPemasukan);
        targetnabungTV = (TextView) findViewById(R.id.targetTV);
        sisauangTV = (TextView) findViewById(R.id.sisauangTV);
        nabungandaTV = (TextView) findViewById(R.id.nabungandaTV);


        kalkulasi = (Button) findViewById(R.id.kalkulasi);
        kalkulasi.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==kalkulasi) {
            //Hitung();
            String pengeluaran = jmlpengeluaran.getText().toString();
            Intent pindah = null;
            pindah = new Intent(MainActivity.this, tampildata.class);
            Bundle b = new Bundle();
            b.putString("pengeluaran", pengeluaran);
            //startActivity(new Intent (MainActivity.this, tampildata.class).putExtras(b));
            pindah.putExtras(b);
            startActivity(pindah);
        }
    }

    private void Hitung(){

        String pengeluaran = jmlpengeluaran.getText().toString().trim();
        String pemasukkan = jmlpemasukan.getText().toString().trim();
        String target = targetnabung.getText().toString().trim();

        if(!TextUtils.isEmpty(pengeluaran) && (!TextUtils.isEmpty(pemasukkan)) && (!TextUtils.isEmpty(target))){

                pemasukanTV.setText("Rp "+pemasukkan);
                pengeluaranTV.setText("Rp "+pengeluaran);
                targetnabungTV.setText("Rp "+target);

            //Konversi Ke Integer
                Integer intpengeluaran = Integer.parseInt(pengeluaran);
                Integer intpemasukan = Integer.parseInt(pemasukkan);
            //Proses Perhitungan
                Integer intsisa = intpemasukan - intpengeluaran;
                Integer intnabung = intsisa * 20/100;

        String nabung = intnabung.toString().trim();
        String sisa = intsisa.toString().trim();

        sisauangTV.setText("Rp "+sisa);
        nabungandaTV.setText("Rp "+nabung);
        }
    }
}
