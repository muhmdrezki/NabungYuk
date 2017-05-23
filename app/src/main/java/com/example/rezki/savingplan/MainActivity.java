
package com.example.rezki.savingplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText jmlpengeluaran, jmlpemasukan, targetnabung;
    private Button kalkulasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jmlpengeluaran = (EditText) findViewById(R.id.jmlpengeluaran);
        jmlpemasukan = (EditText) findViewById(R.id.jmlpemasukkan);
        targetnabung = (EditText) findViewById(R.id.targetnabung);

        kalkulasi = (Button) findViewById(R.id.kalkulasi);
        kalkulasi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==kalkulasi) {
            proses();
        }
    }

    public void proses(){
        String pengeluaran = jmlpengeluaran.getText().toString().trim();
        String pemasukkan = jmlpemasukan.getText().toString().trim();
        String target = targetnabung.getText().toString().trim();

        Intent pindah = new Intent(MainActivity.this, SavingPlanAct.class);
        Bundle b = new Bundle();
        b.putString("pengeluaran", pengeluaran);
        b.putString("pemasukkan", pemasukkan);
        b.putString("target", target);

        pindah.putExtras(b);
        startActivity(pindah);
    }
}


