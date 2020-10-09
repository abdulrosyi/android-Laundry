package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransaksiKebutuhanDAO {
    @Insert
    void inputPengeluaran(TransaksiKebutuhan...transaksiKebutuhans);

    @Query("SELECT * FROM tabel_kebutuhan WHERE faktur LIKE '%'||:faktur||'%' ")
    Cursor cekFaktur(String faktur);

    @Query("SELECT SUM(jumlah) FROM tabel_kebutuhan")
    Cursor sumJumlahKebutuhan();

    @Query("SELECT * FROM tabel_kebutuhan WHERE namakebutuhan LIKE '%' ||:namakebutuhan|| '%'")
    Cursor bacaKebutuhanLike(String namakebutuhan);

    @Query("SELECT * FROM tabel_kebutuhan")
    Cursor bacaKebutuhan();

    @Query("SELECT * FROM tabel_kebutuhan")
    List<TransaksiKebutuhan> bacaKebutuhanbyList();
}
