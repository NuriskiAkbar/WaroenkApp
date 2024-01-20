package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        val spLogin = getSharedPreferences("spLogin", MODE_PRIVATE)
        val idLogin = spLogin.getString("idLogin", null)

        val handler = Handler()
        handler.postDelayed({
            when(idLogin){
                null -> {
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                    finishAffinity()
                }
                else -> {
                    startActivity(
                        Intent(this, DashboardPage::class.java)

                    )
                    finishAffinity()
                }
            }

        },1500)
    }
}