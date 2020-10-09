package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;

import com.skripsi.greenlab.transaksi.vCart;

@Dao
public interface vCartDAO {

    @Query("SELECT * FROM vCart WHERE faktur= :faktur")
    Cursor bacaCartbyFaktur(String faktur);

    @Query("SELECT * FROM vCart WHERE faktur= :faktur")
    vCart[] bacaCart(String faktur);


}
