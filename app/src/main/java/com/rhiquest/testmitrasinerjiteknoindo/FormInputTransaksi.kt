package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityFormInputTransaksiBinding
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityMainBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Transaksi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormInputTransaksi : AppCompatActivity() {

    private lateinit var mainBinding: ActivityFormInputTransaksiBinding
    private lateinit var dbDao: DbDao
    private lateinit var dbInit: DbInit
    var seqNumber = 0
    var seqNumber2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityFormInputTransaksiBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        dbInit = DbInit.getDatabase(applicationContext)
        dbDao = dbInit.DbDao()

        val extras = intent.extras
        val formType = extras?.getString("formtype", null)
        val idtransaksi = extras?.getInt("idtransaksi", 0)

        CoroutineScope(Dispatchers.IO).launch{
            withContext(Dispatchers.Main){
                if (formType == "newdata") {
                    val newNoTransaksi = generateNomor().toString().padStart(3,'0')
                    val newKodeTransaksi = generateKode()
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    val tgltransaksi = dateFormat.format(currentDate)

                    mainBinding.tfnomortransaksi.setText(newNoTransaksi)
                    mainBinding.tftanggaltransaksi.setText(tgltransaksi)
                    mainBinding.tfkodecustomer.setText(newKodeTransaksi)

                    mainBinding.btnsimpantransaksi.setOnClickListener {
                        val namaCust = mainBinding.tfnamacustomer.text.toString()
                        val noTelpCust = mainBinding.tfnotelpcustomer.text.toString()
                        val transaksi = Transaksi(
                            kodetransaksi = newKodeTransaksi,
                            notransaksi = newNoTransaksi,
                            namacustomer = namaCust,
                            jumlahbarang = 2,
                            tanggaltransaksi = tgltransaksi,
                            notelpcustomer = noTelpCust,
                            total = 500000
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            dbDao.insertTransaksi(transaksi)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@FormInputTransaksi,
                                    "Data berhasil di tambahkan, $namaCust",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@FormInputTransaksi, DashboardPage::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                finishAffinity()
                            }
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = dbDao.getDetailTransaksi(idtransaksi!!.toInt())
                        withContext(Dispatchers.Main){
                            mainBinding.tfnomortransaksi.setText(data.notransaksi)
                            mainBinding.tftanggaltransaksi.setText(data.tanggaltransaksi)
                            mainBinding.tfkodecustomer.setText(data.kodetransaksi)
                            mainBinding.tfnamacustomer.setText(data.namacustomer)
                            mainBinding.tfnotelpcustomer.setText(data.notelpcustomer)
                        }
                    }
                    mainBinding.btnsimpantransaksi.setOnClickListener {
                        val noTransaksi = mainBinding.tfnomortransaksi.text.toString()
                        val tglTransaksi = mainBinding.tftanggaltransaksi.text.toString()
                        val kodeTransaksi = mainBinding.tfkodecustomer.text.toString()
                        val namaTransaksi = mainBinding.tfnamacustomer.text.toString()
                        val noTelpTransaksi = mainBinding.tfnotelpcustomer.text.toString()
                        var error = 0

                        if (noTransaksi.isEmpty()){
                            mainBinding.tfnomortransaksi.error = "Field ini tidak boleh kosong"
                            mainBinding.tfnomortransaksi.requestFocus()
                            error++
                        }
                        if (tglTransaksi.isEmpty()){
                            mainBinding.tftanggaltransaksi.error = "Field ini tidak boleh kosong"
                            mainBinding.tftanggaltransaksi.requestFocus()
                            error++
                        }
                        if (kodeTransaksi.isEmpty()){
                            mainBinding.tfkodecustomer.error = "Field ini tidak boleh kosong"
                            mainBinding.tfkodecustomer.requestFocus()
                            error++
                        }
                        if (namaTransaksi.isEmpty()){
                            mainBinding.tfnamacustomer.error = "Field ini tidak boleh kosong"
                            mainBinding.tfnamacustomer.requestFocus()
                            error++
                        }
                        if (noTelpTransaksi.isEmpty()){
                            mainBinding.tfnotelpcustomer.error = "Field ini tidak boleh kosong"
                            mainBinding.tfnotelpcustomer.requestFocus()
                            error++
                        }

                        if (error == 0){
                            val transaksi = Transaksi(
                                idtransaksi = idtransaksi!!.toInt(),
                                kodetransaksi = kodeTransaksi,
                                notransaksi = noTransaksi,
                                namacustomer = namaTransaksi,
                                jumlahbarang = 2,
                                tanggaltransaksi = tglTransaksi,
                                notelpcustomer = noTelpTransaksi,
                                total = 500000
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                dbDao.updateTransaksi(transaksi)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@FormInputTransaksi,
                                        "Data berhasil diperbarui",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this@FormInputTransaksi, DashboardPage::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                    finishAffinity()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@FormInputTransaksi,
                                "Masih ada field yang kosong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@FormInputTransaksi, DashboardPage::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finishAffinity()
    }

    private suspend fun generateKode(): String {
        val dateFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        return withContext(Dispatchers.IO) {
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Transaksi>()
            listItems.addAll(dbDao.getAllTransaksi())

            if (listItems.isEmpty()) {
                seqNumber += 1
            } else {
                val lastIndex = listItems.last()
                val lastId = lastIndex.idtransaksi
                seqNumber = lastId + 1
            }

            "$currentDate-${seqNumber.toString().padStart(3, '0')}"
        }
    }

    private suspend fun generateNomor(): Int {
        return withContext(Dispatchers.IO) {
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Transaksi>()
            listItems.addAll(dbDao.getAllTransaksi())

            if (listItems.isEmpty()) {
                seqNumber2 += 1
            } else {
                val lastIndex = listItems.last()
                val lastId = lastIndex.idtransaksi
                seqNumber2 = lastId + 1
            }

            seqNumber2
        }
    }
}
