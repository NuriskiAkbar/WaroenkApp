package com.rhiquest.testmitrasinerjiteknoindo.room.dataclass

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "admin")
@Parcelize
data class Admin(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "idadmin") val idadmin: Int = 0,
    @ColumnInfo(name = "kodeadmin")val kodeadmin:String,
    @ColumnInfo(name = "nama")val nama:String,
    @ColumnInfo(name = "emailid")val emailid:String,
    @ColumnInfo(name = "password")val password:String,
    @ColumnInfo(name = "nohp")val nohp:String
): Parcelable
