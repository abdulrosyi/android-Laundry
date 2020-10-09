package com.skripsi.greenlab.master;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MasterJasaRecyclerAdapter extends RecyclerView.Adapter<MasterJasaRecyclerAdapter.ViewHolder> {
    private List<MasterJasa> daftarjasa,daftarjasaAll;
    private AppDatabase database;
    private Context context;
    private List<String> daftarkategori;

    public MasterJasaRecyclerAdapter(List<MasterJasa> daftarjasa, Context context){
        this.daftarjasa=daftarjasa;
        this.context=context;
        daftarjasaAll=new ArrayList<>();
        daftarjasaAll.addAll(daftarjasa);
        database=AppDatabase.getInstance(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtjasa,txtharga,txtsatuan,txtkategori;
        private ImageButton imgbtn;
        public ViewHolder(View itemView) {
            super(itemView);
            txtjasa=itemView.findViewById(R.id.txt_namajasa);
            txtharga=itemView.findViewById(R.id.txt_hargajasa);
            txtkategori=itemView.findViewById(R.id.txt_kategorijasa);
            imgbtn=itemView.findViewById(R.id.overflowjasa);
        }
    }

    @Override
    public MasterJasaRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_master_jasa,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MasterJasaRecyclerAdapter.ViewHolder holder, final int position) {
        final String getJasa=daftarjasa.get(position).getNamajasa();
        final int getHarga=daftarjasa.get(position).getHargajasa();
        final String getSatuan=daftarjasa.get(position).getSatuanjasa();
        final String getKategori=daftarjasa.get(position).getKategori();
        Locale localeID=new Locale("in","ID");
        NumberFormat formatRp=NumberFormat.getCurrencyInstance(localeID);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(formatRp.format(Double.parseDouble(String.valueOf(getHarga))));
        stringBuilder.append(" / ");
        stringBuilder.append(getSatuan);
        holder.txtharga.setText(stringBuilder.toString());
        holder.txtjasa.setText(getJasa);
        holder.txtkategori.setText(getKategori);
        holder.imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                        database.jasaDAO().deleteJasa(daftarjasa.get(position));
                                        daftarjasa.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,daftarjasa.size());
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
                                View customlayout=inflater.inflate(R.layout.popup_update_jasa,null);
                                builder2.setView(customlayout);
                                final TextInputEditText et_kategorijasa=customlayout.findViewById(R.id.et_kategori_jasa);
                                final TextInputEditText et_namajasa=customlayout.findViewById(R.id.et_nama_jasa);
                                final TextInputEditText et_hargajasa=customlayout.findViewById(R.id.et_harga_jasa);
                                final Spinner spinnersatuan=customlayout.findViewById(R.id.spinner_satuan_jasa);
                                final MasterJasa jasa=daftarjasa.get(position);
                                et_kategorijasa.setText(getKategori);
                                et_namajasa.setText(getJasa);
                                editToRp(et_hargajasa);
                                et_hargajasa.setText(String.valueOf(getHarga));
                                String[] satuan={"Piece (PC)","Kilogram (KG)","MeterPersegi (M\u00B2)"};
                                ArrayList<String> daftarsatuan= new ArrayList<>(Arrays.asList(satuan));
                                ArrayAdapter<String> adaptersatuan=new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,daftarsatuan);
                                adaptersatuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnersatuan.setAdapter(adaptersatuan);
                                if(getSatuan=="Piece (PC)"){
                                    int spinerposisi=adaptersatuan.getPosition(getSatuan);
                                    spinnersatuan.setSelection(spinerposisi);
                                }else if (getSatuan=="Kilogram (KG)"){
                                    int spinerposisi=adaptersatuan.getPosition(getSatuan);
                                    spinnersatuan.setSelection(spinerposisi);
                                }else{
                                    int spinnerposisi=adaptersatuan.getPosition(getSatuan);
                                    spinnersatuan.setSelection(spinnerposisi);
                                }
                                builder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        jasa.setKategori(et_kategorijasa.getText().toString().trim());
                                        jasa.setNamajasa(et_namajasa.getText().toString().trim());
                                        String harga=et_hargajasa.getText().toString().trim().replace(".","");
                                        int hargajasa=Integer.parseInt(harga);
                                        jasa.setHargajasa(hargajasa);
                                        jasa.setSatuanjasa(spinnersatuan.getSelectedItem().toString());
                                        database.jasaDAO().updateJasa(jasa);
                                        daftarjasa.set(position,jasa);
                                        notifyItemChanged(position);
                                        Toast.makeText(context, "Data berhasil diubah",Toast.LENGTH_SHORT);
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
    private void editToRp(final TextInputEditText textInputEditText){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            private String current="";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(current)){
                    textInputEditText.removeTextChangedListener(this);
                    Locale locale=new Locale("id","id");
                    String replaceble=String.format("[Rp..\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol(locale));
                    String cleanString=editable.toString().replaceAll(replaceble,"");
                    double parsed;
                    try{
                        parsed=Double.parseDouble(cleanString);
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                        parsed=0.00;
                    }
                    NumberFormat formaterr=NumberFormat.getCurrencyInstance(locale);
                    formaterr.setMaximumFractionDigits(0);
                    formaterr.setParseIntegerOnly(true);
                    String formated=formaterr.format(parsed);
                    String replace=String.format("[Rp\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(locale));
                    String clean=formated.replaceAll(replace,"");
                    current=formated;
                    textInputEditText.setText(clean);
                    textInputEditText.setSelection(clean.length());
                    textInputEditText.addTextChangedListener(this);
                }

            }
        });

    }
}
