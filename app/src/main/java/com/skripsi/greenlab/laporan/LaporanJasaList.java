package com.skripsi.greenlab.laporan;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import androidx.room.Room;

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
import com.skripsi.greenlab.PermissionChecker;
import com.skripsi.greenlab.PermissionsActivity;
import com.skripsi.greenlab.R;
import com.skripsi.greenlab.master.MasterJasa;
import com.skripsi.greenlab.transaksi.TransaksiCariJasaAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.skripsi.greenlab.LogUtils.LOGE;
import static com.skripsi.greenlab.PermissionChecker.REQUIRED_PERMISSION;
import static com.skripsi.greenlab.PermissionsActivity.PERMISSION_REQUEST_CODE;


public class LaporanJasaList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TransaksiCariJasaAdapter jasaAdapter;
    private AppDatabase database;
    private ArrayList<MasterJasa> daftarjasa;
    private SearchView txt_cari;
    private Toolbar tulbar;
    private TextView txt_jml_jasa;
    private Button btn_exportpdf;
    private PermissionChecker checker;
    private Context context;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_list_jasa);
        context = getApplicationContext();
        mediaPlayer=MediaPlayer.create(LaporanJasaList.this,R.raw.jasa);
        mediaPlayer.start();
        tulbar = findViewById(R.id.tulbar_laporanpelanggan);
        setSupportActionBar(tulbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laporan Jasa");
        recyclerView = findViewById(R.id.recycler_laporanjasa);
        daftarjasa = new ArrayList<>();
        database =AppDatabase.getInstance(context);
        daftarjasa.addAll(Arrays.asList(database.jasaDAO().readDataJasa()));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        jasaAdapter = new TransaksiCariJasaAdapter(daftarjasa, LaporanJasaList.this);
        recyclerView.setAdapter(jasaAdapter);
        int jumlahjasa = daftarjasa.size();
        txt_jml_jasa = findViewById(R.id.jml_jasa);
        txt_jml_jasa.setText("Jumlah jasa : " + jumlahjasa);
        txt_cari = findViewById(R.id.txt_cari_laporanjasa);
        txt_cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                jasaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                jasaAdapter.getFilter().filter(newText);
                return false;
            }
        });
        btn_exportpdf = findViewById(R.id.export_pdf);
        btn_exportpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                checker = new PermissionChecker(context);
                if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
                    PermissionsActivity.startActivityForResult(LaporanJasaList.this, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
                } else {
                    watermarkPdf(FileUtils.getAppPath(context) + "Laporan_Jasa.pdf", FileUtils.getAppPath(context) + "Laporan_Jasa_WaterMark.pdf");
                    new LaporanJasaList.Loading(LaporanJasaList.this).execute();
                }
            }
        });
    }

    private class Loading extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Context context;

        Loading(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Membuat Pdf", "Mohon Tunggu...", true, false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            createPdf(FileUtils.getAppPath(context) + "Laporan_Jasa.pdf");
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            progressDialog.dismiss();
            watermarkPdf(FileUtils.getAppPath(context) + "Laporan_Jasa.pdf", FileUtils.getAppPath(context) + "Laporan_Jasa_WaterMark.pdf");
        }
    }

    public void createPdf(String desc) {
        if (new File(desc).exists()) {
            new File(desc).delete();
        }
        try {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(desc));
            doc.open();
            doc.setPageSize(PageSize.A4);
            doc.addCreationDate();
            doc.addAuthor("GREEN LAB");
            doc.addCreator("Abdul Rosyi");
            isiLaporan(doc);
            doc.close();
        } catch (IOException | DocumentException ie) {
            LOGE("createPdf: Error" + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(context, "Tidak ada aplikasi yang support untuk membuka file ini", Toast.LENGTH_SHORT).show();
        }
    }

    public void watermarkPdf(String src, String desc) {
        if (new File(desc).exists()) {
            new File(desc).delete();
        }
        try {
            PdfReader reader = new PdfReader(src);
            int halaman = reader.getNumberOfPages();
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(desc));
            int i = 0;
            PdfContentByte content;
            PdfGState gs;
            while (i < halaman) {
                i++;
                content = stamper.getUnderContent(i);
                gs = new PdfGState();
                gs.setFillOpacity(0.3f);
                gs.setStrokeOpacity(0.3f);
                Rectangle halamansize = reader.getPageSize(i);
                int rotasihalaman = reader.getPageRotation(i);
                float x = (halamansize.getLeft() + halamansize.getRight()) / 2;
                float y = (halamansize.getTop() + halamansize.getBottom()) / 2;
                if (rotasihalaman != 0) {
                    x = (halamansize.getHeight()) / 2;
                    y = (halamansize.getWidth()) / 2;
                    y = y - 80;
                }
                Bitmap bitmap = getBitmapFromVectorDrawable(context, R.drawable.logo_fix);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image img = Image.getInstance(stream.toByteArray());
                float lebar = img.getWidth();
                float tinggi = img.getHeight();
                float pw = halamansize.getWidth();
                float ph = halamansize.getHeight();
                float scaleMulti = (pw / lebar) * 0.5f;
                float lebar_gambar = (lebar * (scaleMulti));
                float tinggi_gambar = (tinggi * (scaleMulti));
                float x_koordinat = x - (lebar_gambar / 2);
                float y_koordinat = y - (tinggi_gambar / 2);
                content.saveState();
                content.setGState(gs);
                content.addImage(img, lebar_gambar, 0, 0, tinggi_gambar, x_koordinat, y_koordinat);
                content.restoreState();
            }
            FileUtils.openFile(context, new File(desc));
            stamper.close();
            reader.close();
        } catch (IOException | DocumentException ie) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PermissionsActivity.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission diizinkan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Gagal, permission tidak diizinkan", Toast.LENGTH_SHORT).show();
        }
    }

    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawble1 = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawble1 = (DrawableCompat.wrap(drawble1)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawble1.getIntrinsicWidth(), drawble1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawble1.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawble1.draw(canvas);
        return bitmap;
    }

    private void isiLaporan(Document doc) throws IOException, DocumentException {
        Paragraph p = new Paragraph();
        PdfPTable tabel_kop = new PdfPTable(2);
        tabel_kop.setWidthPercentage(100);
        Bitmap bitmap = getBitmapFromVectorDrawable(context, R.drawable.logo_laporan);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        tabel_kop.addCell(createImageCell(image));
        tabel_kop.addCell(createTextCell("Jalan. Pertengahan RT.10/ RW 03 No.6, Cijantung, Kec. Ps.Rebo, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13770."));
        doc.add(tabel_kop);
        doc.add(new LineSeparator());
        Paragraph p1 = new Paragraph("Laporan Pelanggan", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        p1.setAlignment(Element.ALIGN_CENTER);
        p.add(p1);
        addEmptyLine(p, 1);
        doc.add(p);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        PdfPTable table_isi_header = new PdfPTable(5);
        table_isi_header.setWidthPercentage(100);
        table_isi_header.addCell(getBoldCell("Nomor ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Kategori ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Jasa ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Harga ", PdfPCell.ALIGN_CENTER));
        table_isi_header.addCell(getBoldCell("Satuan ", PdfPCell.ALIGN_CENTER));
        doc.add(table_isi_header);
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator());
        PdfPTable table_isi_data = new PdfPTable(5);
        table_isi_data.setWidthPercentage(100);
        List<MasterJasa> listjasa = database.jasaDAO().bacaDataJasa();
        for (MasterJasa jasa : listjasa) {
            int nomer = jasa.getNomer();
            table_isi_data.addCell(getBoldCell(String.valueOf(nomer), PdfPCell.ALIGN_CENTER));
            String kategori = jasa.getKategori();
            table_isi_data.addCell(getBoldCell(kategori, PdfPCell.ALIGN_CENTER));
            String namajasa = jasa.getNamajasa();
            table_isi_data.addCell(getBoldCell(namajasa, PdfPCell.ALIGN_CENTER));
            int hargajasa = jasa.getHargajasa();
            table_isi_data.addCell(getBoldCell(String.valueOf(hargajasa), PdfPCell.ALIGN_CENTER));
            String satuan = jasa.getSatuanjasa();
            table_isi_data.addCell(getBoldCell(satuan,PdfPCell.ALIGN_CENTER));
        }
        doc.add(table_isi_data);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String date = dateFormat.format(new Date());
        Paragraph tgl = new Paragraph("Jakarta, " + date, new Font(Font.FontFamily.TIMES_ROMAN, 12));
        tgl.setAlignment(Element.ALIGN_RIGHT);
        doc.add(tgl);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        Paragraph nama = new Paragraph("Kasir", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        nama.setAlignment(Element.ALIGN_RIGHT);
        doc.add(nama);
    }

    public PdfPCell getBoldCell(String text, int alignment) {
        Phrase f = new Phrase(text);
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
        Paragraph p2 = new Paragraph("GREEN LAB LAUNDRY", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
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

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(""));
        }
    }
}


