package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.Modul;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.TransaksiActivity;
import com.skripsi.greenlab.master.MasterPelanggan;

import java.util.List;

public class TransaksiHutangBayarActivity extends AppCompatActivity {
    private Context context;
    private AppDatabase database;
    private TextView tv_nama,tv_telp,tv_alamat,tv_total;
    private int hutang=0,kembalian=0;
    private TextInputEditText et_bayar,et_kembali;
    private Button btn_bayarhutang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_hutang_bayar);
        tv_nama=findViewById(R.id.tv_namahutang);
        tv_telp=findViewById(R.id.tv_telphutang);
        tv_alamat=findViewById(R.id.tv_alamathutang);
        tv_total=findViewById(R.id.tv_totalhutang);
        context=getApplicationContext();
        et_bayar=findViewById(R.id.et_jumlahbayar);
        et_kembali=findViewById(R.id.et_kembali);
        final long idpelanggan=getIntent().getLongExtra("idpelanggan",0);
        database =AppDatabase.getInstance(context);
        List<MasterPelanggan> datapelanggan=database.pelangganDAO().bacaDataPelangganbyId(idpelanggan);
        for(MasterPelanggan pelang: datapelanggan){
            String nama=pelang.getNama();
            tv_nama.setText(nama);
            String telp=pelang.getTelp();
            tv_telp.setText(telp);
            String alamat=pelang.getAlamat();
            tv_alamat.setText(alamat);
            hutang=pelang.getHutang();
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Rp.");
            stringBuilder.append(hutang);
            tv_total.setText(stringBuilder.toString());
        }
        et_bayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")) {
                    int bayar = Integer.parseInt(editable.toString());
                    kembalian = bayar - hutang;
                    et_kembali.setText(String.valueOf(kembalian));
                }else{
                    et_bayar.setText("0");
                    et_kembali.setText("0");
                    Toast.makeText(context,"Jumlah Pembayaran tidak boleh 0",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_bayarhutang=findViewById(R.id.btn_bayarhutang);
        btn_bayarhutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_kembali.getText().toString().equals("")){
                    TransaksiHutang transaksiHutang=new TransaksiHutang();
                    transaksiHutang.setIdpelanggan(idpelanggan);
                    transaksiHutang.setTglbayar(Modul.getDate("yyyyMMdd"));
                    transaksiHutang.setHutang(hutang);
                    transaksiHutang.setBayar(Integer.parseInt(et_bayar.getText().toString()));
                    transaksiHutang.setBayarhutang(Integer.parseInt(et_bayar.getText().toString()));
                    transaksiHutang.setKembali(Integer.parseInt(et_kembali.getText().toString()));
                    database.transaksiHutangDAO().insertHutang(transaksiHutang);
                    Toast.makeText(context,"Berhasil",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TransaksiHutangBayarActivity.this, TransaksiActivity.class));
                }else{
                    Toast.makeText(context,"Isi nominal pembayaran",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
