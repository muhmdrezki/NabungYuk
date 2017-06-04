package com.example.rezki.savingplan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SavingPlanFinalAct extends AppCompatActivity implements View.OnClickListener{

    final String TAG = this.getClass().getName();
    private String mPostkey, key;
    private DatabaseReference databaseReference, tabunganRef, user_db;
    private EditText etnabung;
    private Button btn_tabung, btn_apply;
    private TextView tv_namaplanfinal, tv_namatujuanfinal, tv_targetnabung, tv_tabungan, tv_dateAkhir;
    private TextView tv_nabungmgg;
    private Spinner spn_convert;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String tabunganbaru1, tbg, tampung;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_plan_final);

        progressdialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plans");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        mPostkey = getIntent().getExtras().getString("post_id");

        tv_namaplanfinal = (TextView) findViewById(R.id.tv_namaplanfinal);
        tv_namatujuanfinal = (TextView) findViewById(R.id.tv_namatujuanfinal);
        tv_targetnabung = (TextView) findViewById(R.id.tv_targetnabung);
        tv_tabungan = (TextView) findViewById(R.id.tv_tabungan);
        tv_dateAkhir = (TextView) findViewById(R.id.tv_dateAkhir);
        tv_nabungmgg = (TextView) findViewById(R.id.tv_nabungmgg);

        etnabung = (EditText) findViewById(R.id.etnabung);
        spn_convert = (Spinner) findViewById(R.id.spn_convertwaktu);

        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);

        btn_tabung = (Button) findViewById(R.id.btn_tabung);
        btn_tabung.setOnClickListener(this);

        databaseReference.child(mPostkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String namaplan = (String) dataSnapshot.child("nama_plan").getValue();
                String namatujuan = (String) dataSnapshot.child("tujuan_nabung").getValue();
                String targetnabung = (String) dataSnapshot.child("target").getValue();
                String tabungan = (String) dataSnapshot.child("tabungan").getValue();
                String tgl_target = (String) dataSnapshot.child("tgltarget").getValue();
                String nabungmgg = (String) dataSnapshot.child("nabungperbulan").getValue();

                tv_namaplanfinal.setText(namaplan);
                tv_namatujuanfinal.setText(namatujuan);
                tv_targetnabung.setText("Rp. "+targetnabung);
                tv_tabungan.setText("Rp. "+tabungan);
                tv_nabungmgg.setText("Rp. " + nabungmgg+"/ Minggu");
                tbg = tabungan;
                tv_dateAkhir.setText(tgl_target);
                tampung = nabungmgg;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }




        });
    }

    public void convertwaktu(){
        final String waktu = spn_convert.getSelectedItem().toString().trim();
        final String nabungmgg = tampung.toString().trim();

                if(waktu.equals("Minggu")){
                    tv_nabungmgg.setText("Rp. "+tampung+" / Minggu");
                } else if (waktu.equals("Bulan")){
                    Integer intnabungmgg = Integer.parseInt(nabungmgg);
                    Integer intnabungbln = intnabungmgg * 4;

                    String nabungbln = intnabungbln.toString().trim();
                    tv_nabungmgg.setText("Rp. "+nabungbln+ " / Bulan");
                } else if(waktu.equals("Hari")){
                    Integer intnabungmgg = Integer.parseInt(nabungmgg);
                    Integer divnabunghari = intnabungmgg / 7;
                    Integer modnabunghari = intnabungmgg % 7;
                    Integer hslnabunghari = divnabunghari + modnabunghari;

                    String nabunghari = hslnabunghari.toString().trim();
                    tv_nabungmgg.setText("Rp. "+ nabunghari+" / Hari");
                }
    }

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
    public void tabung() {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            String tabungan = tbg.toString().trim();
                            String nabung = etnabung.getText().toString().trim();
                            if (!TextUtils.isEmpty(tabungan)) {
                                Integer inttabungan = Integer.parseInt(tabungan);
                                Integer intnabung = Integer.parseInt(nabung);
                                Integer tabunganbaru = intnabung + inttabungan;
                                final String tabungan_baru = tabunganbaru.toString().trim();
                                tabunganbaru1 = tabungan_baru;
                                databaseReference.child(mPostkey).child("tabungan").setValue(tabunganbaru1);
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.dismiss();
                            break;

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Uang Sejumlah Rp. "+etnabung.getText().toString().trim()+" akan ditambahkan ke tabungan anda").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_savingplanfinal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.PlanList){

            startActivity(new Intent(SavingPlanFinalAct.this,SavingPlan_List.class));

        } else if ( item.getItemId() == R.id.logout){

            logout();

        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(SavingPlanFinalAct.this, MainMenu.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        firebaseAuth.signOut();

    }



    @Override
    public void onClick(View view) {
        if(view==btn_tabung){
            tabung();
        } else if(view==btn_apply){
            convertwaktu();
        }
    }
}

