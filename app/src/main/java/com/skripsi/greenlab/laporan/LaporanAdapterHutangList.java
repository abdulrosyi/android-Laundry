package com.skripsi.greenlab.laporan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.util.ArrayList;

public class LaporanAdapterHutangList extends RecyclerView.Adapter<LaporanAdapterHutangList.ViewHolder> {
    private ArrayList<String> daftarhutang;
    private Context context;
    private AppDatabase database;

    public LaporanAdapterHutangList(ArrayList<String> daftarhutang, Context context){
        this.daftarhutang=daftarhutang;
        this.context=context;
        database=AppDatabase.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_hutang,viewGroup, false);
        return new LaporanAdapterHutangList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] arrstring=daftarhutang.get(i).split("__");
        viewHolder.tv_nama.setText(arrstring[0]);
        viewHolder.tv_alamat.setText(arrstring[1]);
        viewHolder.tv_telp.setText(arrstring[2]);
        viewHolder.tv_hutang.setText(arrstring[3]);
    }

    @Override
    public int getItemCount() {
        return daftarhutang.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nama,tv_telp,tv_alamat,tv_hutang;
        public ViewHolder(View view){
            super(view);
            tv_nama=view.findViewById(R.id.hutang_nama_pelanggan);
            tv_alamat=view.findViewById(R.id.hutang_alamat);
            tv_telp=view.findViewById(R.id.hutang_telp);
            tv_hutang=view.findViewById(R.id.hutang_jumlah);
        }

    }
}
