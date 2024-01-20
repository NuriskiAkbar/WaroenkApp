package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityFormInputBarangBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Barang
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Transaksi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormInputBarang : AppCompatActivity() {

    private lateinit var mainBinding: ActivityFormInputBarangBinding
    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao
    var seqNumber = 0
    var seqNumber2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityFormInputBarangBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        dbInit = DbInit.getDatabase(applicationContext)
        dbDao = dbInit.DbDao()

        val extras = intent.extras
        val formType = extras?.getString("formtype", null)
        val idTransaksi = extras?.getInt("idtransaksi", 0)
        val idBarang = extras?.getInt("idbarang", 0)

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                if (formType == "newdata"){
                    val kodeBarang = generateKode()
                    val noBarang = generateNomor().toString().padStart(3, '0')
                    mainBinding.nomorbarang.setText(noBarang)
                    mainBinding.tfkodebarang.setText(kodeBarang)

                    mainBinding.hargabarang.addTextChangedListener(object : TextWatcher{
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            calculateTotal()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }

                    })
                    mainBinding.jumlahbarang.addTextChangedListener(object : TextWatcher{
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            calculateTotal()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }

                    })

                    mainBinding.btnsimpanbarang.setOnClickListener {
                        val namaBarang = mainBinding.namabarang.text.toString()
                        val savedHargaBarang = mainBinding.hargabarang.text.toString()
                        val savedJumlahBarang = mainBinding.jumlahbarang.text.toString()
                        val savedTotalBarang = mainBinding.totalbarang.text.toString()
                        var error = 0

                        if (kodeBarang.isEmpty()){
                            mainBinding.tfkodebarang.error = "Field ini harusnya terisi nih"
                            mainBinding.tfkodebarang.requestFocus()
                            error++
                        }
                        if (noBarang.isEmpty()){
                            mainBinding.nomorbarang.error = "Field ini harusnya terisi nih"
                            mainBinding.nomorbarang.requestFocus()
                            error++
                        }
                        if (namaBarang.isEmpty()){
                            mainBinding.namabarang.error = "Field ini wajib diisi"
                            mainBinding.namabarang.requestFocus()
                            error++
                        }
                        if (savedHargaBarang.isEmpty()){
                            mainBinding.hargabarang.error = "Field ini wajib diisi"
                            mainBinding.hargabarang.requestFocus()
                            error++
                        }
                        if (savedJumlahBarang.isEmpty()){
                            mainBinding.jumlahbarang.error = "Field ini wajib diisi"
                            mainBinding.jumlahbarang.requestFocus()
                            error++
                        }
                        if (savedTotalBarang.isEmpty()){
                            mainBinding.totalbarang.error = "Field ini wajib diisi"
                            mainBinding.totalbarang.requestFocus()
                            error++
                        }

                        if (error == 0){
                            val barang = Barang(
                                idtransaksi = idTransaksi!!.toInt(),
                                nobarang = noBarang.toInt(),
                                kodebarang = kodeBarang,
                                namabarang = namaBarang,
                                hargabarang = savedHargaBarang.toInt(),
                                jumlahbarang = savedJumlahBarang.toInt(),
                                total = savedTotalBarang.toInt()
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                dbDao.insertBarang(barang)
                                withContext(Dispatchers.Main){
                                    Toast.makeText(
                                        this@FormInputBarang,
                                        "Data berhasil disimpan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val bundle = Bundle()
                                    bundle.putInt("idtransaksi", idTransaksi)
                                    startActivity(Intent(this@FormInputBarang, ListBarangActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .putExtras(bundle))
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(this@FormInputBarang, "Masih ada field yang kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = dbDao.getDetailBarangByID(idBarang!!)
                        withContext(Dispatchers.Main){
                            val kodeBarang = data.kodebarang
                            val noBarang = data.nobarang
                            val namaBarang = data.namabarang
                            val hargaBarang = data.hargabarang
                            val jumlahBarang = data.jumlahbarang
                            val totalBarang = data.total

                            mainBinding.nomorbarang.setText(noBarang.toString())
                            mainBinding.tfkodebarang.setText(kodeBarang)
                            mainBinding.namabarang.setText(namaBarang)
                            mainBinding.jumlahbarang.setText(jumlahBarang.toString())
                            mainBinding.totalbarang.setText(totalBarang.toString())
                            mainBinding.hargabarang.setText(hargaBarang.toString())

                        }
                    }

                    mainBinding.jumlahbarang.addTextChangedListener(object :TextWatcher{
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            calculateTotal()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }

                    })
                    mainBinding.hargabarang.addTextChangedListener(object :TextWatcher{
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            calculateTotal()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }

                    })

                    mainBinding.btnsimpanbarang.setOnClickListener {
                        val namaBarang = mainBinding.namabarang.text.toString()
                        val kodeBarang = mainBinding.tfkodebarang.text.toString()
                        val noBarang = mainBinding.nomorbarang.text.toString()
                        val savedHargaBarang = mainBinding.hargabarang.text.toString()
                        val savedJumlahBarang = mainBinding.jumlahbarang.text.toString()
                        val savedTotalBarang = mainBinding.totalbarang.text.toString()
                        var error = 0

                        if (kodeBarang.isEmpty()){
                            mainBinding.tfkodebarang.error = "Field ini harusnya terisi nih"
                            mainBinding.tfkodebarang.requestFocus()
                            error++
                        }
                        if (noBarang.isEmpty()){
                            mainBinding.nomorbarang.error = "Field ini harusnya terisi nih"
                            mainBinding.nomorbarang.requestFocus()
                            error++
                        }
                        if (namaBarang.isEmpty()){
                            mainBinding.namabarang.error = "Field ini wajib diisi"
                            mainBinding.namabarang.requestFocus()
                            error++
                        }
                        if (savedHargaBarang.isEmpty()){
                            mainBinding.hargabarang.error = "Field ini wajib diisi"
                            mainBinding.hargabarang.requestFocus()
                            error++
                        }
                        if (savedJumlahBarang.isEmpty()){
                            mainBinding.jumlahbarang.error = "Field ini wajib diisi"
                            mainBinding.jumlahbarang.requestFocus()
                            error++
                        }
                        if (savedTotalBarang.isEmpty()){
                            mainBinding.totalbarang.error = "Field ini wajib diisi"
                            mainBinding.totalbarang.requestFocus()
                            error++
                        }

                        if (error == 0){
                            val updatedBarang = Barang(
                                idbarang = idBarang!!.toInt(),
                                idtransaksi = idTransaksi!!.toInt(),
                                nobarang = noBarang.toInt(),
                                namabarang = namaBarang,
                                kodebarang = kodeBarang,
                                hargabarang = savedHargaBarang.toInt(),
                                jumlahbarang = savedJumlahBarang.toInt(),
                                total = savedTotalBarang.toInt()
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                dbDao.updateBarang(updatedBarang)
                                withContext(Dispatchers.Main){
                                    Toast.makeText(this@FormInputBarang, "Data barang berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                    val bundle = Bundle()
                                    bundle.putInt("idtransaksi", idTransaksi)
                                    startActivity(Intent(this@FormInputBarang, ListBarangActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .putExtras(bundle))
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(this@FormInputBarang, "Masih ada field yang kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val extras = intent.extras
        val formType = extras?.getString("formtype", null)
        val idTransaksi = extras?.getInt("idtransaksi", 0)
        val idBarang = extras?.getInt("idbarang", 0)
        val bundle = Bundle()
        bundle.putInt("idtransaksi", idTransaksi!!)
        startActivity(Intent(this@FormInputBarang, ListBarangActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtras(bundle))
        finish()
    }

    private fun calculateTotal() {
        val hargaBarang = mainBinding.hargabarang.text.toString()
        val jumlahBarang = mainBinding.jumlahbarang.text.toString()

        if (hargaBarang.isNotEmpty() && jumlahBarang.isNotEmpty()){
            val hargaBarangRp = hargaBarang.toInt()
            val jumlahBarangRp = jumlahBarang.toInt()

            val totalBarang = hargaBarangRp * jumlahBarangRp
            mainBinding.totalbarang.setText(totalBarang.toString())
        }
    }

    private suspend fun generateKode(): String {
        return withContext(Dispatchers.IO){
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Barang>()
            listItems.addAll(dbDao.getAllBarang())

            if (listItems.isEmpty()) {
                seqNumber2 += 1
            } else {
                val lastIndex = listItems.last()
                val lastId = lastIndex.idtransaksi
                seqNumber2 = lastId + 1
            }
            val randomChar = ('A'..'Z').random()
            "$randomChar${seqNumber2.toString().padStart(3,'0')}"
        }
    }
    private suspend fun generateNomor(): Int {
        return withContext(Dispatchers.IO){
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Barang>()
            listItems.addAll(dbDao.getAllBarang())

            if (listItems.isEmpty()) {
                seqNumber += 1
            } else {
                val lastIndex = listItems.last()
                val lastId = lastIndex.idtransaksi
                seqNumber = lastId + 1
            }

            seqNumber
        }
    }
}