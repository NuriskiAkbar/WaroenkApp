package com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Admin
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Barang
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Transaksi

@Database(entities = [Admin::class, Barang::class, Transaksi::class], version = 9)
abstract class DbInit : RoomDatabase() {

    abstract fun DbDao(): DbDao

    companion object{
        @Volatile
        private var INSTANCE: DbInit? = null

        fun getDatabase(context: Context): DbInit {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): DbInit? {
            return Room.databaseBuilder(
                context.applicationContext,
                DbInit::class.java,
                "dbwarung"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}