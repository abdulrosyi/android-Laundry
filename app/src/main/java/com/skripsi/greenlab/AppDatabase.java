package com.skripsi.greenlab;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.skripsi.greenlab.master.MasterJasa;
import com.skripsi.greenlab.master.MasterJasaDAO;
import com.skripsi.greenlab.master.MasterPelanggan;
import com.skripsi.greenlab.master.MasterPelangganDAO;
import com.skripsi.greenlab.transaksi.TransaksiHutang;
import com.skripsi.greenlab.transaksi.TransaksiHutangDAO;
import com.skripsi.greenlab.transaksi.TransaksiKebutuhan;
import com.skripsi.greenlab.transaksi.TransaksiKebutuhanDAO;
import com.skripsi.greenlab.transaksi.TransaksiLaundry;
import com.skripsi.greenlab.transaksi.TransaksiLaundryDAO;
import com.skripsi.greenlab.transaksi.TransaksiLaundryDetail;
import com.skripsi.greenlab.transaksi.TransaksiLaundryDetailDAO;
import com.skripsi.greenlab.transaksi.vBayar;
import com.skripsi.greenlab.transaksi.vBayarDAO;
import com.skripsi.greenlab.transaksi.vCart;
import com.skripsi.greenlab.transaksi.vCartDAO;
import com.skripsi.greenlab.transaksi.vLaundry;
import com.skripsi.greenlab.transaksi.vLaundryDAO;
import com.skripsi.greenlab.transaksi.vProses;
import com.skripsi.greenlab.transaksi.vProsesDAO;

@Database(entities = {MasterPelanggan.class, MasterJasa.class, TransaksiLaundry.class, TransaksiLaundryDetail.class,TransaksiHutang.class, TransaksiKebutuhan.class},
        views = {vCart.class, vLaundry.class, vProses.class, vBayar.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MasterPelangganDAO pelangganDAO();
    public abstract MasterJasaDAO jasaDAO();
    public abstract TransaksiLaundryDAO laundryDAO();
    public abstract TransaksiLaundryDetailDAO laundryDetailDAO();
    public abstract vCartDAO cartDAO();
    public abstract vProsesDAO prosesDAO();
    public abstract vLaundryDAO vLaundryDAO();
    public abstract vBayarDAO bayarDAO();
    public abstract TransaksiHutangDAO transaksiHutangDAO();
    public abstract TransaksiKebutuhanDAO kebutuhanDAO();

    private static AppDatabase sInstance;
    public static synchronized AppDatabase getInstance(Context context){
        if(sInstance==null){
            sInstance=Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"dblaundry").addCallback(CALLBACK).allowMainThreadQueries().build();
        }
        return sInstance;
    }

    public static RoomDatabase.Callback CALLBACK=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            db.execSQL("CREATE TRIGGER IF NOT EXISTS kurang_detail " +
                    "AFTER DELETE ON tabel_laundry FOR EACH ROW BEGIN DELETE FROM " +
                    "tabel_laundrydetail WHERE tabel_laundrydetail.idlaundry=OLD.idlaundry; END");
            db.execSQL("CREATE TRIGGER IF NOT EXISTS bayar_hutang AFTER INSERT ON " +
                    "tabel_bayarhutang FOR EACH ROW BEGIN UPDATE tabel_pelanggan " +
                    "SET hutang=hutang-NEW.bayarhutang WHERE tabel_pelanggan.nomer_pelanggan=NEW.idpelanggan; END");
            db.execSQL("INSERT INTO tabel_pelanggan VALUES (0,'Nama','Alamat','012345678',0)");
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
