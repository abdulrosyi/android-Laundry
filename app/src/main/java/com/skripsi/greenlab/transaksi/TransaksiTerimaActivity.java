package com.skripsi.greenlab.transaksi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TransaksiTerimaActivity extends AppCompatActivity {
    private AppDatabase database;
    private Context context;
    private Calendar kalenderku;
    private TextInputEditText et_tglterima,et_tglselesai,et_faktur,et_nama,et_jasa,et_harga,et_jumlah,et_ket;
    private DatePickerDialog.OnDateSetListener dialogtglterima,dialogtglselesai;
    private ImageButton btn_tglterima,btn_tglselesai,btn_caripelanggan,btn_carijasa,btn_tambah,btn_kurang;
    private String getpelanggan,getnamajasa="",getsatuan="",getkategori="",gethargajasa="",faktur = "00000000";
    private int getidjasa,getjumlah=0,isikeranjang=0;
    private TextView tv_satuan,tv_total;
    private long getidpelanggan;
    private Button btn_keranjang,btn_simpan;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<vCart> daftarcart;
    private TransaksiKeranjangAdapter keranjangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_terima);
        context=getApplicationContext();
        database=AppDatabase.getInstance(context);
        kalenderku=Calendar.getInstance();
        final DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String tgl=dateFormat.format(new Date());
        et_tglterima=findViewById(R.id.txt_input_tglterima);
        et_tglterima.setText(String.valueOf(tgl));
        dialogtglterima=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                kalenderku.set(Calendar.YEAR, i);
                kalenderku.set(Calendar.MONTH,i1);
                kalenderku.set(Calendar.DAY_OF_MONTH,i2);
                String format="dd/MM/yyyy";
                SimpleDateFormat sdf=new SimpleDateFormat(format, Locale.US);
                et_tglterima.setText(sdf.format(kalenderku.getTime()));
            }
        };
        btn_tglterima=findViewById(R.id.btn_tglterima);
        btn_tglterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TransaksiTerimaActivity.this,dialogtglterima,kalenderku.get(Calendar.YEAR),
                        kalenderku.get(Calendar.MONTH),
                        kalenderku.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        et_tglselesai=findViewById(R.id.txt_input_tglselesai);
        et_tglselesai.setText(String.valueOf(tgl));
        dialogtglselesai=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                kalenderku.set(Calendar.YEAR, i);
                kalenderku.set(Calendar.MONTH,i1);
                kalenderku.set(Calendar.DAY_OF_MONTH,i2);
                String format="dd/MM/yyyy";
                SimpleDateFormat sdf=new SimpleDateFormat(format, Locale.US);
                et_tglselesai.setText(sdf.format(kalenderku.getTime()));
            }
        };
        btn_tglselesai=findViewById(R.id.btn_tglselesai);
        btn_tglselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TransaksiTerimaActivity.this,dialogtglselesai,kalenderku.get(Calendar.YEAR),
                        kalenderku.get(Calendar.MONTH),
                        kalenderku.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_caripelanggan=findViewById(R.id.btn_cari_pelanggan);
        btn_caripelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransaksiTerimaActivity.this, TransaksiCariActivity.class);
                intent.putExtra("cari", "pelanggan");
                startActivityForResult(intent, 2000);
            }
        });
        btn_carijasa=findViewById(R.id.btn_cari_jasa);
        btn_carijasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TransaksiTerimaActivity.this, TransaksiCariActivity.class);
                intent.putExtra("cari","jasa");
                startActivityForResult(intent,3000);
            }
        });
        et_faktur=findViewById(R.id.txt_input_faktur);
        getFaktur();
        et_nama=findViewById(R.id.txt_input_pelanggan);
        et_jasa=findViewById(R.id.txt_input_jasa);
        et_harga=findViewById(R.id.txt_input_harga);
        et_jumlah=findViewById(R.id.txt_input_jumlah);
        et_ket=findViewById(R.id.txt_input_keterangan);
        tv_satuan=findViewById(R.id.satuan);
        tv_total=findViewById(R.id.totalbayar);
        btn_tambah=findViewById(R.id.btn_tambah);
        btn_tambah.setVisibility(View.INVISIBLE);
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getjumlah=1+getjumlah;
                setJumlah(getjumlah,gethargajasa);
            }
        });
        btn_kurang=findViewById(R.id.btn_kurang);
        btn_kurang.setVisibility(View.INVISIBLE);
        btn_kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getjumlah=-1+getjumlah;
                setJumlah(getjumlah,gethargajasa);
            }
        });
        btn_keranjang=findViewById(R.id.btn_keranjang);
        btn_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahKeranjang();
            }
        });
        getTotal();
        btn_simpan=findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String string2 = et_faktur.getText().toString();
                String string3 = et_tglselesai.getText().toString();
                String string4 = et_nama.getText().toString();
                Cursor cursor=database.cartDAO().bacaCartbyFaktur(et_faktur.getText().toString());
                isikeranjang=cursor.getCount();
                if(!(TextUtils.isEmpty(string2) ||
                        TextUtils.isEmpty(string3) ||
                        TextUtils.isEmpty(string4) ||
                        isikeranjang == 0)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(TransaksiTerimaActivity.this);
                    builder.create().setTitle("Anda Yakin?");
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append("Anda yakin ingin menyimpan pesanan \n");
                    stringBuilder.append(string2);
                    stringBuilder.append(" - ");
                    stringBuilder.append(string4);
                    builder.setMessage(stringBuilder.toString());
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String totalbayar=tv_total.getText().toString().replaceAll("\\.","").substring(2);
                            database.laundryDAO().updatetotaltabellaundry(Integer.parseInt(totalbayar),string2);
                            berhasil();
                        }
                    });
                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                    return;
                }else{
                    Toast.makeText(TransaksiTerimaActivity.this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==2000){
            getpelanggan=data.getStringExtra("nama");
            getidpelanggan=data.getLongExtra("idpelanggan",0);
            et_nama.setText(getpelanggan);
        }
        if(resultCode==3000){
            getnamajasa=data.getStringExtra("jasa");
            getidjasa=data.getIntExtra("idjasa",0);
            gethargajasa=data.getStringExtra("harga");
            getsatuan=data.getStringExtra("satuan");
            getkategori=data.getStringExtra("kategori");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getjumlah=0;
        et_jumlah.setText("0");
        String string2=getkategori;
        if(!string2.equals("")){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(getkategori);
            stringBuilder.append(" - ");
            stringBuilder.append(getnamajasa);
            string2=stringBuilder.toString();
        }
        String string3= string2;
        if(!gethargajasa.equals("")){
            setJumlah(0,gethargajasa);
        }
        setEdit(getpelanggan,string3,gethargajasa,getkategori,getsatuan);
    }

    @Override
    public void onBackPressed() {
        keluar();
        return;
    }

    private void setJumlah(int n, String string2){
        et_jumlah.setText(String.valueOf(n));
        if(string2.equals("")){
            btn_tambah.setVisibility(View.INVISIBLE);
        }else{
            btn_tambah.setVisibility(View.VISIBLE);
        }
        if(n<1){
            btn_kurang.setVisibility(View.INVISIBLE);
            return;
        }
        btn_kurang.setVisibility(View.VISIBLE);
    }

    private void setEdit(String string2,String string3,String string4,String string5,String string6){
        et_nama.setText(string2);
        et_jasa.setText(string3);
        et_harga.setText(string4);
        if(getsatuan.equalsIgnoreCase("Piece (Pc)")){
            tv_satuan.setText("/Pcs");
            tv_satuan.setVisibility(View.VISIBLE);
            return;
        }if(getsatuan.equalsIgnoreCase("Kilogram (Kg)")){
            tv_satuan.setText("/Kg");
            tv_satuan.setVisibility(View.VISIBLE);
            return;
        }if(getsatuan.equalsIgnoreCase("MeterPersegi (M\u00B2)")){
            tv_satuan.setText("M\u00B2");
            tv_satuan.setVisibility(View.VISIBLE);
        }
    }

    private void getFaktur(){
        String string2;
        ArrayList arrayList=new ArrayList();
        Cursor cursor=database.laundryDAO().bacaidlaundry();
        if (cursor.moveToNext()) {
            do {
                arrayList.add(cursor.getInt(cursor.getColumnIndex("idlaundry")));
            } while (cursor.moveToNext());
        }
        if(cursor.getCount()==0){
            StringBuilder stringBuilder = new StringBuilder();
            String string3 = faktur;
            stringBuilder.append(string3.substring(0, -1 + string3.length()));
            stringBuilder.append("1");
            string2 = stringBuilder.toString();
        }else{
            int n = 1 + (Integer)arrayList.get(-1 + cursor.getCount());
            StringBuilder stringBuilder = new StringBuilder();
            String string4 = faktur;
            stringBuilder.append(string4.substring(0, string4.length() - String.valueOf(n).length()));
            stringBuilder.append(String.valueOf(n));
            string2 = stringBuilder.toString();
        }
        et_faktur.setText(string2);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertDataLaundryDetail(final TransaksiLaundryDetail transaksiLaundryDetail){
        new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... voids){
                return database.laundryDetailDAO().insertlaundryDetail(transaksiLaundryDetail);
            }
            @Override
            protected void onPostExecute(Long status){
                Toast.makeText(TransaksiTerimaActivity.this,"Memasukkan data..."+status,Toast.LENGTH_SHORT);
            }
        }.execute();
    }

    private void tambahKeranjang() {
        TransaksiLaundry transaksiLaundry = new TransaksiLaundry();
        TransaksiLaundryDetail transaksiLaundryDetail = new TransaksiLaundryDetail();
        String string2 = et_faktur.getText().toString();
        String string3 = et_tglterima.getText().toString();
        String string4 = et_tglselesai.getText().toString();
        String string5 = et_nama.getText().toString();
        String string6 = et_jasa.getText().toString();
        String string7 = et_harga.getText().toString();
        String string8 = et_jumlah.getText().toString();
        String string9 = et_ket.getText().toString().replace("\n", " ");
        if (!(TextUtils.isEmpty(string2) ||
                TextUtils.isEmpty(string3) ||
                TextUtils.isEmpty(string4) ||
                TextUtils.isEmpty(string5) ||
                TextUtils.isEmpty(string6) ||
                TextUtils.isEmpty(string7) ||
                TextUtils.isEmpty(string8))) {
            Cursor cursor=database.laundryDAO().bacafakturlaundry(string2);
            int total=Integer.parseInt(string7) * Integer.parseInt(string8);
            if(cursor.getCount()==0){
                transaksiLaundryDetail.setIdjasa(getidjasa);
                transaksiLaundryDetail.setIdlaundry(Integer.parseInt(string2));
                transaksiLaundryDetail.setJumlahlaundry(Integer.parseInt(string8));
                transaksiLaundryDetail.setBiayalaundry(total);
                transaksiLaundryDetail.setKeterangan(string9);
                database.laundryDAO().masukLaundry(getidpelanggan,string2,convertDate(string3),convertDate(string4));
                insertDataLaundryDetail(transaksiLaundryDetail);
            }else{
                database.laundryDAO().updatetabellaundry(getidpelanggan,convertDate(string3),convertDate(string4),Integer.parseInt(string2));
                transaksiLaundryDetail.setIdjasa(getidjasa);
                transaksiLaundryDetail.setIdlaundry(Integer.parseInt(string2));
                transaksiLaundryDetail.setJumlahlaundry(Integer.parseInt(string8));
                transaksiLaundryDetail.setBiayalaundry(total);
                transaksiLaundryDetail.setKeterangan(string9);
                insertDataLaundryDetail(transaksiLaundryDetail);
            }
            Toast.makeText(TransaksiTerimaActivity.this, "Data berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            getTotal();
            loadKeranjang();
            cleartext();
            return;
        }else{
            Toast.makeText(TransaksiTerimaActivity.this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadKeranjang(){
        String string2=et_faktur.getText().toString();
        daftarcart=new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_keranjang);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        daftarcart.addAll(Arrays.asList(database.cartDAO().bacaCart(string2)));
        keranjangAdapter=new TransaksiKeranjangAdapter(daftarcart,this);
        recyclerView.setAdapter(keranjangAdapter);
    }

    public void getTotal(){
        int n=Integer.parseInt(et_faktur.getText().toString());
        Cursor cursor=database.laundryDetailDAO().sumTotal(n);
        if(cursor.moveToFirst()){
            int d=cursor.getInt(0);
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Rp.");
            stringBuilder.append(d);
            tv_total.setText(stringBuilder.toString());
        }else {
            int d = -1;
        }
        cursor.close();
    }

    private void cleartext(){
        et_jasa.setText("");
        et_harga.setText("");
        et_jumlah.setText("");
        et_ket.setText("");
        setJumlah(0,"");
    }

    private void keluar(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Anda yakin ingin keluar?");
        builder.setMessage("Setelah keluar data faktur ini akan hilang");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int a=Integer.parseInt(et_faktur.getText().toString());
                database.laundryDAO().deleteidlaundry(a);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }


    private void berhasil(){
        final Intent intent=new Intent(TransaksiTerimaActivity.this,TransaksiCetakActivity.class);
        final String string2=et_faktur.getText().toString();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Simpan Data Berhasil");
        builder.setPositiveButton("Cetak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                intent.putExtra("faktur",string2);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setCancelable(false).show();
    }
    public String convertDate(String string2) {
        String[] arrstring = string2.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(arrstring[2]);
        stringBuilder.append(arrstring[1]);
        stringBuilder.append(arrstring[0]);
        return stringBuilder.toString();
    }
}
