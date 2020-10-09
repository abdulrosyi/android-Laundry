package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;

import com.skripsi.greenlab.master.MasterPelanggan;

import java.util.List;

@Dao
public interface vLaundryDAO {

    @Query("SELECT * FROM vLaundry WHERE faktur= :faktur")
    Cursor bacaLaundrybyFaktur(String faktur);

    @Query("SELECT * FROM vLaundry WHERE faktur= :faktur")
    List<vLaundry> bacadataLaundry(String faktur);

    @Query("SELECT * FROM vLaundry")
    Cursor bacaTanggal();

    @Query("SELECT * FROM vLaundry WHERE tglterima BETWEEN  :tglterima AND :tglselesai")
    Cursor bacaLaundry(String tglterima,String tglselesai);

    @Query("SELECT * FROM vLaundry WHERE nama_pelanggan LIKE '%'||:nama_pelanggan||'%' AND tglterima BETWEEN  :tglterima AND :tglselesai ")
    Cursor bacaLaundryLike(String tglterima,String tglselesai,String nama_pelanggan);

    @Query("SELECT SUM(biayalaundry) FROM vLaundry WHERE tglterima BETWEEN  :tglterima AND :tglselesai")
    Cursor sumBiayalaundry(String tglterima,String tglselesai);
}
