package com.skripsi.greenlab.laporan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.skripsi.greenlab.LogUtils.LOGE;
import static com.skripsi.greenlab.PermissionChecker.REQUIRED_PERMISSION;
import static com.skripsi.greenlab.PermissionsActivity.PERMISSION_REQUEST_CODE;

public class LaporanTransaksiList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LaporanAdapterTransaksiList transaksiadapter;
    private AppDatabase database;
    private ArrayList<String> daftarlaundry;
    private SearchView txt_cari;
    private Toolbar tulbar;
    private TextView tv_jmlpesanan,tv_jmlpendapatan;
    private Button btn_cetak,btn_tgldari,btn_tglsampai;
    private PermissionChecker checker;
    private Context context;
    private Calendar kalender;
    private MediaPlayer mediaPlayer;
    private String tglawal,tglsampai;
    private DatePickerDialog.OnDateSetListener dialogtglawal=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            int i3 = i1 + 1;
            tglawal=Modul.convertDate(Modul.setDatePickerNormal(i,i3,i2));
            btn_tgldari.setText(Modul.setDatePickerNormal(i,i3,i2));
            tglUpdate();
        }
    };
    private DatePickerDialog.OnDateSetListener dialogtglsampai=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            int i3 = i1 + 1;
            tglsampai=Modul.convertDate(Modul.setDatePickerNormal(i,i3,i2));
            btn_tglsampai.setText(Modul.setDatePickerNormal(i,i3,i2));
            tglUpdate();
        }
    };
    private int year,month,day;
    Cursor cursor2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_list_transaksi);
        context=getApplicationContext();
        mediaPlayer=MediaPlayer.create(LaporanTransaksiList.this,R.raw.transaksi);
        mediaPlayer.start();
        kalender=Calendar.getInstance();
        year = kalender.get(1);
        month = kalender.get(2);
        day = kalender.get(5);
        database =AppDatabase.getInstance(context);
        tulbar=findViewById(R.id.tulbar_laporanpelanggan);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laporan Transaksi");
        tv_jmlpesanan=findViewById(R.id.jml_pesanan);
        tv_jmlpendapatan=findViewById(R.id.jml_pendapatan);
        btn_tgldari=findViewById(R.id.btn_tgldari);
        btn_tglsampai=findViewById(R.id.btn_tglsampai);
        Cursor cursor=database.vLaundryDAO().bacaTanggal();
        tglawal=Modul.getDate("yyyyMMdd");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            tglawal=cursor.getString(cursor.getColumnIndex("tglterima"));
        }
        tglsampai= Modul.getDate("yyyyMMdd");
        btn_tgldari.setText(Modul.dateToNormal(tglawal));
        btn_tglsampai.setText(Modul.getDate("dd/MM/yyyy"));
        btn_tgldari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(1);
            }
        });
        btn_tglsampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(2);
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tglawal);
        stringBuilder.append("__");
        stringBuilder.append(tglsampai);
        getLaundry("",stringBuilder.toString());
        txt_cari=findViewById(R.id.txt_cari_laporantransaksi);
        txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(tglawal);
                stringBuilder.append("__");
                stringBuilder.append(tglsampai);
                getLaundry(s, stringBuilder.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(tglawal);
                stringBuilder.append("__");
                stringBuilder.append(tglsampai);
                getLaundry(s, stringBuilder.toString());
                return false;
            }
        });
        btn_cetak=findViewById(R.id.cetak_pdf);
        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                checker=new PermissionChecker(context);
                if(checker.lacksPermissions(REQUIRED_PERMISSION)){
                    PermissionsActivity.startActivityForResult(LaporanTransaksiList.this,PERMISSION_REQUEST_CODE,REQUIRED_PERMISSION);
                }else {
                    new LaporanTransaksiList.Loading(LaporanTransaksiList.this).execute();
                }
            }
        });
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
            doc.addCreator("Abdul Rosyi");
            isiLaporan(doc);
            doc.close();
        }catch (IOException | DocumentException ie){
            LOGE("createPdf: Error"+ie.getLocalizedMessage());
        }catch (ActivityNotFoundException ae){
            Toast.makeText(context,"Tidak ada aplikasi yang support untuk membuka file ini",Toast.LENGTH_SHORT).show();
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

    private void isiLaporan(Document doc) throws IOException,DocumentException{
        Paragraph p=new Paragraph();
        PdfPTable tabel_kop = new PdfPTable(2);
        tabel_kop.setWidthPercentage(100);
        Bitmap bitmap=getBitmapFromVectorDrawable(context,R.drawable.logo_laporan);
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        Image image=Image.getInstance(stream.toByteArray());
        tabel_kop.addCell(createImageCell(image));
        tabel_kop.addCell(createTextCell("Jalan. Pertengahan RT.10/ RW 03 No.6, Cijantung, Kec. Ps.Rebo, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13770."));
        doc.add(tabel_kop);
        doc.add(new LineSeparator());
        Paragraph p1=new Paragraph("Laporan Transaksi", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        p1.setAlignment(Element.ALIGN_CENTER);
        p.add(p1);
        addEmptyLine(p, 1);
        doc.add(p);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        PdfPTable table_isi_header= new PdfPTable(10);
        table_isi_header.setWidthPercentage(100);
        table_isi_header.addCell(getBoldCell("Faktur ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Nama ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Tanggal Terima ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Tanggal Selesai ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Nama Jasa  ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Biaya ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Jumlah Unit ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Jumlah", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Total ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Status ", PdfPCell.ALIGN_CENTER));
        doc.add(table_isi_header);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        PdfPTable table_isi_data = new PdfPTable(10);
        table_isi_data.setWidthPercentage(100);
        cursor2=database.vLaundryDAO().bacaLaundry(tglawal,tglsampai);
        if(Modul.getCount(cursor2)>0) {
            while (cursor2.moveToNext()) {
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "faktur"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "nama_pelanggan"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "tglterima"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "tglselesai"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "namajasa"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "hargajasa"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "jumlahlaundry"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "biayalaundry"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "total"), PdfPCell.ALIGN_CENTER));
                table_isi_data.addCell(getBoldCell(Modul.getString(cursor2, "statuslaundry"), PdfPCell.ALIGN_CENTER));
            }
        }
        doc.add(table_isi_data);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        DateFormat dateFormat=new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String date=dateFormat.format(new Date());
        Paragraph tgl=new Paragraph("Jakarta, "+date, new Font(Font.FontFamily.TIMES_ROMAN, 12));
        tgl.setAlignment(Element.ALIGN_RIGHT);
        doc.add(tgl);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        Paragraph nama=new Paragraph("Kasir",new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        nama.setAlignment(Element.ALIGN_RIGHT);
        doc.add(nama);
    }

    public PdfPCell getBoldCell(String text, int alignment) {
        Phrase f= new Phrase(text);
        f.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
        PdfPCell cell = new PdfPCell(f);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell createImageCell(Image img) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell(img, true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p2 = new Paragraph("GREEN LAB LAUNDRY",new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
        p2.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p2);
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private static void addEmptyLine(Paragraph paragraph, int number){
        for (int i=0; i<number; i++){
            paragraph.add(new Paragraph(""));
        }
    }
    private void getLaundry(String string2,String string3){
        recyclerView=findViewById(R.id.recycler_laporantransaksi);
        daftarlaundry=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        transaksiadapter=new LaporanAdapterTransaksiList(daftarlaundry, LaporanTransaksiList.this);
        recyclerView.setAdapter(transaksiadapter);
        String string4;
        String[] arrstring = string3.split("__");
        if(Integer.valueOf(arrstring[0]) <= Integer.valueOf(arrstring[1])){
            if (string2.isEmpty()) {
                cursor2=database.vLaundryDAO().bacaLaundry(arrstring[0],arrstring[1]);
            } else {
                cursor2=database.vLaundryDAO().bacaLaundryLike(arrstring[0],arrstring[1],string2);
            }
        }else{
            Toast.makeText(context, "Masukkan tanggal dengan benar", Toast.LENGTH_SHORT).show();
        }
        Cursor cursor=database.vLaundryDAO().sumBiayalaundry(arrstring[0],arrstring[1]);
        cursor.moveToNext();
        if(Modul.getCount(cursor2)>0){
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Jumlah Pesanan Diterima : ");
            stringBuilder2.append(Modul.getCount(cursor2));
            tv_jmlpesanan.setText(stringBuilder2.toString());
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Jumlah Pendapatan : Rp. ");
            stringBuilder3.append(cursor.getString(0));
            tv_jmlpendapatan.setText(stringBuilder3.toString());
            while (cursor2.moveToNext()){
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(Modul.getString(cursor2, "faktur"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "nama_pelanggan"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "tglterima"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "namajasa"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "hargajasa"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "jumlahlaundry"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "biayalaundry"));
                stringBuilder4.append("__");
                stringBuilder4.append(Modul.getString(cursor2, "satuanjasa"));
                daftarlaundry.add(stringBuilder4.toString());
            }
        }else{
            tv_jmlpesanan.setText("Jumlah Pesanan Diterima : 0");
            tv_jmlpendapatan.setText("Jumlah Pendapatan : Rp. 0");
        }
        transaksiadapter.notifyDataSetChanged();
    }

    private void tglUpdate(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tglawal);
        stringBuilder.append("__");
        stringBuilder.append(tglsampai);
        getLaundry("", stringBuilder.toString());
    }

    protected Dialog onCreateDialog(int n) {
        if (n == 1) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(LaporanTransaksiList.this, dialogtglawal, year, month, day);
            return datePickerDialog;
        }
        if (n == 2) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(LaporanTransaksiList.this, dialogtglsampai, year, month, day);
            return datePickerDialog;
        }
        return null;
    }

    public void setDate(int n) {
        showDialog(n);
    }

    private class Loading extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Context context;
        Loading(Context context){
            this.context=context;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog= ProgressDialog.show(context,"Membuat Pdf", "Mohon Tunggu...",true,false);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            createPdf(FileUtils.getAppPath(context)+"Laporan_Transaksi.pdf");
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);
            progressDialog.dismiss();
            watermarkPdf(FileUtils.getAppPath(context)+"Laporan_Transaksi.pdf",FileUtils.getAppPath(context)+"Laporan_Transaksi_WaterMark.pdf");
        }
    }
}

