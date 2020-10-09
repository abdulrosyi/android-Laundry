package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface vProsesDAO {

    @Query("SELECT * FROM vProses")
    vProses[] bacaProses();

    @Query("SELECT * FROM vProses WHERE statuslaundry= :statuslaundry ORDER BY tglterima ASC")
    vProses[] readProses(String statuslaundry);

    @Query("SELECT * FROM vProses WHERE faktur= :faktur")
    Cursor bacaProsesbyFaktur(String faktur);
}
