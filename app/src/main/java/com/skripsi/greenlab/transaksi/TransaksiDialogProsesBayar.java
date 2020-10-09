package com.skripsi.greenlab.transaksi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.Modul;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.TransaksiActivity;

public class TransaksiDialogProsesBayar {
    private Context context;
    private View dialog;
    private AppDatabase database;
    private TextInputEditText et_faktur,et_total,et_kembali,et_bayar;
    private Spinner spinner;
    private ImageButton btn_close;
    private Button btn_bayar;
    private int kembalian=0;

    public TransaksiDialogProsesBayar(final Context context, String string2){
        this.context=context;
        dialog= LayoutInflater.from(context).inflate(R.layout.popup_transaksi_bayar,null);
        et_faktur=dialog.findViewById(R.id.et_proses_faktur);
        et_total=dialog.findViewById(R.id.et_proses_total);
        et_bayar=dialog.findViewById(R.id.et_proses_bayar);
        et_kembali=dialog.findViewById(R.id.et_proses_kembali);
        btn_close=dialog.findViewById(R.id.btn_close);
        btn_bayar=dialog.findViewById(R.id.btn_proses_bayar);
        database =AppDatabase.getInstance(context);
        Cursor cursor=database.laundryDAO().bacalaundrywhere(string2);
        cursor.moveToNext();
        et_faktur.setText(string2);
        et_total.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("total"))));
        spinner=dialog.findViewById(R.id.spinner_bayar);
        final String[] arrstring = new String[1];
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arrstring[0]=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                    int total = Integer.parseInt(et_total.getText().toString());
                    kembalian = bayar - total;
                    et_kembali.setText(String.valueOf(kembalian));
                }else{
                    et_bayar.setText("0");
                    et_kembali.setText("0");
                    Toast.makeText(context,"Jumlah Pembayaran tidak boleh 0",Toast.LENGTH_SHORT).show();
                }
            }
        });
        final AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.customdialog).setView(dialog).setCancelable(false).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogInterface.OnShowListener onShowListener=new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_bayar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String string2=et_faktur.getText().toString();
                        final String string3=arrstring[0];
                        int string4=Integer.parseInt(et_total.getText().toString());
                        int bayar=Integer.parseInt(et_bayar.getText().toString());
                        final int kembali=Integer.parseInt(et_kembali.getText().toString());
                        String string7= Modul.getDate("yyyyMMdd");
                        if (string3.equals((Object)"Tunai") && string4 > bayar) {
                            Toast.makeText(context,"Uang bayar tidak cukup!", (Toast.LENGTH_SHORT)).show();
                            return;
                        }
                        if (string3.equals((Object)"Hutang") && kembali > 0) {
                            Toast.makeText(context, "Uang cukup untuk pembayaran tunai", (Toast.LENGTH_SHORT)).show();
                            return;
                        }
                        database.laundryDAO().updatebayarlaundry(bayar,kembali,string7,"Selesai",string3,string2);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.create();
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Konfirmasi pesanan laundry ");
                        stringBuilder2.append(string2);
                        stringBuilder2.append(" selesai?");
                        builder.setMessage(stringBuilder2.toString());
                        DialogInterface.OnClickListener onClickListener=new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (string3.equals((Object)"Hutang")) {
                                    Cursor cursor=database.laundryDAO().bacalaundrywhere(string2);
                                    cursor.moveToNext();
                                    int idpelanggan=cursor.getInt(cursor.getColumnIndex("idpelanggan"));
                                    int kembalian=Integer.parseInt(Modul.removeMinus(String.valueOf(kembali)));
                                    database.pelangganDAO().updateHutangPelanggan(kembalian,idpelanggan);
                                }
                                Toast.makeText(context,"Berhasil",Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                new AlertDialog.Builder(context).setMessage("Simpan data berhasil").setPositiveButton("Cetak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(context,TransaksiCetakActivity.class);
                                        intent.putExtra("faktur",string2);
                                        context.startActivity(intent);
                                    }
                                }).setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        context.startActivity(new Intent(context,TransaksiActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                }).setCancelable(false).create().show();
                                return;
                                }
                        };
                        builder.setPositiveButton("Ya",onClickListener);
                        builder.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context,"Lakukan pembayaran jika proses laundry udah sudah selesai",Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });
            }
        };
        alertDialog.setOnShowListener(onShowListener);
        alertDialog.show();
    }
}
