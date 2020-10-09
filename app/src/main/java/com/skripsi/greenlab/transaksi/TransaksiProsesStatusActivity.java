package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.util.ArrayList;
import java.util.Arrays;

public class TransaksiProsesStatusActivity extends AppCompatActivity {
    private Button btn_bayar;
    private AppDatabase database;
    private String faktur;
    private TextView tv_total;
    private TextInputEditText et_faktur,et_tglterima,et_tglselesai,et_nama,et_alamat,et_telp;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<vCart> daftarcart;
    private TransaksiProsesStatusAdapter transaksiProsesStatusAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_proses_status);
        context=getApplicationContext();
        database =AppDatabase.getInstance(context);
        faktur=getIntent().getStringExtra("faktur");
        et_faktur=findViewById(R.id.txt_input_faktur);
        et_faktur.setText(faktur);
        et_tglterima=findViewById(R.id.txt_input_tglterima);
        et_tglselesai=findViewById(R.id.txt_input_tglselesai);
        et_nama=findViewById(R.id.txt_input_pelanggan);
        et_alamat=findViewById(R.id.txt_input_alamat);
        et_telp=findViewById(R.id.txt_input_telp);
        tv_total=findViewById(R.id.totalbayar);
        btn_bayar=findViewById(R.id.btn_bayar);
        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TransaksiDialogProsesBayar(TransaksiProsesStatusActivity.this,faktur);
            }
        });
        loadKeranjangProses();
        setTextProses();
    }

    private void loadKeranjangProses(){
        String string2=et_faktur.getText().toString();
        daftarcart=new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_keranjang);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        daftarcart.addAll(Arrays.asList(database.cartDAO().bacaCart(string2)));
        transaksiProsesStatusAdapter=new TransaksiProsesStatusAdapter(daftarcart,this);
        recyclerView.setAdapter(transaksiProsesStatusAdapter);
    }

    private void setTextProses(){
        Cursor cursor=database.vLaundryDAO().bacaLaundrybyFaktur(faktur);
        cursor.moveToNext();
        String tglterima=cursor.getString(cursor.getColumnIndex("tglterima"));
        et_tglterima.setText(tglterima);
        String tglselesai=cursor.getString(cursor.getColumnIndex("tglselesai"));
        et_tglselesai.setText(tglselesai);
        String nama=cursor.getString(cursor.getColumnIndex("nama_pelanggan"));
        et_nama.setText(nama);
        int total=cursor.getInt(cursor.getColumnIndex("total"));
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Rp. ");
        stringBuilder.append(total);
        tv_total.setText(stringBuilder.toString());
        Cursor cursor1=database.prosesDAO().bacaProsesbyFaktur(faktur);
        cursor1.moveToNext();
        String alamat=cursor1.getString(cursor1.getColumnIndex("alamat_pelanggan"));
        et_alamat.setText(alamat);
        String telp=cursor1.getString(cursor1.getColumnIndex("telp_pelanggan"));
        et_telp.setText(telp);
    }
}
