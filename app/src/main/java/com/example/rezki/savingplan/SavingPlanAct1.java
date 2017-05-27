package com.example.rezki.savingplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SavingPlanAct1 extends AppCompatActivity implements View.OnClickListener{

    private EditText etnamaplan;
    private Spinner spn_tujuan_nabung;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savingplanact1);

        etnamaplan = (EditText) findViewById(R.id.etnamaplan);
        spn_tujuan_nabung = (Spinner) findViewById(R.id.spn_tujuan_nabung);
        btn_next = (Button) findViewById(R.id.btn_next_act1);
        btn_next.setOnClickListener(this);
    }

    public void bawadata(){
        String nama_plan = etnamaplan.getText().toString().trim();
        String tujuan_nabung = spn_tujuan_nabung.getSelectedItem().toString().trim();

        Intent pindah = new Intent(SavingPlanAct1.this, SavingPlanAct2.class);
        Bundle b = new Bundle();

        b.putString("nama_plan", nama_plan);
        b.putString("tujuan_nabung", tujuan_nabung);

        pindah.putExtras(b);
        startActivity(pindah);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_next){
            bawadata();
        }
    }
}
