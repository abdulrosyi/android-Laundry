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

import java.util.ArrayList;
import java.util.Arrays;

public class TransaksiProsesActivity extends AppCompatActivity {
    private Context context;
    private AppDatabase database;
    private RecyclerView recyclerView;
    private TransaksiProsesListAdapter transaksiProsesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<vProses> daftarproses;
    private SearchView txt_cari;
    private Toolbar tulbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_transaksi_proses);
        tulbar=findViewById(R.id.tulbar);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Proses");
        recyclerView=findViewById(R.id.recycler_proses);
        daftarproses=new ArrayList<>();
        database =AppDatabase.getInstance(context);
        daftarproses.addAll(Arrays.asList(database.prosesDAO().readProses("Proses")));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        transaksiProsesAdapter= new TransaksiProsesListAdapter(daftarproses,TransaksiProsesActivity.this);
        recyclerView.setAdapter(transaksiProsesAdapter);
        txt_cari=findViewById(R.id.txt_cari);
        txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transaksiProsesAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                transaksiProsesAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
