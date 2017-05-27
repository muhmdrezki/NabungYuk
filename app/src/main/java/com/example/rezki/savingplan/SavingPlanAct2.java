
package com.example.rezki.savingplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SavingPlanAct2 extends AppCompatActivity implements View.OnClickListener{

    private EditText jmlpengeluaran, jmlpemasukan, targetnabung, targetwaktu;
    private Spinner spn_targetwaktu;
    private Button kalkulasi;

    private String nama_plan1, tujuan_nabung1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savingplanact2);

        jmlpengeluaran = (EditText) findViewById(R.id.jmlpengeluaran);
        jmlpemasukan = (EditText) findViewById(R.id.jmlpemasukkan);
        targetnabung = (EditText) findViewById(R.id.targetnabung);
        targetwaktu = (EditText) findViewById(R.id.targetwaktu);
        spn_targetwaktu = (Spinner) findViewById(R.id.spn_pilihtarget);

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
        }
    }

    public void proses(){
        String pengeluaran = jmlpengeluaran.getText().toString().trim();
        String pemasukkan = jmlpemasukan.getText().toString().trim();
        String target = targetnabung.getText().toString().trim();
        String targetwkt = targetwaktu.getText().toString().trim();
        String spn_targetwkt = spn_targetwaktu.getSelectedItem().toString().trim();

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
                b.putString("target_waktu",targetwkt);
                b.putString("spn_targetwaktu", spn_targetwkt);
                b.putString("nama_plan", nama_plan1);
                b.putString("tujuan_nabung", tujuan_nabung1);

                pindah.putExtras(b);
                startActivity(pindah);
            }
        }
    }



