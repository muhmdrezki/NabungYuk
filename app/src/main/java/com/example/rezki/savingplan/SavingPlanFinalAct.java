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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class SavingPlanFinalAct extends AppCompatActivity implements View.OnClickListener{

    final String TAG = this.getClass().getName();
    private String mPostkey, key;
    private DatabaseReference databaseReference, tabunganRef, user_db;
    private EditText etnabung;
    private Button btn_tabung, btn_apply;
    private TextView tv_namaplanfinal, tv_namatujuanfinal, tv_targetnabung, tv_tabungan, tv_dateAkhir, tv_status, tv_failed;
    private TextView tv_nabungmgg;
    private Spinner spn_convert;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String tabunganbaru1, tbg, tampung, target, status_plan, Tanggal_HariIni, tgl, duit;
    private ProgressDialog progressdialog;

    private NumberFormat nf;

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
        tv_status = (TextView) findViewById(R.id.TVstatus_plan);
        tv_failed = (TextView) findViewById(R.id.TVstatus_failed);


        etnabung = (EditText) findViewById(R.id.etnabung);
        etnabung.addTextChangedListener(new CurrencyTextWatcher());

        spn_convert = (Spinner) findViewById(R.id.spn_convertwaktu);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spn_lamanabung, R.layout.spinner_item);
        spn_convert.setAdapter(adapter);

        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);

        btn_tabung = (Button) findViewById(R.id.btn_tabung);
        btn_tabung.setOnClickListener(this);

        ambilTanggalHariIni();

        Locale local = new Locale("id", "ID");
        nf = NumberFormat.getCurrencyInstance(local);

        databaseReference.child(mPostkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String namaplan = (String) dataSnapshot.child("nama_plan").getValue();
                String namatujuan = (String) dataSnapshot.child("tujuan_nabung").getValue();

                String targetnabung = (String) dataSnapshot.child("target").getValue();
                String rp_targetnabung = nf.format(Double.parseDouble(targetnabung));

                String tabungan = (String) dataSnapshot.child("tabungan").getValue();
                String rp_tabugan = nf.format(Double.parseDouble(tabungan));

                String tgl_target = (String) dataSnapshot.child("tgltarget").getValue();

                String nabungmgg = (String) dataSnapshot.child("nabungperbulan").getValue();
                String rp_nabungmgg = nf.format(Double.parseDouble(nabungmgg));

                status_plan = (String) dataSnapshot.child("status").getValue();

                tv_namaplanfinal.setText(namaplan);
                tv_namatujuanfinal.setText(namatujuan);
                tv_targetnabung.setText(rp_targetnabung);
                tv_tabungan.setText(rp_tabugan);
                tv_nabungmgg.setText(rp_nabungmgg + "/ Minggu");
                tv_dateAkhir.setText(tgl_target);

                tbg = tabungan;
                tampung = nabungmgg;
                target = targetnabung;
                tgl = tgl_target;

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date deadline = null;
                Date nowdate = null;
                try {
                    deadline = df.parse(tgl);
                    nowdate = df.parse(Tanggal_HariIni);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Integer target_uang = Integer.parseInt(target);
                Integer uang_tabungan = Integer.parseInt(tbg);

                if (uang_tabungan >= target_uang) {
                    btn_tabung.setVisibility(View.INVISIBLE);
                    etnabung.setVisibility(View.INVISIBLE);
                    tv_status.setVisibility(View.VISIBLE);
                } else if ((uang_tabungan < target_uang) && (nowdate.compareTo(deadline))>=0) {
                    btn_tabung.setVisibility(View.INVISIBLE);
                    etnabung.setVisibility(View.INVISIBLE);
                    tv_failed.setVisibility(View.VISIBLE);
                    AlertDialog.Builder alert = new AlertDialog.Builder(SavingPlanFinalAct.this);
                    alert.setTitle("Sorry !");
                    alert.setMessage("Target Anda Tidak Tercapai !");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    databaseReference.child(mPostkey).child("status").setValue("Failed");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }




        });
    }

    public void convertwaktu(){
        final String waktu = spn_convert.getSelectedItem().toString().trim();
        final String nabungmgg = tampung.toString().trim();
        String rp_nabungmgg = nf.format(Double.parseDouble(nabungmgg));

                if(waktu.equals("Minggu")){
                    tv_nabungmgg.setText(rp_nabungmgg+" / Minggu");
                } else if (waktu.equals("Bulan")){
                    Integer intnabungmgg = Integer.parseInt(nabungmgg);
                    Integer intnabungbln = intnabungmgg * 4;
                    String nabungbln = intnabungbln.toString().trim();

                    String rp_nabungbln = nf.format(Double.parseDouble(nabungbln));
                    tv_nabungmgg.setText(rp_nabungbln+ " / Bulan");

                } else if(waktu.equals("Hari")){
                    Integer intnabungmgg = Integer.parseInt(nabungmgg);
                    Integer divnabunghari = intnabungmgg / 7;
                    Integer modnabunghari = intnabungmgg % 7;
                    Integer hslnabunghari = divnabunghari + modnabunghari;
                    String nabunghari = hslnabunghari.toString().trim();

                    String rp_nabunghari = nf.format(Double.parseDouble(nabunghari));
                    tv_nabungmgg.setText(rp_nabunghari+" / Hari");
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

    //fungsi ambil tanggal hari ini
    public void ambilTanggalHariIni(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Tanggal_HariIni = dateFormat.format(date);
        Toast.makeText(this, Tanggal_HariIni, Toast.LENGTH_SHORT).show();
    }

    public void tabung() {

        String nabung = duit;
        if (TextUtils.isEmpty(nabung)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SavingPlanFinalAct.this);
            alert.setMessage("Jumlah Tabungan Belum Diisi");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            String rp_tabungan = nf.format(Double.parseDouble(tbg));
                            String tabungan = rp_tabungan.toString().trim().replace("Rp", "").replace(".", "");
                            String nabung = duit;
                            Integer inttabungan = Integer.parseInt(tabungan);
                            Integer intnabung = Integer.parseInt(nabung.toString().trim().replace("Rp", "").replace(".", ""));
                            Integer tabunganbaru = intnabung + inttabungan;
                            final String tabungan_baru = tabunganbaru.toString().trim();
                            tabunganbaru1 = tabungan_baru;
                            databaseReference.child(mPostkey).child("tabungan").setValue(tabunganbaru1);
                            etnabung.setText("");

                            Integer target_uang = Integer.parseInt(target);
                            Integer uang_tabungan = Integer.parseInt(tabunganbaru1);

                            if (uang_tabungan >= target_uang) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(SavingPlanFinalAct.this);
                                alert.setTitle("Congratulations ! ");
                                alert.setMessage("Anda telah mencapai target anda !");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                                databaseReference.child(mPostkey).child("status").setValue("Completed");
                                btn_tabung.setVisibility(View.INVISIBLE);
                                etnabung.setVisibility(View.INVISIBLE);
                            }
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(SavingPlanFinalAct.this);
                            alert1.setMessage("Tabungan Berhasil Ditambahkan");
                            alert1.setPositiveButton("OK", null);
                            alert1.show();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.dismiss();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Uang sejumlah " + etnabung.getText().toString().trim() + " akan ditambahkan ke tabungan anda").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();

        }
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

    private class CurrencyTextWatcher implements TextWatcher {
        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String formatted = null;
            if (!mEditing) {
                mEditing = true;

                Locale local = new Locale("in", "id");
                String digits = s.toString().replaceAll("\\D", "");
                NumberFormat nf = NumberFormat.getCurrencyInstance(local);
                try {
                    formatted = nf.format(Double.parseDouble(digits));
                    s.replace(0, s.length(), formatted);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }
                mEditing = false;
            }
            duit = formatted;
        }
    }
}

