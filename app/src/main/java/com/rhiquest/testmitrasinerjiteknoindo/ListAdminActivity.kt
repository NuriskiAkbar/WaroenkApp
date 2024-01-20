package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityListAdminBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.AdminAdapter
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Admin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListAdminActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityListAdminBinding
    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityListAdminBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        mainBinding.rvAdmin.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        CoroutineScope(Dispatchers.IO).launch {
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Admin>()
            listItems.addAll(dbDao.getAllAdmin())
            withContext(Dispatchers.Main){
                if (listItems.isEmpty()){
                    mainBinding.rvAdmin.visibility = View.GONE
                    mainBinding.tvNodataadmin.visibility = View.VISIBLE
                } else {
                    mainBinding.rvAdmin.visibility = View.VISIBLE
                    mainBinding.tvNodataadmin.visibility = View.GONE

                    val adapterAdmin = AdminAdapter(listItems)
                    mainBinding.rvAdmin.adapter = adapterAdmin
                }
            }
        }

        mainBinding.fabAdmin.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("formtype","newdata")
            startActivity(Intent(this, FormInputAdminActivity::class.java).putExtras(bundle))
        }
    }
}