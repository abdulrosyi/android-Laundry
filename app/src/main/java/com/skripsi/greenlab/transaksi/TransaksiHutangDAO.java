package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TransaksiHutangDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertHutang(TransaksiHutang...transaksiHutangs);

}
