package com.skripsi.greenlab.master;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.util.ArrayList;
import java.util.Arrays;


public class MasterPelangganList extends AppCompatActivity {
    private Button btn_tambah;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase database;
    private ArrayList<MasterPelanggan> daftarpelanggan;
    private MasterPelangganRecyclerAdapter pelangganAdapter;
    private SearchView txt_cari;
    private Toolbar tulbar;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list_pelanggan);
        context=getApplicationContext();
        btn_tambah = findViewById(R.id.btn_tambah_pelanggan);
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterPelangganList.this, MasterPelangganInput.class);
                startActivity(intent);
            }
        });
        tulbar=findViewById(R.id.tulbar);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Pelanggan");
        recyclerView = findViewById(R.id.recycler_pelanggan);
        daftarpelanggan = new ArrayList<>();
        database=AppDatabase.getInstance(context);
        daftarpelanggan.addAll(Arrays.asList(database.pelangganDAO().readDataPelanggan()));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        pelangganAdapter=new MasterPelangganRecyclerAdapter(daftarpelanggan,MasterPelangganList.this);
        recyclerView.setAdapter(pelangganAdapter);
        txt_cari=findViewById(R.id.txt_cari);
        txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pelangganAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pelangganAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
