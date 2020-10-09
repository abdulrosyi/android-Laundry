package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface vBayarDAO {

    @Query("SELECT * FROM vBayar WHERE idlaundry= :idlaundry")
    Cursor bacaBayar(int idlaundry);
}
