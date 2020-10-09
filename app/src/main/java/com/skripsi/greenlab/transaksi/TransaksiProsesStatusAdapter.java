package com.skripsi.greenlab.transaksi;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransaksiProsesStatusAdapter extends RecyclerView.Adapter<TransaksiProsesStatusAdapter.ViewHolder> {
    private List<vCart> daftarcart;
    private AppDatabase database;
    private Context context;

    public TransaksiProsesStatusAdapter(List<vCart> daftarcart, Context context){
        this.daftarcart=daftarcart;
        this.context=context;
        database=AppDatabase.getInstance(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_jasa,tv_bayar,tv_jumlah,tv_ket;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_jasa=itemView.findViewById(R.id.tv_namajasa);
            tv_bayar=itemView.findViewById(R.id.tv_harga);
            tv_jumlah=itemView.findViewById(R.id.tv_jumlah);
            tv_ket=itemView.findViewById(R.id.tv_ket);
        }
    }
    @Override
    public TransaksiProsesStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_proses_status,viewGroup, false);
        return new TransaksiProsesStatusAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransaksiProsesStatusAdapter.ViewHolder viewHolder, int position) {
        String keterangan="";
        final int idlaundrydetail=daftarcart.get(position).getIdlaundrydetail();
        final String namakategori=daftarcart.get(position).getNamakategori();
        final String namajasa=daftarcart.get(position).getNamajasa();
        final String ket=daftarcart.get(position).getKeterangan();
        int bayar=daftarcart.get(position).getBiayalaundry();
        int jml=daftarcart.get(position).getJumlahlaundry();
        String satuan=daftarcart.get(position).getSatuanjasa();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(namakategori);
        stringBuilder.append(") ");
        stringBuilder.append(namajasa);
        stringBuilder.append(" - ");
        String string2=stringBuilder.toString();
        viewHolder.tv_jasa.setText(string2);
        Locale localeID=new Locale("in","ID");
        NumberFormat formatRp=NumberFormat.getCurrencyInstance(localeID);
        String stringbayar=String.valueOf(bayar);
        viewHolder.tv_bayar.setText(formatRp.format(Double.parseDouble(stringbayar)));
        StringBuilder stringBuilder1=new StringBuilder();
        stringBuilder1.append(jml);
        stringBuilder1.append(" ");
        if(satuan.equalsIgnoreCase("Piece (Pc)")){
            stringBuilder1.append("/Pcs");
        }if(satuan.equalsIgnoreCase("Kilogram (Kg)")){
            stringBuilder1.append("/Kg");
        }if(satuan.equalsIgnoreCase("MeterPersegi (M\u00B2)")){
            stringBuilder1.append("M\u00B2");
        }
        String string3=stringBuilder1.toString();
        viewHolder.tv_jumlah.setText(string3);
        if(ket.equals("")){
            viewHolder.tv_ket.setText("Tidak ada keterangan");
        }else{
            viewHolder.tv_ket.setText(ket);
        }
        viewHolder.tv_ket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Keterangan");
                stringBuilder2.append(namakategori);
                stringBuilder2.append(" - ");
                stringBuilder2.append(namajasa);
                builder.setTitle(stringBuilder2.toString());
                if (ket.equals("")) {
                    builder.setMessage("Tidak ada keterangan");
                } else {
                    builder.setMessage(ket);
                }
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return daftarcart.size();
    }

}
