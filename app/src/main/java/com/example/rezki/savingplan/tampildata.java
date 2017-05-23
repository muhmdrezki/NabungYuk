package com.example.rezki.savingplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class tampildata extends AppCompatActivity {


    private TextView pengeluaranTV, pemasukanTV, targetnabungTV, sisauangTV, nabungandaTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampildata);

        pengeluaranTV = (TextView) findViewById(R.id.ViewPengeluaran);
        pemasukanTV = (TextView) findViewById(R.id.ViewPemasukan);
        targetnabungTV = (TextView) findViewById(R.id.targetTV);
        sisauangTV = (TextView) findViewById(R.id.sisauangTV);
        nabungandaTV = (TextView) findViewById(R.id.nabungandaTV);

        Bundle b = getIntent().getExtras();
        pengeluaranTV.setText(b.getCharSequence("pengeluaran"));

    }

}
