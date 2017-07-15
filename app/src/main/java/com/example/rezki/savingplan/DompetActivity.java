package com.example.rezki.savingplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DompetActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_pemasukan, btn_pengeluaran;
    private TextView tv_tanggal_hariini, tv_uang, tv_tgl_pemasukan_terakhir, tv_tgl_pengeluaran_terakhir;

    private TextView tv_stat0, tv_stat2, tv_stat3;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db_Ref;
    private Calendar calendar;

    private Query db_Query;

    private String Uang_User, tanggal,user2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dompet);

        //Deklarasi Variable dan Objek
        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String UserID = user.getUid().toString().trim();

        db_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID);
        db_Ref.keepSynced(true);

        tv_tgl_pemasukan_terakhir = (TextView) findViewById(R.id.tv_tgl_pemasukan_terakhir);
        tv_tgl_pengeluaran_terakhir = (TextView) findViewById(R.id.tv_tgl_pengeluaran_terakhir);
        tv_uang = (TextView) findViewById(R.id.tv_uang);
        tv_tanggal_hariini = (TextView) findViewById(R.id.tv_tglhariini);

        btn_pemasukan = (Button) findViewById(R.id.btn_pemasukan);
        btn_pemasukan.setOnClickListener(this);

        btn_pengeluaran = (Button) findViewById(R.id.btn_pengeluaran);
        btn_pengeluaran.setOnClickListener(this);

        tv_stat0 = (TextView) findViewById(R.id.tv_statuang0);
        tv_stat2 = (TextView) findViewById(R.id.tv_statuang2);
        tv_stat3 = (TextView) findViewById(R.id.tv_statuang3);

        //Mengambil data uang user
        //Mengambil Waktu Pengeluaran dan Pemasukan Terakhir User
        db_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String kategoriTerakhir = (String) dataSnapshot.child("kategori_pemasukan_terakhir").getValue();
                String kategoriTerakhir2 = (String) dataSnapshot.child("kategori_pengeluaran_terakhir").getValue();
                String uang_user = (String) dataSnapshot.child("uang").getValue();
                Integer uang = Integer.parseInt(uang_user);

                if (!kategoriTerakhir.equals("-") && uang<=0 ){
                    btn_pengeluaran.setEnabled(false);
                    tv_stat3.setText("Uang Anda Habis!");
                    tv_stat3.setVisibility(View.VISIBLE);
                } else if (kategoriTerakhir.equals("-") && uang<=0){
                    btn_pengeluaran.setEnabled(false);
                    tv_stat0.setText("Silahkan Catat Pemasukan Terlebih Dahulu");
                    tv_stat0.setVisibility(View.VISIBLE);
                    tv_stat3.setVisibility(View.GONE);
                } else if(uang>0 && uang<100000){
                    btn_pengeluaran.setEnabled(true);
                    tv_stat2.setText("Anda Harus Lebih Hemat!");
                    tv_stat2.setVisibility(View.VISIBLE);
                    tv_stat3.setVisibility(View.GONE);
                } else if(uang>=100000){
                    btn_pengeluaran.setEnabled(true);
                    tv_stat0.setText("Status Keuangan : Aman");
                    tv_stat0.setVisibility(View.VISIBLE);
                    tv_stat3.setVisibility(View.GONE);
                }
                    Locale local = new Locale("id", "ID");
                    NumberFormat nf = NumberFormat.getCurrencyInstance(local);
                    String rupiah = nf.format(Double.parseDouble(uang_user));
                    tv_uang.setText(rupiah);

                String tanggal_pemasukan = (String) dataSnapshot.child("tgl_pemasukan_terakhir").getValue();
                String tanggal_pengeluaran = (String) dataSnapshot.child("tgl_pengeluaran_terakhir").getValue();
                tv_tgl_pemasukan_terakhir.setText(tanggal_pemasukan+" dari "+kategoriTerakhir);
                tv_tgl_pengeluaran_terakhir.setText(tanggal_pengeluaran+" untuk "+kategoriTerakhir2);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ambilTanggalHariIni();
    }

    //Code untuk double back klik exit
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

    //Code untuk memnuculkan progress dialog pada saat masuk dompet activity
    //Progress dialog jadi gabisa di cancel sampai selesai download
    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.onStart();
        progressDialog.setMessage("Loading Data");;
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    //Code untuk mengambil dan menampilkan tanggal hari ini
    public void ambilTanggalHariIni(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String Tanggal_HariIni = dateFormat.format(date);
        tv_tanggal_hariini.setText(Tanggal_HariIni);
    }

    //Code supaya link dan tombol aktif
    @Override
    public void onClick(View view) {
        if(btn_pemasukan==view){
            Intent loginIntent = new Intent(DompetActivity.this, Pemasukan_List.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        } else if(view==btn_pengeluaran){
            Intent loginIntent = new Intent(DompetActivity.this, Pengeluaran_List.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
    }

    //menampilkan menu dari menu.xml/menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_dompet, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      if ( item.getItemId() == R.id.logout){

            logout();

        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(DompetActivity.this, MainMenu.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //fungsi LOGOUT FIREBASE
    private void logout() {

        FirebaseAuth firebaseauth;
        firebaseauth = FirebaseAuth.getInstance();
        firebaseauth.signOut();

    }
}
