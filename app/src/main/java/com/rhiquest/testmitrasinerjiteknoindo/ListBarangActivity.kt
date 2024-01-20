package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityListBarangBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.BarangAdapter
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Barang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListBarangActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityListBarangBinding
    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityListBarangBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        val bundle = intent.extras
        val idtransaksi = bundle?.getInt("idtransaksi", 0)

        CoroutineScope(Dispatchers.IO).launch {
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Barang>()
            listItems.addAll(dbDao.getBarangByID(idtransaksi!!))
            withContext(Dispatchers.Main){
                if (listItems.isEmpty()){
                    mainBinding.rvBarang.visibility = View.GONE
                    mainBinding.tvNodatabarang.visibility = View.VISIBLE
                } else {
                    mainBinding.rvBarang.visibility = View.VISIBLE
                    mainBinding.rvBarang.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    val adapterBarang = BarangAdapter(listItems)
                    mainBinding.rvBarang.adapter = adapterBarang
                    mainBinding.tvNodatabarang.visibility = View.GONE
                }
            }
        }


        mainBinding.fabBarang.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("formtype","newdata")
            bundle.putInt("idtransaksi",idtransaksi!!)
            val intent = Intent(this, FormInputBarang::class.java).putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val bundle = Bundle()
        startActivity(Intent(this@ListBarangActivity, DashboardPage::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtras(bundle))
        finish()
    }
}