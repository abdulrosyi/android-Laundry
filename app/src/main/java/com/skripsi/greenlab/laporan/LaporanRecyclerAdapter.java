package com.skripsi.greenlab.laporan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.greenlab.R;


public class LaporanRecyclerAdapter extends RecyclerView.Adapter<LaporanRecyclerAdapter.ViewHolder> {
    private String[] txtheader,txtsub;
    private Context context;

    public LaporanRecyclerAdapter(String[] txtheader, String[] txtsub, Context context){
        this.txtheader=txtheader;
        this.txtsub=txtsub;
        this.context=context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView header,sub;
        private ConstraintLayout list_laporan;

        public ViewHolder(View itemView) {
            super(itemView);
            header=itemView.findViewById(R.id.txt_header);
            sub=itemView.findViewById(R.id.txt_sub);
            list_laporan=itemView.findViewById(R.id.list_laporan);
            context=itemView.getContext();
            list_laporan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    switch (getAdapterPosition()){
                        case 0:
                            intent=new Intent(context,LaporanPelangganList.class);
                            break;
                        case 1:
                            intent=new Intent(context, LaporanJasaList.class);
                            break;
                        case 2:
                            intent=new Intent(context, LaporanTransaksiList.class);
                            break;
                        case 3:
                            intent=new Intent(context, LaporanKebutuhanList.class);
                            break;
                        case 4:
                            intent=new Intent(context,LaporanHutangList.class);
                    }
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public LaporanRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan,parent, false);
        return new LaporanRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LaporanRecyclerAdapter.ViewHolder holder, final int position) {
        holder.header.setText(txtheader[position]);
        holder.sub.setText(txtsub[position]);
    }

    @Override
    public int getItemCount() {
        return txtheader.length;
    }

}
