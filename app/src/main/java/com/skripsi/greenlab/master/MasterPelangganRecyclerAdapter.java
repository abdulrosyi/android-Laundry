package com.skripsi.greenlab.master;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.util.ArrayList;
import java.util.List;

public class MasterPelangganRecyclerAdapter extends RecyclerView.Adapter<MasterPelangganRecyclerAdapter.ViewHolder> {
    private List<MasterPelanggan> daftarPelanggan,daftarpelangganAll;
    private AppDatabase database;
    private Context context;

    public MasterPelangganRecyclerAdapter(List<MasterPelanggan> daftarPelanggan, Context context){
        this.daftarPelanggan=daftarPelanggan;
        this.context=context;
        daftarpelangganAll=new ArrayList<>();
        daftarpelangganAll.addAll(daftarPelanggan);
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
            imgbtn=itemView.findViewById(R.id.overflow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_master_pelanggan,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        Long getNo=daftarPelanggan.get(position).getNomer();
        final String getNama=daftarPelanggan.get(position).getNama();
        final String getAlamat=daftarPelanggan.get(position).getAlamat();
        final String getTelp=daftarPelanggan.get(position).getTelp();
        holder.nama.setText(getNama);
        holder.alamat.setText(getAlamat);
        holder.telp.setText(getTelp);
        holder.imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupmenu=new PopupMenu(view.getContext(),view);
                popupmenu.inflate(R.menu.menu_popup);
                popupmenu.show();
                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.hapus:
                                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                builder.setTitle("Hapus Data Pelanggan");
                                builder.setMessage("Yaking ingin menghapus data pelanggan");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        database.pelangganDAO().deletePelanggan(daftarPelanggan.get(position));
                                        daftarPelanggan.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,daftarPelanggan.size());
                                        Toast.makeText(context, "Data berhasil dihapus",Toast.LENGTH_SHORT);
                                    }
                                });
                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "Data tidak dihapus",Toast.LENGTH_SHORT);
                                    }
                                });
                                builder.show();
                                break;
                            case R.id.ubah:
                                AlertDialog.Builder builder2=new AlertDialog.Builder(context);
                                builder2.setTitle("Ubah Data Pelanggan");
                                builder2.setCancelable(false);
                                LayoutInflater inflater=LayoutInflater.from(context);
                                View customlayout=inflater.inflate(R.layout.popup_update_pelanggan,null);
                                builder2.setView(customlayout);
                                final EditText editTextnama=customlayout.findViewById(R.id.txtd_input_nama);
                                final EditText editTextalamat=customlayout.findViewById(R.id.txtd_input_alamat);
                                final EditText editTexttelp=customlayout.findViewById(R.id.txtd_input_telp);
                                final MasterPelanggan pelanggan=daftarPelanggan.get(position);
                                editTextnama.setText(getNama);
                                editTextalamat.setText(getAlamat);
                                editTexttelp.setText(getTelp);
                                builder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        pelanggan.setNama(editTextnama.getText().toString());
                                        pelanggan.setAlamat(editTextalamat.getText().toString());
                                        pelanggan.setTelp(editTexttelp.getText().toString());
                                        database.pelangganDAO().updatePelanggan(pelanggan);
                                        daftarPelanggan.set(position,pelanggan);
                                        notifyItemChanged(position);
                                    }
                                });
                                builder2.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "Data tidak diubah",Toast.LENGTH_SHORT);
                                    }
                                });
                                builder2.show();
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount(){
        return daftarPelanggan.size();
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
                daftarPelanggan=(ArrayList<MasterPelanggan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
