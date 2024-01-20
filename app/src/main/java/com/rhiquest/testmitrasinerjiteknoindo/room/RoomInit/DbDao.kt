package com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Admin
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Barang
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Transaksi
import java.util.concurrent.Flow

@Dao
interface DbDao {

    @Query("Select * from admin WHERE emailid = :emailid AND password = :password")
    fun loginAdmin(emailid:String, password:String) : Boolean

    @Query("Select * from admin WHERE emailid = :emailid")
    fun checkEmailAdmin(emailid:String) : Boolean

    @Query("SELECT * from admin ORDER BY idadmin Asc")
    fun getAllAdmin(): List<Admin>

    @Query("Select * from admin WHERE idadmin = :idadmin")
    fun getDetailAdminByID(idadmin : Int): Admin

    @Query("Select * from admin WHERE emailid = :emailid")
    fun getDetailAdminByEmail(emailid : String): Admin

    @Query("UPDATE admin SET password = :password WHERE idadmin = :idadmin")
    fun updatePasswordAdmin(idadmin: Int, password: String): Int

    @Insert
    fun insertAdmin(admin: Admin)

    @Update
    fun updateAdmin(admin: Admin)

    @Delete
    fun deleteAdmin(admin: Admin)

    @Query("Select * from transaksi ORDER BY tanggaltransaksi Desc")
    fun getAllTransaksi(): List<Transaksi>

    @Query("Select * from transaksi WHERE idtransaksi = :idtransaksi")
    fun getDetailTransaksi(idtransaksi: Int): Transaksi

    @Query("Select * from barang WHERE idtransaksi = :idtransaksi ORDER BY nobarang Asc")
    fun getBarangByID(idtransaksi: Int): List<Barang>

    @Query("SELECT COUNT(*) from barang WHERE idtransaksi = :idtransaksi")
    fun getCountBarang(idtransaksi: Int): Int

    @Query("SELECT SUM(total) from barang WHERE idtransaksi = :idtransaksi")
    fun getSumTotalBarang(idtransaksi: Int): Int

    @Insert
    fun insertTransaksi(transaksi: Transaksi)

    @Update
    fun updateTransaksi(transaksi: Transaksi)

    @Delete
    fun deleteTransaksi(transaksi: Transaksi)

    @Query("Select * from barang ORDER BY namabarang Asc")
    fun getAllBarang(): List<Barang>

    @Query("Select * from barang WHERE idbarang = :idbarang")
    fun getDetailBarangByID(idbarang : Int): Barang

    @Insert
    fun insertBarang(barang: Barang)

    @Update
    fun updateBarang(barang: Barang)

    @Delete
    fun deleteBarang(Barang: Barang)
}