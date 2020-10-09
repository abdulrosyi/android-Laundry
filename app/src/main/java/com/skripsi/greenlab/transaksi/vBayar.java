package com.skripsi.greenlab.transaksi;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@DatabaseView("SELECT " +
        "tabel_laundry.idlaundry,tabel_laundry.idpelanggan," +
        "tabel_pelanggan.nama_pelanggan,tabel_laundry.total," +
        "tabel_laundry.bayar,tabel_laundry.kembali,tabel_laundry.tglbayar," +
        "tabel_laundry.statusbayar,tabel_laundry.statuslaundry" +
        " FROM tabel_laundry INNER JOIN tabel_pelanggan ON tabel_laundry.idpelanggan=tabel_pelanggan.nomer_pelanggan")
public class vBayar implements Serializable {
    @ColumnInfo(name = "idlaundry")
    private int idlaundry;
    public int getIdlaundry(){
        return idlaundry;
    }
    public void setIdlaundry(int idlaundry) {
        this.idlaundry = idlaundry;
    }
    @ColumnInfo(name = "idpelanggan")
    public int idpelanggan;
    public int getIdpelanggan(){
        return idpelanggan;
    }
    public void setIdpelanggan(int idpelanggan){
        this.idpelanggan=idpelanggan;
    }
    @ColumnInfo(name = "nama_pelanggan")
    public String nama;
    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama=nama;
    }
    @ColumnInfo(name = "total")
    public int total;
    public int getTotal(){
        return total;
    }
    public void setTotal(int total){
        this.total=total;
    }
    @ColumnInfo(name = "bayar")
    public String bayar;
    public String getBayar(){
        return bayar;
    }
    public void setBayar(String bayar){
        this.bayar=bayar;
    }
    @ColumnInfo(name = "kembali")
    private String kembali;
    public String getKembali(){
        return kembali;
    }
    public void setKembali(String kembali){
        this.kembali=kembali;
    }
    @ColumnInfo(name = "tglbayar")
    private String tglbayar;
    public String getTglbayar(){
        return tglbayar;
    }
    public void setTglbayar(String tglbayar){
        this.tglbayar=tglbayar;
    }
    @ColumnInfo(name = "statuslaundry", defaultValue = "Proses")
    private String statuslaundry;
    public String getStatuslaundry(){
        return statuslaundry;
    }
    public void setStatuslaundry(String statuslaundry){
        this.statuslaundry=statuslaundry;
    }
    @ColumnInfo(name = "statusbayar",defaultValue = "Belum Bayar")
    private String statusbayar;
    public String getStatusbayar(){
        return statusbayar;
    }
    public void setStatusbayar(String statusbayar){
        this.statusbayar=statusbayar;
    }
}
