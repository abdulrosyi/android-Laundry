package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.master.MasterPelanggan;

import java.util.ArrayList;
import java.util.List;


public class TransaksiCariPelangganAdapter extends RecyclerView.Adapter<TransaksiCariPelangganAdapter.ViewHolder> {
    private List<MasterPelanggan> daftarMasterPelanggan,daftarpelangganAll;
    private AppDatabase database;
    private Context context;

    public TransaksiCariPelangganAdapter(List<MasterPelanggan> daftarMasterPelanggan, Context context){
        this.daftarMasterPelanggan = daftarMasterPelanggan;
        this.context=context;
        daftarpelangganAll=new ArrayList<>();
        daftarpelangganAll.addAll(daftarMasterPelanggan);
        database=AppDatabase.getInstance(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,alamat,telp;
        private ImageButton imgbtn;
        public ViewHolder(View itemView) {
            super(itemView);
            nama=itemView.findViewById(R.id.txt_nama);
            alamat=itemView.findViewById(R.id.txt_alamat);
            telp=itemView.findViewById(R.id.txt_telp);
        }
    }


    @Override
    public TransaksiCariPelangganAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cari_pelanggan,parent, false);
        return new TransaksiCariPelangganAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransaksiCariPelangganAdapter.ViewHolder holder, int position) {
        final Long getNo= daftarMasterPelanggan.get(position).getNomer();
        final String getNama= daftarMasterPelanggan.get(position).getNama();
        final String getAlamat= daftarMasterPelanggan.get(position).getAlamat();
        final String getTelp= daftarMasterPelanggan.get(position).getTelp();
        holder.nama.setText(getNama);
        holder.alamat.setText(getAlamat);
        holder.telp.setText(getTelp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TransaksiCariActivity.class);
                intent.putExtra("nama",getNama);
                intent.putExtra("idpelanggan",getNo);
                ((TransaksiCariActivity) context).setResult(2000, intent);
                ((TransaksiCariActivity) context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return daftarMasterPelanggan.size();
    }

    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<MasterPelanggan> filteredList=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredList.addAll(daftarpelangganAll);
                }else{
                    for(MasterPelanggan row : daftarpelangganAll ){
                        if(row.getNama().toLowerCase().contains(charSequence.toString().toLowerCase())){
                            filteredList.add(row);
                            notifyDataSetChanged();
                        }
                    }
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                daftarMasterPelanggan =(ArrayList<MasterPelanggan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

