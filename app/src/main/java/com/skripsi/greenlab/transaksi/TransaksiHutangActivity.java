package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.master.MasterPelanggan;

import java.util.ArrayList;
import java.util.Arrays;

public class TransaksiHutangActivity extends AppCompatActivity {
    private Context context;
    private Toolbar tulbar;
    private RecyclerView recyclerView;
    private AppDatabase database;
    private ArrayList<MasterPelanggan> daftarpelanggan;
    private RecyclerView.LayoutManager layoutManager;
    private TransaksiHutangListAdapter transaksiHutangListAdapter;
    private SearchView txt_cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_transaksi_hutang);
        tulbar=findViewById(R.id.tulbar);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Hutang");
        recyclerView=findViewById(R.id.recycler_hutang);
        daftarpelanggan=new ArrayList<>();
        database =AppDatabase.getInstance(context);
        daftarpelanggan.addAll(Arrays.asList(database.pelangganDAO().bacaDataHutang()));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        transaksiHutangListAdapter= new TransaksiHutangListAdapter(daftarpelanggan,TransaksiHutangActivity.this);
        recyclerView.setAdapter(transaksiHutangListAdapter);
        txt_cari=findViewById(R.id.txt_cari);
        txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transaksiHutangListAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                transaksiHutangListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}
