package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityDashboardPageBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.TransaksiAdapter
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Transaksi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardPage : AppCompatActivity() {

    private lateinit var mainBinding : ActivityDashboardPageBinding
    private lateinit var dbDao: DbDao
    private lateinit var dbInit: DbInit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityDashboardPageBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        mainBinding.actionBar.setNavigationOnClickListener {
            if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)){
                mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                mainBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        mainBinding.drawerMenu.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_listadmin -> {
                    startActivity(Intent(this, ListAdminActivity::class.java))
                    true
                }
                R.id.logout -> {
                    val spLogin = getSharedPreferences("spLogin", MODE_PRIVATE)
                    val edit = spLogin.edit()
                    edit.clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                    true
                }
                else->false
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Transaksi>()
            listItems.addAll(dbDao.getAllTransaksi())

            withContext(Dispatchers.Main){
                if (listItems.isEmpty()) {
                    mainBinding.rvTransaksi.visibility = View.GONE
                    mainBinding.tvNodatatransaksi.visibility = View.VISIBLE
                } else {
                    mainBinding.rvTransaksi.visibility = View.VISIBLE
                    mainBinding.rvTransaksi.layoutManager =
                        LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    val adapterTransaksi = TransaksiAdapter(listItems)
                    mainBinding.rvTransaksi.adapter = adapterTransaksi
                    mainBinding.tvNodatatransaksi.visibility = View.GONE

                }
            }
        }

        mainBinding.fabTransaksi.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("formtype", "newdata")
            startActivity(Intent(this, FormInputTransaksi::class.java).putExtras(bundle))
        }
    }
}