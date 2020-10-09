package com.skripsi.greenlab.laporan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.util.ArrayList;

public class LaporanAdapterKebutuhanList extends RecyclerView.Adapter<LaporanAdapterKebutuhanList.ViewHolder> {
    private ArrayList<String> daftarkebutuhan;
    private AppDatabase database;
    private Context context;
    public LaporanAdapterKebutuhanList(ArrayList<String> daftarkebutuhan, Context context){
        this.daftarkebutuhan=daftarkebutuhan;
        this.context=context;
        database=AppDatabase.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_kebutuhan,viewGroup, false);
        return new LaporanAdapterKebutuhanList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] arrstring=daftarkebutuhan.get(i).split("__");
        viewHolder.nama.setText(arrstring[0]);
        viewHolder.tgl.setText(arrstring[1]);
        viewHolder.faktur.setText(arrstring[2]);
        viewHolder.ket.setText(arrstring[3]);
        viewHolder.harga.setText(arrstring[4]);
    }

    @Override
    public int getItemCount() {
        return daftarkebutuhan.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,faktur,tgl,ket,harga;
        public ViewHolder(View itemView) {
            super(itemView);
            nama=itemView.findViewById(R.id.tv_kebutuhan);
            faktur=itemView.findViewById(R.id.tv_kebutuhan_faktur);
            tgl=itemView.findViewById(R.id.tv_kebutuhan_tgl);
            harga=itemView.findViewById(R.id.tv_kebutuhan_harga);
            ket=itemView.findViewById(R.id.tv_kebutuhan_ket);
        }
    }
}
