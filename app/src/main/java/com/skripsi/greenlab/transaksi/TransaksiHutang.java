package com.skripsi.greenlab.transaksi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.skripsi.greenlab.master.MasterPelanggan;

import java.io.Serializable;

@Entity(tableName = "tabel_bayarhutang",
        foreignKeys = @ForeignKey(entity = MasterPelanggan.class,
                parentColumns = "nomer_pelanggan",childColumns = "idpelanggan",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.CASCADE))
public class TransaksiHutang implements Serializable {
    @NonNull
    @ColumnInfo(name = "idbayarhutang")
    @PrimaryKey(autoGenerate = true)
    public int idbayarhutang;
    public int getIdbayarhutang(){
        return idbayarhutang;
    }
    public void setIdbayarhutang(int idbayarhutang){
        this.idbayarhutang=idbayarhutang;
    }
    @ColumnInfo(name = "idpelanggan")
    public long idpelanggan;
    public long getIdpelanggan(){
        return idpelanggan;
    }
    public void setIdpelanggan(long idpelanggan){
        this.idpelanggan=idpelanggan;
    }
    @ColumnInfo(name = "tglbayar")
    public String tglbayar;
    public String getTglbayar(){
        return tglbayar;
    }
    public void setTglbayar(String tglbayar){
        this.tglbayar=tglbayar;
    }
    @ColumnInfo(name = "bayar")
    public int bayar;
    public int getBayar(){return bayar;}
    public void setBayar(int bayar){this.bayar=bayar;}
    @ColumnInfo(name = "hutang")
    public int hutang;
    public int getHutang(){return hutang;}
    public void setHutang(int hutang){this.hutang=hutang;}
    @ColumnInfo(name = "bayarhutang")
    public int bayarhutang;
    public int getBayarhutang(){return bayarhutang;}
    public void setBayarhutang(int bayarhutang){this.bayarhutang=bayarhutang;}
    @ColumnInfo(name = "kembali")
    public int kembali;
    public int getKembali(){return kembali;}
    public void setKembali(int kembali){this.kembali=kembali;}
    @ColumnInfo(name = "keteranganhutang")
    public String kethutang;
    public String getKethutang(){return kethutang;}
    public void setKethutang(String kethutang){this.kethutang=kethutang;}

}
