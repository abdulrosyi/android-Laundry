package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransaksiLaundryDetailDAO {

    @Query("SELECT SUM(biayalaundry) AS TOTAL FROM tabel_laundrydetail WHERE idlaundry= :idlaundry")
    Cursor sumTotal(int idlaundry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertlaundryDetail(TransaksiLaundryDetail laundryDetail);

    @Query("DELETE FROM tabel_laundrydetail WHERE idlaundrydetail= :idlaundrydetail ")
    void deletebyidlaundrydetail(int idlaundrydetail);
}
