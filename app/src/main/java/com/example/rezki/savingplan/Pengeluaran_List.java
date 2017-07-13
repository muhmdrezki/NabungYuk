package com.example.rezki.savingplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Rezki on 6/7/2017.
 */

public class Pengeluaran_List extends AppCompatActivity{

    private RecyclerView list_pengeluaran;

    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;
    private FirebaseUser user;
    private DatabaseReference db_Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran__list);

        //deklarasi firebase auth
        firebaseauth = FirebaseAuth.getInstance();
        //memanggil user yang sedang login
        user = firebaseauth.getCurrentUser();

        //menentukan path child pengeluaran
        db_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Pengeluaran");
        db_Ref.keepSynced(true);

        //konfigurasi layout recyclerview dengan pemasukan_list.xml
        list_pengeluaran = (RecyclerView) findViewById(R.id.pemasukan_list);
        list_pengeluaran.setHasFixedSize(true);
        list_pengeluaran.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        list_pengeluaran.setHasFixedSize(true);
        list_pengeluaran.setLayoutManager(linearLayoutManager);

        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseRecyclerAdapter<Pengeluaran, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pengeluaran, PostViewHolder>(

                        Pengeluaran.class, R.layout.pemasukan_list, PostViewHolder.class, db_Ref
                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, Pengeluaran model, int position) {

                        final String post_key = getRef(position).getKey();
                        viewHolder.setKategori(String.valueOf(model.getKategori()));
                        viewHolder.setDetail(String.valueOf(model.getDetail()));
                        viewHolder.setJumlah(String.valueOf(model.getJumlah()));
                        viewHolder.setTgl_pengeluaran(String.valueOf(model.getTgl_pengeluaran()));


                    }
                };
                list_pengeluaran.setAdapter(firebaseRecyclerAdapter);
            }
        };
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

        public void setTgl_pengeluaran(String tgl_pengeluaran) {
            TextView tgl= (TextView) view.findViewById(R.id.tv_tanggal);
            tgl.setText("Tanggal Pengeluaran : "+tgl_pengeluaran);
        }

        public void setJumlah(String jumlah) {
            TextView jml= (TextView) view.findViewById(R.id.tv_Jumlah_Pemasukan);
            Locale local = new Locale("id", "ID");
            NumberFormat nf = NumberFormat.getCurrencyInstance(local);
            String rupiah = nf.format(Double.parseDouble(jumlah));
            jml.setText("Jumlah Pengeluaran : "+rupiah);
        }

        public void setDetail(String detail) {
            TextView details = (TextView) view.findViewById(R.id.tv_detail);
            details.setText("Detail : "+detail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_pengeluaran, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add){

            startActivity(new Intent(Pengeluaran_List.this,CatatPengeluaranAct.class));

        } else if ( item.getItemId() == R.id.My_Dompet){
            startActivity(new Intent(Pengeluaran_List.this,DompetActivity.class));

        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(Pengeluaran_List.this, MainMenu.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
