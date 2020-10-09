package com.goblo.skripshit.transaksi.terima;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.goblo.skripshit.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TerimaActivity extends AppCompatActivity {
    private ImageButton caripelanggan,carijasa,btn_tglterima,btn_tglselesai,btn_tambah,btn_kurang;
    private TextInputEditText txt_nama,txt_jasa,txt_harga,txt_jumlah,txt_tglterima,txt_tglselesai;
    private String pelanggan,namajasa,satuan;
    private String hargajasa="";
    private Calendar mycalender;
    private DatePickerDialog.OnDateSetListener tglterima,tglselesai;
    private Button btn_keranjang;
    private TextView tx_totalbayar,tv_satuan;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2000){
            pelanggan=data.getStringExtra("nama");
            return;
        }
        if(resultCode==3000){
            namajasa=data.getStringExtra("jasa");
            hargajasa=data.getStringExtra("harga");
            satuan=data.getStringExtra("satuan");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_terima);
        mycalender=Calendar.getInstance();
        tglterima=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mycalender.set(Calendar.YEAR, i);
                mycalender.set(Calendar.MONTH,i1);
                mycalender.set(Calendar.DAY_OF_MONTH,i2);
                txt_tglterima=findViewById(R.id.txt_input