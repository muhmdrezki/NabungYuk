package com.example.rezki.savingplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Pemasukan_List extends AppCompatActivity {

    private RecyclerView list_pemasukan;

    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;
    private FirebaseUser user;
    private DatabaseReference db_Ref, db_RefNew, getDb_time;
    private Query db_time;
    private String last_key, Key, category, jumlah_pemasukan, details, tanggal;

    private ProgressDialog progressDialog;
    private String keychild;

    final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasukan__list);

        firebaseauth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        user = firebaseauth.getCurrentUser();
        String userId = user.getUid().toString().trim();

        db_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Pemasukan");
        db_Ref.keepSynced(true);

        db_RefNew = FirebaseDatabase.getInstance().getReference().child("Pemasukan_Terakhir");
        db_RefNew.keepSynced(true);

        db_time = db_Ref.getRef().limitToLast(1);

        keychild = db_time.getRef().getKey();

        getDb_time = db_Ref.child(keychild);


        list_pemasukan = (RecyclerView) findViewById(R.id.pemasukan_list);
        list_pemasukan.setHasFixedSize(true);
        list_pemasukan.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        list_pemasukan.setHasFixedSize(true);
        list_pemasukan.setLayoutManager(linearLayoutManager);

        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseRecyclerAdapter<Pemasukan, Pemasukan_List.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pemasukan, Pemasukan_List.PostViewHolder>(

                        Pemasukan.class, R.layout.pemasukan_list, Pemasukan_List.PostViewHolder.class, db_Ref
                ) {
                    @Override
                    protected void populateViewHolder(Pemasukan_List.PostViewHolder viewHolder, Pemasukan model, int position) {

                        final String post_key = getRef(position).getKey();
                        Key = post_key.toString();

                        viewHolder.setKategori(String.valueOf(model.getKategori()));
                        viewHolder.setDetail(String.valueOf(model.getDetail()));
                        viewHolder.setJumlah(String.valueOf(model.getJumlah()));
                        viewHolder.setTgl_pemasukan(String.valueOf(model.getTgl_pemasukan()));


                    }
                };
                list_pemasukan.setAdapter(firebaseRecyclerAdapter);
            }
        };
    //catatpemasukanterakhir();

    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        Log.d(TAG, "click");

        if (doubleBackToExitPressedOnce==true) {
            //super.onBackPressed();
            Intent  intent = new Intent(Intent.ACTION_MAIN);
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

    @Override
    protected void onStart() {
        super.onStart();
        firebaseauth.addAuthStateListener(authlistener);
    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View view;

        public PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setKategori(String kategori) {
            TextView category= (TextView) view.findViewById(R.id.tv_kategori);
            category.setText("Kategori : "+kategori);
        }

        public void setTgl_pemasukan(String tgl_pemasukan) {
            TextView tgl= (TextView) view.findViewById(R.id.tv_tanggal);
            tgl.setText("Tanggal Pemasukan : "+tgl_pemasukan);
        }

        public void setJumlah(String jumlah) {
            TextView jml= (TextView) view.findViewById(R.id.tv_Jumlah_Pemasukan);
            jml.setText("Jumlah Pemasukan : Rp. "+jumlah);
        }

        public void setDetail(String detail) {
            TextView details = (TextView) view.findViewById(R.id.tv_detail);
            details.setText("Detail : "+detail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_pemasukan, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add){

            startActivity(new Intent(Pemasukan_List.this,CatatPemasukanAct.class));

        } else if ( item.getItemId() == R.id.My_Dompet){
            Bundle b = new Bundle();
            b.putString("lastest_key", last_key);
            startActivity(new Intent(Pemasukan_List.this,DompetActivity.class).putExtras(b));

        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(Pemasukan_List.this, MainMenu.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
