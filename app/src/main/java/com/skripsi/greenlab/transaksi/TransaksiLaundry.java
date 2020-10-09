package com.skripsi.greenlab.transaksi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.skripsi.greenlab.master.MasterPelanggan;

import java.io.Serializable;

@Entity(tableName = "tabel_laundry",
        foreignKeys = @ForeignKey(entity = MasterPelanggan.class,parentColumns = "nomer_pelanggan",
                childColumns = "idpelanggan",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.CASCADE))
public class TransaksiLaundry implements Serializable {

    @NonNull
    @ColumnInfo(name = "idlaundry")
    @PrimaryKey(autoGenerate = true)
    private int idlaundry;

    public int getIdlaundry(){
        return idlaundry;
    }

    public void setIdlaundry(int idlaundry) {
        this.idlaundry = idlaundry;
    }

    @ColumnInfo(name = "idpelanggan")
    public long idpelanggan;
    public long getIdpelanggan(){
        return idpelanggan;
    }
    public void setIdpelanggan(long idpelanggan){
        this.idpelanggan=idpelanggan;
    }

    @ColumnInfo(name = "faktur")
    private String faktur;
    public String getFaktur(){
        return faktur;
    }
    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    @ColumnInfo(name = "tglterima")
    private String tglterima;
    public String getTglterima(){
        return tglterima;
    }
    public void setTglterima(String tglterima){
        this.tglterima=tglterima;
    }

    @ColumnInfo(name = "tglselesai")
    private String tglselesai;
    public String getTglselesai(){
        return tglselesai;
    }
    public void setTglselesai(String tglselesai){
        this.tglselesai=tglselesai;
    }

    @ColumnInfo(name = "total")
    private String total;
    public String getTotal(){
        return total;
    }
    public void setTotal(String total){
        this.total=total;
    }

    @ColumnInfo(name = "bayar")
    private String bayar;
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
