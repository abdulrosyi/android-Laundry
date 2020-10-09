package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.master.MasterPelanggan;

import java.util.ArrayList;
import java.util.List;

public class TransaksiHutangListAdapter extends RecyclerView.Adapter<TransaksiHutangListAdapter.ViewHolder> {
    private List<MasterPelanggan> daftarpelanggan,daftarpelangganAll;
    private AppDatabase database;
    private Context context;

    public TransaksiHutangListAdapter(List<MasterPelanggan> daftarpelanggan, Context context){
        this.daftarpelanggan=daftarpelanggan;
        this.context=context;
        daftarpelangganAll=new ArrayList<>();
        daftarpelangganAll.addAll(daftarpelanggan);
        database=AppDatabase.getInstance(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nama,tv_hutang;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_nama=itemView.findViewById(R.id.tv_hutangnama);
            tv_hutang=itemView.findViewById(R.id.tv_hutang);
        }
    }

    @Override
    public TransaksiHutangListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_hutang,viewGroup, false);
        return new TransaksiHutangListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransaksiHutangListAdapter.ViewHolder viewHolder, int i) {
        final long idpelanggan=daftarpelanggan.get(i).getNomer();
        String nama=daftarpelanggan.get(i).getNama();
        int hutang=daftarpelanggan.get(i).getHutang();
        viewHolder.tv_nama.setText(nama);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Rp.");
        stringBuilder.append(hutang);
        viewHolder.tv_hutang.setText(stringBuilder.toString());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,TransaksiHutangBayarActivity.class);
                intent.putExtra("idpelanggan",idpelanggan);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return daftarpelanggan.size();
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
                daftarpelanggan=(ArrayList<MasterPelanggan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
