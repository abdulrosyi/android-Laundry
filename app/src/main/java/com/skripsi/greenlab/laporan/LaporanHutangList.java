package com.skripsi.greenlab.laporan;

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
import com.skripsi.greenlab.master.MasterPelanggan;
import com.skripsi.greenlab.transaksi.TransaksiKebutuhan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.skripsi.greenlab.LogUtils.LOGE;
import static com.skripsi.greenlab.PermissionChecker.REQUIRED_PERMISSION;
import static com.skripsi.greenlab.PermissionsActivity.PERMISSION_REQUEST_CODE;

public class LaporanHutangList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LaporanAdapterHutangList adapterHutangList;
    private AppDatabase database;
    private ArrayList<String> daftarhutang;
    private SearchView txt_cari;
    private Toolbar tulbar;
    private TextView tv_jmlhutang,tv_jmlpelangganhutang;
    private Button btn_cetak;
    private PermissionChecker checker;
    private Context context;
    private Cursor cursor1=null;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_list_hutang);
        context=getApplicationContext();
        mediaPlayer=MediaPlayer.create(LaporanHutangList.this,R.raw.hutang_pelanggan);
        mediaPlayer.start();
        database=AppDatabase.getInstance(context);
        tulbar=findViewById(R.id.tulbar_laporanhutang);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laporan Hutang");
        tv_jmlhutang=findViewById(R.id.jml_total_hutang);
        tv_jmlpelangganhutang=findViewById(R.id.jml_pelanggan_hutang);
        getHutang("");
        txt_cari=findViewById(R.id.txt_cari_laporanhutang);
        txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getHutang(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getHutang(s);
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
                    PermissionsActivity.startActivityForResult(LaporanHutangList.this,PERMISSION_REQUEST_CODE,REQUIRED_PERMISSION);
                }else {
                    new Loading(LaporanHutangList.this).execute();
                }


            }
        });
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
            createPdf(FileUtils.getAppPath(context)+"Laporan_Hutang.pdf");
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);
            progressDialog.dismiss();
            watermarkPdf(FileUtils.getAppPath(context)+"Laporan_Hutang.pdf",FileUtils.getAppPath(context)+"Laporan_Hutang_WaterMark.pdf");
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
        Paragraph p1=new Paragraph("Laporan Hutang", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        p1.setAlignment(Element.ALIGN_CENTER);
        p.add(p1);
        addEmptyLine(p, 1);
        doc.add(p);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        PdfPTable table_isi_header= new PdfPTable(4);
        table_isi_header.setWidthPercentage(100);
        table_isi_header.addCell(getBoldCell("Nama Pelanggan ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Alamat Pelanggan  ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Telepeon Pelanggan ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Hutang ", PdfPCell.ALIGN_CENTER));
        doc.add(table_isi_header);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        PdfPTable table_isi_data = new PdfPTable(4);
        table_isi_data.setWidthPercentage(100);
        List<MasterPelanggan> datapelanggan=database.pelangganDAO().bacaPelangganHutang();
        for(MasterPelanggan pelang: datapelanggan){
            String nama=pelang.getNama();
            table_isi_data.addCell(getBoldCell(nama,PdfPCell.ALIGN_CENTER));
            String alamat=pelang.getAlamat();
            table_isi_data.addCell(getBoldCell(alamat,PdfPCell.ALIGN_CENTER));
            String telp=pelang.getTelp();
            table_isi_data.addCell(getBoldCell(telp,PdfPCell.ALIGN_CENTER));
            int hutang=pelang.getHutang();
            table_isi_data.addCell(getBoldCell(String.valueOf(hutang),PdfPCell.ALIGN_CENTER));
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
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private static void addEmptyLine(Paragraph paragraph, int number){
        for (int i=0; i<number; i++){
            paragraph.add(new Paragraph(""));
        }
    }
    private void getHutang(String string2){
        recyclerView=findViewById(R.id.recycler_laporanhutang);
        daftarhutang=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapterHutangList=new LaporanAdapterHutangList(daftarhutang,LaporanHutangList.this);
        recyclerView.setAdapter(adapterHutangList);
        Cursor cursor=database.pelangganDAO().sumHutangPelanggan();
        cursor.moveToNext();
        if(string2.isEmpty()){
            cursor1=database.pelangganDAO().bacaHutangPelanggan();
        }else{
            cursor1=database.pelangganDAO().bacaHutangPelangganLike(string2);
        }
        if(Modul.getCount(cursor1)>0){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Jumlah Pelanggan Hutang : ");
            stringBuilder.append(Modul.getCount(cursor1));
            tv_jmlpelangganhutang.setText(stringBuilder.toString());
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Total Hutang : Rp. ");
            stringBuilder3.append(cursor.getString(0));
            tv_jmlhutang.setText(stringBuilder3.toString());
            while (cursor1.moveToNext()){
                StringBuilder stringBuilder1=new StringBuilder();
                stringBuilder1.append(Modul.getString(cursor1, "nama_pelanggan"));
                stringBuilder1.append("__");
                stringBuilder1.append(Modul.getString(cursor1, "alamat_pelanggan"));
                stringBuilder1.append("__");
                stringBuilder1.append(Modul.getString(cursor1, "telp_pelanggan"));
                stringBuilder1.append("__");
                stringBuilder1.append(Modul.getString(cursor1, "hutang"));
                daftarhutang.add(stringBuilder1.toString());
            }
        }else{
            tv_jmlpelangganhutang.setText("Jumlah Pelanggan Hutang : 0");
            tv_jmlhutang.setText("Total Hutang : Rp. 0");
        }
        adapterHutangList.notifyDataSetChanged();
    }

}
