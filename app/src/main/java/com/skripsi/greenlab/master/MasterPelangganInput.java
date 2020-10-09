package com.skripsi.greenlab.master;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

public class MasterPelangganInput extends AppCompatActivity {
    private TextInputEditText txt_nama,txt_alamat,txt_telp;
    private Button btn_simpan;
    private AppDatabase database;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_input_pelanggan);
        context=getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Input Pelanggan");
        txt_nama=findViewById(R.id.txt_input_nama);
        txt_alamat=findViewById(R.id.txt_input_alamat);
        txt_telp=findViewById(R.id.txt_input_telp);
        btn_simpan=findViewById(R.id.btn_tambah);
        database=AppDatabase.getInstance(context);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_nama.getText().toString().isEmpty()||txt_alamat.getText().toString().isEmpty()||txt_telp.getText().toString().isEmpty()){
                    Toast.makeText(MasterPelangganInput.this, "Nama, alamat atau telepon tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    MasterPelanggan data=new MasterPelanggan();
                    data.setNama(txt_nama.getText().toString());
                    data.setAlamat(txt_alamat.getText().toString());
                    data.setTelp(txt_telp.getText().toString());
                    insertData(data);
                    Toast.makeText(MasterPelangganInput.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MasterPelangganInput.this, MasterPelangganList.class);
                    startActivity(intent);
                    txt_nama.setText("");
                    txt_alamat.setText("");
                    txt_telp.setText("");
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void insertData(final MasterPelanggan pelanggan){
        new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... voids){
                return database.pelangganDAO().insertPelanggan(pelanggan);
            }
            @Override
            protected void onPostExecute(Long status){
                Toast.makeText(MasterPelangganInput.this,"Status Row"+status,Toast.LENGTH_SHORT);
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
