package com.skripsi.greenlab.transaksi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.skripsi.greenlab.master.MasterJasa;

import java.io.Serializable;

@Entity (tableName = "tabel_laundrydetail",
         foreignKeys = { @ForeignKey(entity = TransaksiLaundry.class,
                 parentColumns = "idlaundry",
                 childColumns = "idlaundry",
                 onUpdate = ForeignKey.CASCADE,
                 onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = MasterJasa.class,
                 parentColumns = "nomer_jasa",
                childColumns = "idjasa",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.RESTRICT)}
                )
public class TransaksiLaundryDetail implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
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

    public TransaksiLaundryDetail(){

    }

}
