package com.skripsi.greenlab;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.greenlab.laporan.LaporanRecyclerAdapter;

public class LaporanActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private String[] txtheader={"Laporan Pelanggan","Laporan Jasa","Laporan Transaksi","Laporan Kebutuhan Laundry","Laporan Hutang Pelanggan"};
    private String[] txtsub={"Digunakan untuk melihat laporan pelanggan","Digunakan untuk melihat laporan jasa",
                             "Digunakan untuk melihat laporan transaksi","Digunakan untuk melihat laporan kebutuhan laundry",
                             "Digunakan untuk melihat laporan hutang"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laporan");
        recyclerView=findViewById(R.id.recycler_laporan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter=new LaporanRecyclerAdapter(txtheader,txtsub,this);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
