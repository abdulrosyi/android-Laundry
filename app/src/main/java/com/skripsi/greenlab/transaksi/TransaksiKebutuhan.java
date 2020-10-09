package com.skripsi.greenlab.transaksi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tabel_kebutuhan")
public class TransaksiKebutuhan implements Serializable {
    @NonNull
    @ColumnInfo(name = "idkebutuhan")
    @PrimaryKey(autoGenerate = true)
    public long idkebutuhan;
    public long getIdkebutuhan() {
        return idkebutuhan;
    }
    public void setIdkebutuhan(long idkebutuhan) {
        this.idkebutuhan = idkebutuhan;
    }
    @ColumnInfo(name = "tglkebutuhan")
    public String tglkebutuhan;
    public String getTglkebutuhan() {
        return tglkebutuhan;
    }
    public void setTglkebutuhan(String tglkebutuhan) {
        this.tglkebutuhan = tglkebutuhan;
    }
    @ColumnInfo(name = "faktur")
    public String faktur;
    public String getFaktur() {
        return faktur;
    }
    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }
    @ColumnInfo(name = "namakebutuhan")
    public String namakebutuhan;
    public String getNamakebutuhan() {
        return namakebutuhan;
    }
    public void setNamakebutuhan(String namakebutuhan) {
        this.namakebutuhan = namakebutuhan;
    }
    @ColumnInfo(name = "jumlah")
    public int jumlah;
    public int getJumlah() {
        return jumlah;
    }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    @ColumnInfo(name = "keterangan")
    public String keterangan;
    public String getKeterangan() {
        return keterangan;
    }
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
