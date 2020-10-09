package com.skripsi.greenlab.master;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tabel_pelanggan")

public class MasterPelanggan implements Serializable {

    @NonNull
    @ColumnInfo(name = "nomer_pelanggan")
    @PrimaryKey(autoGenerate = true)
    private long nomer;

    @NonNull
    @ColumnInfo(name = "nama_pelanggan")
    private String nama;

    @NonNull
    @ColumnInfo(name = "alamat_pelanggan")
    private String alamat;

    @NonNull
    @ColumnInfo(name = "telp_pelanggan")
    private String telp;

    @ColumnInfo(name = "hutang")
    public int hutang;
    public int getHutang(){
        return hutang;
    }
    public void setHutang(int hutang){
        this.hutang=hutang;
    }

    public long getNomer(){
        return nomer;
    }
    public void setNomer(long nomer){
        this.nomer=nomer;
    }

    public String getNama(){
        return nama;
    }

    public void setNama(String nama){
        this.nama=nama;
    }

    public String getAlamat(){
        return alamat;
    }

    public void setAlamat(String alamat){
        this.alamat=alamat;
    }

    public String getTelp(){
        return telp;
    }

    public void setTelp(String telp){
        this.telp=telp;
    }
}
