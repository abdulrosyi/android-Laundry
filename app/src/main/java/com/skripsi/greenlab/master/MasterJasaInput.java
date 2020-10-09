package com.skripsi.greenlab.master;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MasterJasaInput extends AppCompatActivity {
    private Spinner spinnersatuan;
    private AppDatabase database;
    private String txtsatuan="";
    private TextInputEditText et_kategori,et_nama,et_harga;
    private Button btn_simpan;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_input_jasa);
        context=getApplicationContext();
        database=AppDatabase.getInstance(context);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Input Jasa");
        spinnersatuan=findViewById(R.id.spinner_satuan_jasa);
        String[] satuan={"Piece (Pc)","Kilogram (Kg)","MeterPersegi (M\u00B2)"};
        ArrayList<String> daftarsatuan= new ArrayList<>(Arrays.asList(satuan));
        ArrayAdapter<String> adaptersatuan=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,daftarsatuan);
        adaptersatuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersatuan.setAdapter(adaptersatuan);
        spinnersatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtsatuan=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        et_kategori=findViewById(R.id.et_kategori_jasa);
        et_nama=findViewById(R.id.et_nama_jasa);
        et_harga=findViewById(R.id.et_harga_jasa);
        editToRp(et_harga);
        btn_simpan=findViewById(R.id.btn_simpan_jasa);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_kategori.getText().toString().isEmpty() || et_nama.getText().toString().isEmpty() || et_harga.getText().toString().isEmpty()){
                    Toast.makeText(MasterJasaInput.this, "Masukkan data dengan benar.", Toast.LENGTH_SHORT).show();
                }else{
                    MasterJasa data=new MasterJasa();
                    data.setKategori(et_kategori.getText().toString().trim());
                    data.setNamajasa(et_nama.getText().toString().trim());
                    String harga=et_harga.getText().toString().trim().replace(".","");
                    int harga_jasa=Integer.parseInt(harga);
                    data.setHargajasa(harga_jasa);
                    data.setSatuanjasa(txtsatuan);
                    insertData(data);
                    Toast.makeText(MasterJasaInput.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MasterJasaInput.this, MasterJasaList.class);
                    startActivity(intent);
                    et_kategori.setText("");
                    et_nama.setText("");
                    et_harga.setText("");
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void insertData(final MasterJasa jasa){
        new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... voids){
                return database.jasaDAO().insertJasa(jasa);
            }
            @Override
            protected void onPostExecute(Long status){
            }
        }.execute();
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
                    String replaceble=String.format("[Rp..\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(locale));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
