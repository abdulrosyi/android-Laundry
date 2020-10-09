package com.skripsi.greenlab.transaksi;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.room.Room;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.skripsi.greenlab.AppDatabase;
import com.skripsi.greenlab.FileUtils;
import com.skripsi.greenlab.Modul;
import com.skripsi.greenlab.PermissionChecker;
import com.skripsi.greenlab.PermissionsActivity;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.laporan.LaporanPelangganList;
import com.skripsi.greenlab.master.MasterPelanggan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.skripsi.greenlab.LogUtils.LOGE;
import static com.skripsi.greenlab.PermissionChecker.REQUIRED_PERMISSION;
import static com.skripsi.greenlab.PermissionsActivity.PERMISSION_REQUEST_CODE;


public class TransaksiCetakActivity extends AppCompatActivity {
    private PermissionChecker checker;
    private ConstraintLayout struk;
    private Button btn_cetak,btn_save;
    private TextView tv_faktur,tv_tglterima,tv_tglselesai,tv_nama,tv_status,tv_totalharga,tv_bayar,tv_kembali,tv_pembayaran,tv_daftarjasa;
    private String faktur;
    private AppDatabase database;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_cetak);
        context=getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cetak");
        database =AppDatabase.getInstance(context);
        faktur=getIntent().getStringExtra("faktur");
        tv_faktur=findViewById(R.id.tv_faktur);
        tv_tglterima=findViewById(R.id.tv_tgl_terima);
        tv_tglselesai=findViewById(R.id.tv_tgl_selesai);
        tv_nama=findViewById(R.id.tv_nama);
        tv_status=findViewById(R.id.tv_status);
        tv_totalharga=findViewById(R.id.tv_totalharga);
        tv_bayar=findViewById(R.id.tv_bayar);
        tv_kembali=findViewById(R.id.tv_kembali);
        tv_pembayaran=findViewById(R.id.tv_pembayaran);
        tv_daftarjasa=findViewById(R.id.tv_daftarjasa);
        struk=findViewById(R.id.struk);
        cetakStruk();
        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker=new PermissionChecker(context);
                if(checker.lacksPermissions(REQUIRED_PERMISSION)){
                    PermissionsActivity.startActivityForResult(TransaksiCetakActivity.this,PERMISSION_REQUEST_CODE,REQUIRED_PERMISSION);
                }else {
                    ConstraintLayout constraintLayout;
                    struk=constraintLayout=findViewById(R.id.struk);
                    cetakStruk();
                    saveGambar(FileUtils.getAppPath(context)+"Struk-"+faktur+".jpg",TransaksiCetakActivity.this,constraintLayout);
                }
            }
        });
        btn_cetak=findViewById(R.id.btn_cetak);
        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker=new PermissionChecker(context);
                if(checker.lacksPermissions(REQUIRED_PERMISSION)){
                    PermissionsActivity.startActivityForResult(TransaksiCetakActivity.this,PERMISSION_REQUEST_CODE,REQUIRED_PERMISSION);
                    new Loading(TransaksiCetakActivity.this).execute();
                }else {
                    new Loading(TransaksiCetakActivity.this).execute();
                }
            }
        });
    }

    private class Loading extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        private Context context;
        Loading(Context context){
            this.context=context;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog= ProgressDialog.show(context,"Mohon Tunggu...", "",true,false);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            createPdf(FileUtils.getAppPath(context)+"Struk-"+faktur+".pdf");
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);
            progressDialog.dismiss();
            watermarkPdf(FileUtils.getAppPath(context)+"Struk-"+faktur+".pdf",FileUtils.getAppPath(context)+"Struk-"+faktur+"-Watermak.pdf");
        }
    }

    private void cetakStruk(){
        String string2;
        Cursor cursor=database.vLaundryDAO().bacaLaundrybyFaktur(faktur);
        cursor.moveToNext();
        tv_faktur.setText("Faktur : "+faktur);
        String tglterima=cursor.getString(cursor.getColumnIndex("tglterima"));
        tv_tglterima.setText("Tanggal Terima : "+tglterima);
        String tglselesai=cursor.getString(cursor.getColumnIndex("tglselesai"));
        tv_tglselesai.setText("Tanggal Selesai : "+tglselesai);
        String nama=cursor.getString(cursor.getColumnIndex("nama_pelanggan"));
        tv_nama.setText("Pelanggan : "+nama);
        String status=cursor.getString(cursor.getColumnIndex("statuslaundry"));
        tv_status.setText("Status : " + status);
        int total = cursor.getInt(cursor.getColumnIndex("total"));
        tv_totalharga.setText("Total Harga : " + total);
        if(status.equals("Proses")){
            tv_pembayaran.setVisibility(View.INVISIBLE);
            tv_bayar.setVisibility(View.INVISIBLE);
            tv_kembali.setVisibility(View.INVISIBLE);
        }else {
            int bayar = cursor.getInt(cursor.getColumnIndex("bayar"));
            tv_bayar.setText("Bayar : "+bayar);
            int kembali = cursor.getInt(cursor.getColumnIndex("kembali"));
            tv_kembali.setText("Kembali : "+kembali);
            String pembayaran= cursor.getString(cursor.getColumnIndex("statusbayar"));
            tv_pembayaran.setText("Pembayaran : "+pembayaran);
        }
        String string3=string2="";
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()){
            String namajasa=cursor.getString(cursor.getColumnIndex("namajasa"));
            int harga=cursor.getInt(cursor.getColumnIndex("hargajasa"));
            int jumlah=cursor.getInt(cursor.getColumnIndex("jumlahlaundry"));
            //int totalharga=cursor.getInt(cursor.getColumnIndex("biayalaundry"));
            int totalharga=jumlah*harga;
            String ket=cursor.getString(cursor.getColumnIndex("keterangan"));
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(string3);
            stringBuilder.append(namajasa);
            stringBuilder.append("\n");
            stringBuilder.append(jumlah);
            stringBuilder.append(" X ");
            stringBuilder.append(harga);
            stringBuilder.append("\n");
            if(!ket.equals("")) {
                stringBuilder.append("(Ket : ");
                stringBuilder.append(ket);
                stringBuilder.append(" )");
            }
            stringBuilder.append("\n");
            stringBuilder.append(Modul.setRight(String.valueOf(totalharga)));
            stringBuilder.append("\n");
            String string4=stringBuilder.toString();
            StringBuilder stringBuilder1=new StringBuilder();
            stringBuilder1.append(string2);
            stringBuilder1.append(namajasa);
            stringBuilder1.append("\n");
            stringBuilder1.append(jumlah);
            stringBuilder1.append(" X ");
            stringBuilder1.append(harga);
            stringBuilder1.append("\n");
            if(!ket.equals("")) {
                stringBuilder1.append("(Ket : ");
                stringBuilder1.append(ket);
                stringBuilder1.append(" )");
            }
            stringBuilder1.append("\n");
            stringBuilder1.append(setRight(String.valueOf(totalharga)));
            stringBuilder1.append("\n");
            string2=stringBuilder1.toString();
            string3=string4;
        }
        tv_daftarjasa.setText(string2);
    }

    private void saveGambar(String desc,Context context,View v){
        if(new File(desc).exists()){
            new File(desc).delete();
        }
        Bitmap bitmap=TransaksiCetakActivity.getBitmapFromView(v);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap,0.0f,0.0f,null);
        Bitmap bitmap2 = TransaksiCetakActivity.getBitmapFromView(v);
        try {
            new File(desc).createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(desc));
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            FileUtils.openFile(context, new File(desc));
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            Log.i("TAG", "Eror simpan file gambar");
        }
        scanGaler(TransaksiCetakActivity.this,desc);

    }

    private void scanGaler(Context context,String desc){
        try{
            MediaScannerConnection.scanFile(context, new String[]{desc}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {
                }
            });
            return;
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
    public void watermarkPdf(String src,String desc){
        if(new File(desc).exists()){
            new File(desc).delete();
        }
        try {
            PdfReader reader = new PdfReader(src);
            int halaman=reader.getNumberOfPages();
            PdfStamper stamper=new PdfStamper(reader, new FileOutputStream(desc));
            int i=0;
            PdfContentByte content;
            PdfGState gs;
            while(i<halaman){
                i++;
                content=stamper.getUnderContent(i);
                gs=new PdfGState();
                gs.setFillOpacity(0.3f);
                gs.setStrokeOpacity(0.3f);
                Rectangle halamansize=reader.getPageSize(i);
                int rotasihalaman=reader.getPageRotation(i);
                float x=(halamansize.getLeft() + halamansize.getRight()) / 2;
                float y=(halamansize.getTop() + halamansize.getBottom()) / 2;
                if(rotasihalaman !=0){
                    x=(halamansize.getHeight())/2;
                    y=(halamansize.getWidth())/2;
                    y=y-80;
                }
                Bitmap bitmap=getBitmapFromVectorDrawable(context,R.drawable.logo_fix);
                ByteArrayOutputStream stream= new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                Image img=Image.getInstance(stream.toByteArray());
                float lebar=img.getWidth();
                float tinggi=img.getHeight();
                float pw=halamansize.getWidth();
                float ph=halamansize.getHeight();
                float scaleMulti=(pw/lebar)*0.5f;
                float lebar_gambar=(lebar*(scaleMulti));
                float tinggi_gambar=(tinggi*(scaleMulti));
                float x_koordinat=x-(lebar_gambar/2);
                float y_koordinat=y-(tinggi_gambar/2);
                content.saveState();
                content.setGState(gs);
                content.addImage(img,lebar_gambar,0,0,tinggi_gambar,x_koordinat,y_koordinat);
                content.restoreState();
            }
            FileUtils.openFile(context, new File(desc));
            stamper.close();
            reader.close();
        }catch (IOException | DocumentException ie) {
            LOGE("bacaPdf: Error" + ie.getLocalizedMessage());
        }
    }

    public void createPdf(String desc){
        if(new File(desc).exists()){
            new File(desc).delete();
        }
        try{
            Document doc=new Document();
            PdfWriter writer=PdfWriter.getInstance(doc,new FileOutputStream(desc));
            doc.open();
            doc.setPageSize(PageSize.A4);
            doc.addCreationDate();
            doc.addAuthor("GREEN LAB");
            doc.addCreator("Faktur");
            isiLaporan(doc);
            doc.close();
        }catch (IOException | DocumentException ie){
            LOGE("createPdf: Error"+ie.getLocalizedMessage());
        }catch (ActivityNotFoundException ae){
            Toast.makeText(context,"Tidak ada aplikasi yang support untuk membuka file ini",Toast.LENGTH_SHORT).show();
        }
    }

    private void isiLaporan(Document doc) throws IOException,DocumentException{
        Paragraph p=new Paragraph("GREEN LAB", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
        Paragraph p1=new Paragraph("Jalan Pertengahan RT.10/ RW 03 No.6, Cijantung, Kec. Ps.Rebo, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13770.", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(p1);
        addEmptyLine(p, 1);
        doc.add(p);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        Cursor cursor=database.vLaundryDAO().bacaLaundrybyFaktur(faktur);
        cursor.moveToNext();
        String tglterima=cursor.getString(cursor.getColumnIndex("tglterima"));
        String tglselesai=cursor.getString(cursor.getColumnIndex("tglselesai"));
        String nama=cursor.getString(cursor.getColumnIndex("nama_pelanggan"));
        String status=cursor.getString(cursor.getColumnIndex("statuslaundry"));
        int totalbayar = cursor.getInt(cursor.getColumnIndex("total"));
        int bayar = cursor.getInt(cursor.getColumnIndex("bayar"));
        int kembali = cursor.getInt(cursor.getColumnIndex("kembali"));
        String pembayaran= cursor.getString(cursor.getColumnIndex("statusbayar"));
        Paragraph pp=new Paragraph();
        Paragraph p2=new Paragraph("Faktur : "+faktur, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph p3=new Paragraph("Tanggal Terima : "+tglterima,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph p4=new Paragraph("Tanggal Selesai : "+tglselesai,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph p5=new Paragraph("Pelanggan : "+nama,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph p6=new Paragraph("Status : "+status,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph p7=new Paragraph("Pembayaran : "+pembayaran,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        pp.add(p2);
        pp.add(p3);
        pp.add(p4);
        pp.add(p5);
        pp.add(p6);
        pp.add(p7);
        pp.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(pp,1);
        doc.add(pp);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        Paragraph ppp=new Paragraph();
        ppp.setAlignment(Element.ALIGN_LEFT);
        List<vLaundry> dataLaundry=database.vLaundryDAO().bacadataLaundry(faktur);
        for(vLaundry vLaundry: dataLaundry){
            String namajasa=vLaundry.getNamajasa();
            Paragraph pjasa=new Paragraph(namajasa,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            ppp.add(pjasa);
            int harga=vLaundry.getHargajasa();
            int jumlah=vLaundry.getJumlahlaundry();
            Paragraph pjumlah=new Paragraph(jumlah+" X "+harga,new Font(Font.FontFamily.TIMES_ROMAN, 12,  Font.BOLD));
            ppp.add(pjumlah);
            String ket=vLaundry.getKeterangan();
            Paragraph pket=new Paragraph(ket,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
            ppp.add(pket);
            int totalharga=jumlah*harga;
            Paragraph total=new Paragraph(String.valueOf(totalharga),new Font(Font.FontFamily.TIMES_ROMAN, 12,  Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            ppp.add(total);
        }
        doc.add(ppp);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        Paragraph pppp=new Paragraph();
        Paragraph ptotalbayar=new Paragraph("Total Harga : "+totalbayar,new Font(Font.FontFamily.TIMES_ROMAN, 12,  Font.BOLD));
        Paragraph pbayar=new Paragraph("Bayar : "+bayar,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph pkembali=new Paragraph("Kembali : "+kembali,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        pppp.add(ptotalbayar);
        pppp.add(pbayar);
        pppp.add(pkembali);
        addEmptyLine(pppp,4);
        pppp.setAlignment(Element.ALIGN_RIGHT);
        doc.add(pppp);
        Paragraph ppppp=new Paragraph("TERIMAKASIH SUDAH MENGGUNAKAN LAYANAN JASA KAMI", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
        ppppp.setAlignment(Element.ALIGN_CENTER);
        doc.add(ppppp);
    }

    private static void addEmptyLine(Paragraph paragraph, int number){
        for (int i=0; i<number; i++){
            paragraph.add(new Paragraph(""));
        }
    }
    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId){
        Drawable drawble1= ContextCompat.getDrawable(context,drawableId);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            drawble1=(DrawableCompat.wrap(drawble1)).mutate();
        }
        Bitmap bitmap=Bitmap.createBitmap(drawble1.getIntrinsicWidth(),drawble1.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(bitmap);
        drawble1.setBounds(0,0, canvas.getWidth(),canvas.getHeight());
        drawble1.draw(canvas);
        return bitmap;
    }
    public static Bitmap getBitmapFromView(View view) {
        view.measure(0, 0);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }
    public static String setRight(String string2) {
        int n = string2.length();
        String string3 = "";
        for (int i = 0; i < 32 - n; ++i) {
            if (31 - n == i) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string3);
                stringBuilder.append(string2);
                string3 = stringBuilder.toString();
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string3);
            stringBuilder.append(" ");
            string3 = stringBuilder.toString();
        }
        return string3;
    }
}
