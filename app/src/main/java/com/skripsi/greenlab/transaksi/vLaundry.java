package com.skripsi.greenlab.transaksi;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import java.io.Serializable;

@DatabaseView("SELECT " +
        "tabel_laundry.idpelanggan,tabel_pelanggan.nama_pelanggan," +
        "tabel_laundry.faktur, tabel_laundry.tglterima," +
        "tabel_laundry.tglselesai, tabel_laundry.total," +
        "tabel_laundry.bayar,tabel_laundry.kembali," +
        "tabel_laundry.statuslaundry,tabel_laundry.statusbayar," +
        "tabel_laundrydetail.idlaundrydetail,tabel_laundrydetail.idlaundry," +
        "tabel_laundrydetail.idjasa,tabel_jasa.namajasa," +
        "tabel_jasa.hargajasa,tabel_jasa.satuanjasa," +
        "tabel_laundrydetail.jumlahlaundry,tabel_laundrydetail.biayalaundry," +
        "tabel_laundrydetail.keterangan " +
        "FROM tabel_laundrydetail INNER JOIN tabel_laundry ON tabel_laundrydetail.idlaundry = tabel_laundry.idlaundry " +
        "INNER JOIN tabel_jasa ON tabel_laundrydetail.idjasa=tabel_jasa.nomer_jasa " +
        "INNER JOIN tabel_pelanggan ON tabel_laundry.idpelanggan=tabel_pelanggan.nomer_pelanggan")
public class vLaundry implements Serializable {
    @ColumnInfo(name = "idpelanggan")
    public int idpelanggan;
    public int getIdpelanggan(){
        return idpelanggan;
    }
    public void setIdpelanggan(int idpelanggan){
        this.idpelanggan=idpelanggan;
    }
    @ColumnInfo(name = "namapelanggan")
    public String namapelanggan;
    public String getNamapelanggan(){
        return namapelanggan;
    }
    public void setNamapelanggan(String namapelanggan){
        this.namapelanggan=namapelanggan;
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
    @ColumnInfo(name = "bayar")
    public int bayar;
    public int getBayar(){
        return bayar;
    }
    public void setBayar(int bayar){
        this.bayar=bayar;
    }
    @ColumnInfo(name = "kembali")
    public int kembali;
    public int getKembali(){
        return kembali;
    }
    public void setKembali(int kembali){
        this.kembali=kembali;
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
    @ColumnInfo(name = "idlaundrydetail")
    public int idlaundrydetail;
    public int getIdlaundrydetail(){
        return idlaundrydetail;
    }
    public void setIdlaundrydetail(int idlaundrydetail){
        this.idlaundrydetail=idlaundrydetail;
    }
    @ColumnInfo(name = "idlaundry")
    public int idlaundry;
    public int getIdlaundry(){
        return idlaundry;
    }
    public void setIdlaundry(int idlaundry){
        this.idlaundry=idlaundry;
    }
    @ColumnInfo(name = "idjasa")
    public int idjasa;
    public int getIdjasa(){
        return idjasa;
    }
    public void setIdjasa(int idjasa){
        this.idjasa=idjasa;
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
}
