package com.rhiquest.testmitrasinerjiteknoindo.room.dataclass

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "transaksi")
@Parcelize
data class Transaksi(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "idtransaksi") var idtransaksi: Int = 0,
    @ColumnInfo(name = "kodetransaksi") var kodetransaksi:String = "",
    @ColumnInfo(name = "notransaksi") var notransaksi:String = "",
    @ColumnInfo(name = "namacustomer") var namacustomer:String = "",
    @ColumnInfo(name = "jumlahbarang") var jumlahbarang:Int = 0,
    @ColumnInfo(name = "tanggaltransaksi") var tanggaltransaksi:String = "",
    @ColumnInfo(name = "notelpcustomer") var notelpcustomer:String = "",
    @ColumnInfo(name = "total") var total:Int = 0
): Parcelable
