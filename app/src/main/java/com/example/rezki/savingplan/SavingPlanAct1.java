package com.example.rezki.savingplan;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spn_tujuan, R.layout.spinner_item);
        spn_tujuan_nabung.setAdapter(adapter);
        btn_next = (Button) findViewById(R.id.btn_next_act1);
        btn_next.setOnClickListener(this);
    }

    public void bawadata() {
        String nama_plan = etnamaplan.getText().toString().trim();
        String tujuan_nabung = spn_tujuan_nabung.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(nama_plan)) {
            Toast.makeText(this, "Harap isi nama plan anda", Toast.LENGTH_SHORT).show();
        } else if (tujuan_nabung.equals("Pilih")) {
            Toast.makeText(this, "Harap pilih tujuan menabung anda", Toast.LENGTH_SHORT).show();
        } else {
            Intent pindah = new Intent(SavingPlanAct1.this, SavingPlanAct2.class);
            Bundle b = new Bundle();

            b.putString("nama_plan", nama_plan);
            b.putString("tujuan_nabung", tujuan_nabung);

            pindah.putExtras(b);
            startActivity(pindah);
        }
    }

    //code untuk double back langsung exit
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

    //menampilkan menu.xml / menu layout di toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_dompet, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.main_menu) {

            startActivity(new Intent(SavingPlanAct1.this,MainMenu.class));

        } else if ( item.getItemId() == R.id.logout){

            logout();

        }
        return super.onOptionsItemSelected(item);
    }

    //fungsi logout

    private void logout() {

        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        auth.signOut();

    }

    @Override
    public void onClick(View view) {
        if(view==btn_next){
            bawadata();
        }
    }
}
