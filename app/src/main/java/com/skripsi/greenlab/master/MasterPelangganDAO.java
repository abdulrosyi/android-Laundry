package com.skripsi.greenlab.master;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MasterPelangganDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPelanggan(MasterPelanggan pelanggan);

    @Query("SELECT * FROM tabel_pelanggan")
    MasterPelanggan[] readDataPelanggan();

    @Query("SELECT * FROM tabel_pelanggan WHERE hutang>0")
    MasterPelanggan[] bacaDataHutang();

    @Query("UPDATE tabel_pelanggan SET hutang= :hutang WHERE nomer_pelanggan= :idpelanggan")
    void updateHutangPelanggan(int hutang, int idpelanggan);

    @Delete
    void deletePelanggan(MasterPelanggan pelanggan);

    @Update
    int updatePelanggan(MasterPelanggan pelanggan);

    @Query("SELECT * FROM tabel_pelanggan")
    List<MasterPelanggan> bacaDataPelanggan();

    @Query("SELECT * FROM tabel_pelanggan WHERE nomer_pelanggan= :idpelanggan")
    List<MasterPelanggan> bacaDataPelangganbyId(long idpelanggan);

    @Query("SELECT SUM(hutang) FROM tabel_pelanggan")
    Cursor sumHutangPelanggan();

    @Query("SELECT * FROM tabel_pelanggan WHERE hutang>0")
    Cursor bacaHutangPelanggan();

    @Query("SELECT * FROM tabel_pelanggan WHERE hutang>0 AND nama_pelanggan LIKE '%' ||:namapelanggan|| '%'")
    Cursor bacaHutangPelangganLike(String namapelanggan);

    @Query("SELECT * FROM tabel_pelanggan WHERE hutang>0")
    List<MasterPelanggan> bacaPelangganHutang();
}
