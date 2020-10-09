package com.skripsi.greenlab.transaksi;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;


@Dao
public interface TransaksiLaundryDAO {

    @Query("SELECT idlaundry FROM tabel_laundry")
    Cursor bacaidlaundry();

    @Query("SELECT faktur FROM tabel_laundry WHERE faktur= :faktur")
    Cursor bacafakturlaundry(String faktur);

    @Query("UPDATE tabel_laundry SET idpelanggan=:idpelanggan, tglterima=:tglterima, tglselesai=:tglselesai WHERE idlaundry=:idlaundry")
    void updatetabellaundry(long idpelanggan,String tglterima, String tglselesai, int idlaundry);

    @Query("UPDATE tabel_laundry SET bayar= :bayar, kembali= :kembali, tglbayar= :tglbayar, statuslaundry= :statuslaundry, statusbayar= :statusbayar WHERE faktur= :faktur")
    void updatebayarlaundry(int bayar, int kembali, String tglbayar, String statuslaundry, String statusbayar, String faktur);

    @Query("UPDATE tabel_laundry SET total= :total WHERE faktur= :faktur")
    void updatetotaltabellaundry(int total,String faktur);

    @Query("INSERT INTO tabel_laundry (idpelanggan,faktur,tglterima,tglselesai) VALUES(:idpelanggan, :faktur,:tglterima,:tglselesai)")
    void masukLaundry(long idpelanggan,String faktur,String tglterima,String tglselesai);

    @Query("DELETE FROM tabel_laundry WHERE idlaundry= :idlaundry")
    void deleteidlaundry(int idlaundry);

    @Query("SELECT * FROM tabel_laundry WHERE faktur= :faktur")
    Cursor bacalaundrywhere(String faktur);
}