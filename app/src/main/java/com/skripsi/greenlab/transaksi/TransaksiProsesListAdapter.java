package com.skripsi.greenlab.transaksi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

public class TransaksiProsesListAdapter extends RecyclerView.Adapter<TransaksiProsesListAdapter.ViewHolder> {
    private List<vProses> daftarproses,daftarprosesAll;
    private AppDatabase database;
    private Context context;

    public TransaksiProsesListAdapter(List<vProses> daftarproses, Context context){
        this.daftarproses=daftarproses;
        this.context=context;
        daftarprosesAll=new ArrayList<>();
        daftarprosesAll.addAll(daftarproses);
        database=AppDatabase.getInstance(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nama,tv_tgl,tv_faktur;
        private ImageButton btn_wasap,btn_telp;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_nama=itemView.findViewById(R.id.tv_prosesnama);
            tv_tgl=itemView.findViewById(R.id.tv_prosestgl);
            tv_faktur=itemView.findViewById(R.id.tv_prosesfaktur);
            btn_wasap=itemView.findViewById(R.id.proses_imgwasap);
            btn_telp=itemView.findViewById(R.id.proses_imgtlp);
        }
    }

    @Override
    public TransaksiProsesListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_proses,viewGroup, false);
        return new TransaksiProsesListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransaksiProsesListAdapter.ViewHolder viewHolder, int i) {
        final String telp=daftarproses.get(i).getTelp();
        String nama=daftarproses.get(i).getNama();
        String tglterima=daftarproses.get(i).getTglterima();
        String tglselesai=daftarproses.get(i).getTglselesai();
        final String faktur=daftarproses.get(i).getFaktur();
        viewHolder.tv_nama.setText(nama);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(tglterima);
        stringBuilder.append(" - ");
        stringBuilder.append(tglselesai);
        viewHolder.tv_tgl.setText(stringBuilder.toString());
        viewHolder.tv_faktur.setText(faktur);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,TransaksiProsesStatusActivity.class);
                intent.putExtra("faktur",faktur);
                context.startActivity(intent);
            }
        });
        viewHolder.btn_wasap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("android.intent.action.MAIN");
                intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT","");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("62");
                stringBuilder.append(telp);
                stringBuilder.append("@s.whatsapp.net");
                intent.putExtra("jid", stringBuilder.toString());
                intent.setPackage("com.whatsapp");
                context.startActivity(intent);
                return;
            }
        });
        viewHolder.btn_telp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("android.intent.action.DIAL");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("tel:+62");
                stringBuilder.append(telp);
                intent.setData(Uri.parse(stringBuilder.toString()));
                context.startActivity(intent);
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return daftarproses.size();
    }

    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<vProses> filteredList=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredList.addAll(daftarprosesAll);
                }else{
                    for(vProses row : daftarprosesAll ){
                        if(row.getNama().toLowerCase().contains(charSequence.toString().toLowerCase())||
                        row.getStatuslaundry().toLowerCase().contains(charSequence.toString().toLowerCase())||
                        row.getFaktur().toLowerCase().contains(charSequence.toString().toLowerCase())){
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
                daftarproses=(ArrayList<vProses>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
