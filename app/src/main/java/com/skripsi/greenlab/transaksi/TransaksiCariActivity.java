package com.skripsi.greenlab.transaksi;

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
import androidx.room.Room;


import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.master.MasterJasa;
import com.skripsi.greenlab.master.MasterJasaInput;
import com.skripsi.greenlab.master.MasterPelanggan;
import com.skripsi.greenlab.master.MasterPelangganInput;

import java.util.ArrayList;
import java.util.Arrays;

public class TransaksiCariActivity extends AppCompatActivity {
    private String a;
    private Toolbar tulbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase database;
    private ArrayList<MasterPelanggan> daftarpelanggan;
    private ArrayList<MasterJasa> daftarjasa;
    private TransaksiCariPelangganAdapter cariPelangganAdapter;
    private TransaksiCariJasaAdapter jasaAdapter;
    private SearchView txt_cari;
    private Button btn_tambah;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_cari);
        context=getApplicationContext();
        database = AppDatabase.getInstance(context);
        String a=getIntent().getStringExtra("cari");
        if(a.equals("pelanggan")){
            tulbar=findViewById(R.id.tulbar);
            setSupportActionBar(tulbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cari Pelanggan");
            recyclerView = findViewById(R.id.recycler_transaksi);
            daftarpelanggan = new ArrayList<>();
            daftarpelanggan.addAll(Arrays.asList(database.pelangganDAO().readDataPelanggan()));
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            cariPelangganAdapter=new TransaksiCariPelangganAdapter(daftarpelanggan, TransaksiCariActivity.this);
            recyclerView.setAdapter(cariPelangganAdapter);
            txt_cari=findViewById(R.id.txt_cari);
            txt_cari.setQueryHint("Cari Pelanggan");
            txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    cariPelangganAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    cariPelangganAdapter.getFilter().filter(newText);
                    return false;
                }
            });
            btn_tambah=findViewById(R.id.btn_tambah);
            btn_tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(TransaksiCariActivity.this, MasterPelangganInput.class));
                }
            });
        }else if(a.equals("jasa")){
            tulbar=findViewById(R.id.tulbar);
            setSupportActionBar(tulbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cari Jasa");
            recyclerView=findViewById(R.id.recycler_transaksi);
            daftarjasa=new ArrayList<>();
            daftarjasa.addAll(Arrays.asList(database.jasaDAO().readDataJasa()));
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            jasaAdapter=new TransaksiCariJasaAdapter(daftarjasa, TransaksiCariActivity.this);
            recyclerView.setAdapter(jasaAdapter);
            txt_cari=findViewById(R.id.txt_cari);
            txt_cari.setQueryHint("Cari Jasa");
            txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    jasaAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    jasaAdapter.getFilter().filter(newText);
                    return false;
                }
            });
            btn_tambah=findViewById(R.id.btn_tambah);
            btn_tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(TransaksiCariActivity.this, MasterJasaInput.class));
                }
            });
        }
    }
}
