package com.skripsi.greenlab.transaksi;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import java.io.Serializable;

@DatabaseView("SELECT " +
        "tabel_laundrydetail.idlaundrydetail, tabel_laundrydetail.idjasa, " +
        "tabel_laundrydetail.idlaundry,tabel_laundry.faktur, " +
        "tabel_laundrydetail.jumlahlaundry, tabel_laundrydetail.biayalaundry, " +
        "tabel_laundrydetail.keterangan, tabel_jasa.namakategori," +
        "tabel_jasa.namajasa, tabel_jasa.hargajasa, tabel_jasa.satuanjasa " +
        " FROM tabel_laundrydetail INNER JOIN tabel_laundry ON tabel_laundrydetail.idlaundry = tabel_laundry.idlaundry"+
        " INNER JOIN tabel_jasa ON tabel_laundrydetail.idjasa=tabel_jasa.nomer_jasa")

public class vCart implements Serializable {
    @ColumnInfo(name = "idlaundrydetail")
    public int idlaundrydetail;
    public int getIdlaundrydetail(){
        return idlaundrydetail;
    }
    public void setIdlaundrydetail(int idlaundrydetail){
        this.idlaundrydetail=idlaundrydetail;
    }
    @ColumnInfo(name = "idjasa")
    public int idjasa;
    public int getIdjasa(){
        return idjasa;
    }
    public void setIdjasa(int idjasa){
        this.idjasa=idjasa;
    }
    @ColumnInfo(name = "idlaundry")
    public int idlaundry;
    public int getIdlaundry(){
        return idlaundry;
    }
    public void setIdlaundry(int idlaundry){
        this.idlaundry=idlaundry;
    }
    @ColumnInfo(name = "faktur")
    public String faktur;
    public String getFaktur(){
        return faktur;
    }
    public void setFaktur(String faktur){
        this.faktur=faktur;
    }
    @ColumnInfo(name = "jumlahlaundry")
    public int jumlahlaundry;
    public int getJumlahlaundry(){
        return jumlahlaundry;
    }
    public void setJumlahlaundry(int jumlahlaundry){
        this.jumlahlaundry=jumlahlaundry;
    }
    @ColumnInfo(name = "biayalaundry")
    public int biayalaundry;
    public int getBiayalaundry(){
        return biayalaundry;
    }
    public void setBiayalaundry(int biayalaundry){
        this.biayalaundry=biayalaundry;
    }
    @ColumnInfo(name = "keterangan")
    public String keterangan;
    public String getKeterangan(){
        return keterangan;
    }
    public void setKeterangan(String keterangan){
        this.keterangan=keterangan;
    }
    @ColumnInfo(name = "namakategori")
    public String namakategori;
    public String getNamakategori(){
        return namakategori;
    }
    public void setNamakategori(String namakategori){
        this.namakategori=namakategori;
    }
    @ColumnInfo(name = "namajasa")
    public String namajasa;
    public String getNamajasa(){
        return namajasa;
    }
    public void setNamajasa(String namajasa){
        this.namajasa=namajasa;
    }
    @ColumnInfo(name = "hargajasa")
    public int hargajasa;
    public int getHargajasa(){
        return hargajasa;
    }
    public void setHargajasa(int hargajasa){
        this.hargajasa=hargajasa;
    }
    @ColumnInfo(name = "satuanjasa")
    public String satuanjasa;
    public String getSatuanjasa(){
        return satuanjasa;
    }
    public void setSatuanjasa(String satuanjasa){
        this.satuanjasa=satuanjasa;
    }
}
