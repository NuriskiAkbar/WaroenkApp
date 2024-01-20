package com.rhiquest.testmitrasinerjiteknoindo.room.dataclass

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "barang")
@Parcelize
data class Barang(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "idbarang") var idbarang:Int = 0,
    @ColumnInfo(name = "idtransaksi") var idtransaksi: Int = 0,
    @ColumnInfo(name = "nobarang") var nobarang: Int = 0,
    @ColumnInfo(name = "namabarang") var namabarang: String = "",
    @ColumnInfo(name = "kodebarang") var kodebarang: String = "",
    @ColumnInfo(name = "hargabarang") var hargabarang: Int = 0,
    @ColumnInfo(name = "jumlahbarang") var jumlahbarang: Int = 0,
    @ColumnInfo(name = "total") var total: Int = 0
): Parcelable