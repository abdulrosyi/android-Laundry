package com.skripsi.greenlab.transaksi;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import java.io.Serializable;

@DatabaseView("SELECT " +
        "tabel_laundry.idlaundry, tabel_laundry.idpelanggan," +
        "tabel_laundry.faktur, tabel_laundry.tglterima," +
        "tabel_laundry.tglselesai,tabel_laundry.total," +
        "tabel_laundry.statuslaundry,tabel_laundry.statusbayar," +
        "tabel_pelanggan.nama_pelanggan,tabel_pelanggan.alamat_pelanggan," +
        "tabel_pelanggan.telp_pelanggan " +
        " FROM tabel_laundry INNER JOIN tabel_pelanggan ON tabel_laundry.idpelanggan=tabel_pelanggan.nomer_pelanggan")

public class vProses implements Serializable {
    @ColumnInfo(name = "idlaundry")
    public int idlaundry;
    public int getIdlaundry(){
        return idlaundry;
    }
    public void setIdlaundry(int idlaundry){
        this.idlaundry=idlaundry;
    }
    @ColumnInfo(name = "idpelanggan")
    public int idpelanggan;
    public int getIdpelanggan(){
        return idpelanggan;
    }
    public void setIdpelanggan(int idpelanggan){
        this.idpelanggan=idpelanggan;
    }
    @ColumnInfo(name = "faktur")
    public String faktur;
    public String getFaktur(){
        return faktur;
    }
    public void setFaktur(String faktur){
        this.faktur=faktur;
    }
    @ColumnInfo(name = "tglterima")
    public String tglterima;
    public String getTglterima(){
        return tglterima;
    }
    public void setTglterima(String tglterima){
        this.tglterima=tglterima;
    }
    @ColumnInfo(name = "tglselesai")
    public String tglselesai;
    public String getTglselesai(){
        return tglselesai;
    }
    public void setTglselesai(String tglselesai){
        this.tglselesai=tglselesai;
    }
    @ColumnInfo(name = "total")
    public int total;
    public int getTotal(){
        return total;
    }
    public void setTotal(int total){
        this.total=total;
    }
    @ColumnInfo(name = "statuslaundry")
    public String statuslaundry;
    public String getStatuslaundry(){
        return statuslaundry;
    }
    public void setStatuslaundry(String statuslaundry){
        this.statuslaundry=statuslaundry;
    }
    @ColumnInfo(name = "statusbayar")
    public String statusbayar;
    public String getStatusbayar(){
        return statusbayar;
    }
    public void setStatusbayar(String statusbayar){
        this.statusbayar=statusbayar;
    }
    @ColumnInfo(name = "nama_pelanggan")
    public String nama;
    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama=nama;
    }
    @ColumnInfo(name = "alamat_pelanggan")
    public String alamat;
    public String getAlamat(){
        return alamat;
    }
    public void setAlamat(String alamat){
        this.alamat=alamat;
    }
    @ColumnInfo(name = "telp_pelanggan")
    public String telp;
    public String getTelp(){
        return telp;
    }
    public void setTelp(String telp){
        this.telp=telp;
    }
}
