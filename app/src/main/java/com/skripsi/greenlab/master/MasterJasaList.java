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
import androidx.room.Room;


import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.util.ArrayList;
import java.util.Arrays;

public class MasterJasaList extends AppCompatActivity {
    private AppDatabase database;
    private Button btn_pindah;
    String text="";
    private SearchView txt_cari;
    private MasterJasaRecyclerAdapter jasaAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MasterJasa> daftarjasa;
    private Toolbar tulbar;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list_jasa);
        context=getApplicationContext();
        tulbar=findViewById(R.id.tulbar);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Jasa");
        recyclerView=findViewById(R.id.recycler_jasa);
        daftarjasa=new ArrayList<>();
        database=AppDatabase.getInstance(context);
        daftarjasa.addAll(Arrays.asList(database.jasaDAO().readDataJasa()));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        jasaAdapter=new MasterJasaRecyclerAdapter(daftarjasa, MasterJasaList.this);
        recyclerView.setAdapter(jasaAdapter);
        btn_pindah=findViewById(R.id.btn_pindah_inputjasa);
        btn_pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterJasaList.this, MasterJasaInput.class);
                startActivity(intent);
            }
        });
        txt_cari=findViewById(R.id.txt_carijasa);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
