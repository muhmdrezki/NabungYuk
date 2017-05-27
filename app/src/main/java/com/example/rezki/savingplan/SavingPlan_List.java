package com.example.rezki.savingplan;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SavingPlan_List extends AppCompatActivity {

    private RecyclerView planlist;
    private DatabaseReference databaseReference;
    private Query QueryDatabase;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_plan__list);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plans");
        databaseReference.keepSynced(true);

        firebaseauth = FirebaseAuth.getInstance();
        user = firebaseauth.getCurrentUser();

        QueryDatabase = databaseReference.orderByChild("uid").equalTo(user.getUid());

        planlist = (RecyclerView) findViewById(R.id.planlist);
        planlist.setHasFixedSize(true);
        planlist.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        planlist.setHasFixedSize(true);
        planlist.setLayoutManager(linearLayoutManager);

        firebaseauth = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseRecyclerAdapter<Plans, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Plans, PostViewHolder>(

                        Plans.class, R.layout.plan_list, PostViewHolder.class, QueryDatabase
                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, Plans model, int position) {

                        final String post_key = getRef(position).getKey();

                        viewHolder.setNama_plan(String.valueOf(model.getNama_plan()));
                        viewHolder.setTarget(String.valueOf(model.getTarget()));
                        viewHolder.setTabungan(String.valueOf(model.getTabungan()));

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(SavingPlan_List.this, post_key, Toast.LENGTH_LONG).show();
                                Intent SinglePostIntent = new Intent(SavingPlan_List.this, SavingPlanFinalAct.class);
                                SinglePostIntent.putExtra("post_id", post_key);
                                startActivity(SinglePostIntent);
                            }
                        });
                    }
                };
                planlist.setAdapter(firebaseRecyclerAdapter);
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

        public void setNama_plan(String nama_plan) {
            TextView namaplan= (TextView) view.findViewById(R.id.tv_namaplanCV);
            namaplan.setText(nama_plan);
        }

        public void setTabungan(String tabungan) {
            TextView tabungan_skrng = (TextView) view.findViewById(R.id.tv_tabunganCV);
            tabungan_skrng.setText("Jumlah tabungan anda saat ini :"+"Rp "+tabungan);
        }

        public void setTarget(String target) {
            TextView target_plan = (TextView) view.findViewById(R.id.tv_targetCV);
            target_plan.setText("Jumlah target :"+"Rp "+target);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add){

            startActivity(new Intent(SavingPlan_List.this,SavingPlanAct1.class));

        } else if ( item.getItemId() == R.id.logout){

            logout();

        } else if ( item.getItemId() == R.id.main_menu){
            startActivity(new Intent(SavingPlan_List.this, MainMenu.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        firebaseauth.signOut();

    }

}
