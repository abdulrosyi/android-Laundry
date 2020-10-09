package com.skripsi.greenlab.transaksi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.Modul;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.TransaksiActivity;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class TransaksiKebutuhanActivity extends AppCompatActivity {
    private TextInputEditText et_faktur,et_tgl,et_namakebutuhan,et_ket,et_jmlpengeluaran;
    private ImageButton btn_tgl;
    private Button btn_simpan;
    private Calendar calendar;
    private String tgl;
    private int year,day,month;
    private AppDatabase database;
    private Context context;
    private String tempFaktur="KEBUTUHAN-";
    private String faktur="";
    private DatePickerDialog.OnDateSetListener dTanggal=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            int i3 = i1 + 1;
            tgl = Modul.convertDate(Modul.setDatePickerNormal(i, i3, i2));
            et_tgl.setText(Modul.setDatePickerNormal(i,i3,i2));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        database=AppDatabase.getInstance(context);
        calendar=Calendar.getInstance();
        year=calendar.get(1);
        month=calendar.get(2);
        day=calendar.get(5);
        tgl=Modul.getDate("yyyyMMdd");
        setContentView(R.layout.activity_transaksi_pengeluaran);
        et_tgl=findViewById(R.id.et_tgl_pengeluaran);
        et_tgl.setText(Modul.dateToNormal(tgl));
        et_faktur=findViewById(R.id.et_faktur_pengeluaran);
        cekFaktur();
        et_faktur.setText(faktur);
        et_namakebutuhan=findViewById(R.id.et_nama_pengeluaran);
        et_jmlpengeluaran=findViewById(R.id.et_jml_pengeluaran);
        editToRp(et_jmlpengeluaran);
        et_ket=findViewById(R.id.et_ket_pengeluaran);
        btn_tgl=findViewById(R.id.btn_tgl_pengeluaran);
        btn_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(0);
            }
        });
        btn_simpan=findViewById(R.id.btn_simpan_pengeluaran);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanKebutuhan();
            }
        });
    }
    protected Dialog onCreateDialog(int n) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(TransaksiKebutuhanActivity.this, dTanggal, year, month, day);
        return datePickerDialog;
    }

    public void setDate(int n) {
        this.showDialog(n);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void cekFaktur(){
        Cursor cursor=database.kebutuhanDAO().cekFaktur(tempFaktur);
        if (Modul.getCount(cursor) > 0) {
            if (Modul.getCount(cursor) < 10) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(tempFaktur);
                stringBuilder2.append("0");
                stringBuilder2.append(1 + Modul.getCount(cursor));
                faktur = stringBuilder2.toString();
            } else {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(tempFaktur);
                stringBuilder3.append(1 + Modul.getCount(cursor));
                faktur = stringBuilder3.toString();
            }
        } else {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(tempFaktur);
            stringBuilder4.append("01");
            faktur = stringBuilder4.toString();
        }
    }

    private void simpanKebutuhan(){
        final TransaksiKebutuhan transaksiKebutuhan=new TransaksiKebutuhan();
        String string2=et_faktur.getText().toString();
        String string3=et_tgl.getText().toString();
        String string4=et_namakebutuhan.getText().toString();
        String string5=et_ket.getText().toString();
        String string6=et_jmlpengeluaran.getText().toString().trim().replace(".","");;
        if(!(TextUtils.isEmpty(string2)
                || TextUtils.isEmpty(string3)
                || TextUtils.isEmpty(string4)
                || TextUtils.isEmpty(string6))){
            transaksiKebutuhan.setFaktur(string2);
            transaksiKebutuhan.setTglkebutuhan(convertDate(string3));
            transaksiKebutuhan.setNamakebutuhan(string4);
            transaksiKebutuhan.setJumlah(Integer.parseInt(string6));
            transaksiKebutuhan.setKeterangan(string5);
            new AlertDialog.Builder(TransaksiKebutuhanActivity.this).setTitle("Simpan Transaksi")
                    .setMessage("Konfirmasi data yang dimasukkan sudah benar?").setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.kebutuhanDAO().inputPengeluaran(transaksiKebutuhan);
                    Toast.makeText(TransaksiKebutuhanActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TransaksiKebutuhanActivity.this, TransaksiActivity.class));
                }
            }).setNegativeButton("Tidak",null).create().show();
        }else{
            Toast.makeText(TransaksiKebutuhanActivity.this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }

    public String convertDate(String string2) {
        String[] arrstring = string2.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(arrstring[2]);
        stringBuilder.append(arrstring[1]);
        stringBuilder.append(arrstring[0]);
        return stringBuilder.toString();
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
