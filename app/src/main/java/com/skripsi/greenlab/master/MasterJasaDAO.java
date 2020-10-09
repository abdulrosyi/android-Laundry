package com.skripsi.greenlab.master;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MasterJasaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertJasa(MasterJasa jasa);

    @Query("SELECT * FROM tabel_jasa")
    MasterJasa[] readDataJasa();

    @Query("SELECT * FROM tabel_jasa")
    List<MasterJasa> bacaDataJasa();

    @Delete
    void deleteJasa(MasterJasa jasa);

    @Update
    int updateJasa(MasterJasa jasa);
}
