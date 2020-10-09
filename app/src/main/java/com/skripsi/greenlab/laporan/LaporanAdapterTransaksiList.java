package com.skripsi.greenlab.laporan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.transaksi.vLaundry;

import java.util.ArrayList;
import java.util.List;

public class LaporanAdapterTransaksiList extends RecyclerView.Adapter<LaporanAdapterTransaksiList.ViewHolder> {
    private ArrayList<String> daftarlaundry;
    private AppDatabase database;
    private Context context;

    public LaporanAdapterTransaksiList(ArrayList<String> daftarlaundry, Context context){
        this.daftarlaundry = daftarlaundry;
        this.context=context;
        database=AppDatabase.getInstance(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView fakturnama,tgl,kategorijasa,harga;
        public ViewHolder(View itemView) {
            super(itemView);
            fakturnama=itemView.findViewById(R.id.fakturnama);
            tgl=itemView.findViewById(R.id.tgl);
            kategorijasa=itemView.findViewById(R.id.kategorijasa);
            harga=itemView.findViewById(R.id.harga);
        }
    }

    @Override
    public LaporanAdapterTransaksiList.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_transaksi,viewGroup, false);
        return new LaporanAdapterTransaksiList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LaporanAdapterTransaksiList.ViewHolder viewHolder, int i) {
        String[] arrstring=daftarlaundry.get(i).split("__");
        StringBuilder stringBuilder1=new StringBuilder();
        stringBuilder1.append(arrstring[0]);
        stringBuilder1.append(" - ");
        stringBuilder1.append(arrstring[1]);
        viewHolder.fakturnama.setText(stringBuilder1.toString());
        viewHolder.tgl.setText(arrstring[2]);
        viewHolder.kategorijasa.setText(arrstring[3]);
        StringBuilder stringBuilder2=new StringBuilder();
        stringBuilder2.append("Rp. ");
        stringBuilder2.append(arrstring[4]);
        stringBuilder2.append(" x ");
        stringBuilder2.append(arrstring[5]);
        stringBuilder2.append(arrstring[7]);
        stringBuilder2.append(" = ");
        stringBuilder2.append(arrstring[6]);
        viewHolder.harga.setText(stringBuilder2.toString());
    }


    @Override
    public int getItemCount() {
        return daftarlaundry.size();
    }
}
