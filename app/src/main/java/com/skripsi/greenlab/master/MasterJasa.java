package com.skripsi.greenlab.master;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tabel_jasa")
public class MasterJasa implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "nomer_jasa")
    private int nomer;

    @NonNull
    @ColumnInfo(name = "namakategori")
    private String kategori;

    @NonNull
    @ColumnInfo(name = "namajasa")
    private String namajasa;

    @NonNull
    @ColumnInfo(name = "hargajasa")
    private int hargajasa;

    @NonNull
    @ColumnInfo(name = "satuanjasa")
    private String satuanjasa;

    public void setNomer(int nomer){
        this.nomer=nomer;
    }
    public int getNomer(){
        return nomer;
    }
    public void setKategori(String kategori){
        this.kategori=kategori;
    }
    public String getKategori(){
        return kategori;
    }
    public void setNamajasa(String namajasa){
        this.namajasa=namajasa;
    }

    public String getNamajasa(){
        return namajasa;
    }

    public void setHargajasa(int hargajasa){
        this.hargajasa=hargajasa;
    }

    public int getHargajasa(){
        return hargajasa;
    }

    public void setSatuanjasa(String satuanjasa){
        this.satuanjasa=satuanjasa;
    }

    public String getSatuanjasa(){
        return satuanjasa;
    }

}
