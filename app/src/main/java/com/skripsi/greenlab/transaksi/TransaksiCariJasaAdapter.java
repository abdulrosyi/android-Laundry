package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.master.MasterJasa;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransaksiCariJasaAdapter extends RecyclerView.Adapter<TransaksiCariJasaAdapter.ViewHolder> {
    private List<MasterJasa> daftarjasa,daftarjasaAll;
    private AppDatabase database;
    private Context context;

    public TransaksiCariJasaAdapter(List<MasterJasa> daftarjasa, Context context){
        this.daftarjasa=daftarjasa;
        this.context=context;
        daftarjasaAll=new ArrayList<>();
        daftarjasaAll.addAll(daftarjasa);
        database=AppDatabase.getInstance(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private String satuan;
        private TextView txtjasa,txtharga,txtsatuan,txtkategori;
        public ViewHolder(View itemView) {
            super(itemView);
            txtjasa=itemView.findViewById(R.id.txt_namajasa);
            txtharga=itemView.findViewById(R.id.txt_hargajasa);
            txtsatuan=itemView.findViewById(R.id.txt_satuanjasa);
            txtkategori=itemView.findViewById(R.id.txt_kategorijasa);
        }
    }

    @Override
    public TransaksiCariJasaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cari_jasa,parent, false);
        return new TransaksiCariJasaAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransaksiCariJasaAdapter.ViewHolder holder, int position) {
        String satuan="";
        final int getNomer=daftarjasa.get(position).getNomer();
        final String getJasa=daftarjasa.get(position).getNamajasa();
        final int getHarga=daftarjasa.get(position).getHargajasa();
        final String harga=String.valueOf(getHarga);
        final String getSatuan=daftarjasa.get(position).getSatuanjasa();
        final String getKategori=daftarjasa.get(position).getKategori();
        holder.txtjasa.setText(getJasa);
        Locale localeID=new Locale("in","ID");
        NumberFormat formatRp=NumberFormat.getCurrencyInstance(localeID);
        String hargarp=formatRp.format(Double.parseDouble(String.valueOf(getHarga)));
        holder.txtharga.setText(hargarp);
        if(getSatuan.equals("Piece (PC)")){
            satuan="/Pc";
        }if(getSatuan.equals("Kilogram (KG)")){
            satuan="/Kg";
        }if(getSatuan.equals("MeterPersegi (M\u00B2)")){
            satuan="/M\u00B2";
        }
        holder.txtsatuan.setText(satuan);
        holder.txtkategori.setText(getKategori);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TransaksiCariActivity.class);
                intent.putExtra("jasa",getJasa);
                intent.putExtra("idjasa",getNomer);
                intent.putExtra("harga",harga);
                intent.putExtra("satuan",getSatuan);
                intent.putExtra("kategori",getKategori);
                ((TransaksiCariActivity) context).setResult(3000, intent);
                ((TransaksiCariActivity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return daftarjasa.size();
    }

    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<MasterJasa> filteredList=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredList.addAll(daftarjasaAll);
                }else{
                    for(MasterJasa row : daftarjasaAll ){
                        if(row.getNamajasa().toLowerCase().contains(charSequence.toString().toLowerCase())){
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
                daftarjasa=(ArrayList<MasterJasa>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
