package com.rhiquest.testmitrasinerjiteknoindo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityLoginBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityLoginBinding
    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        val spLogin = getSharedPreferences("spLogin", MODE_PRIVATE)
        val save = spLogin.edit()
        dbInit = DbInit.getDatabase(applicationContext)
        dbDao = dbInit.DbDao()

        mainBinding.tfemailid.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s!!.contains("@")){
                    mainBinding.tfemailid.error = "Email anda tidak ada karakter '@'"
                    mainBinding.tfemailid.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        mainBinding.btnLogin.setOnClickListener {
            var error = 0
            val emailId = mainBinding.tfemailid.text.toString()
            val password = mainBinding.tfpassword.text.toString()

            if (emailId.isEmpty()){
                mainBinding.tfemailid.error = "Field ini tidak boleh kosong"
                mainBinding.tfemailid.requestFocus()
                error++
            }
            if (password.isEmpty()){
                mainBinding.tfpassword.error = "Field ini tidak boleh kosong"
                mainBinding.tfpassword.requestFocus()
                error++
            }

            if (error == 0){
                CoroutineScope(Dispatchers.IO).launch {
                    val isExist = dbDao.loginAdmin(emailid = emailId, password = password)
                    withContext(Dispatchers.Main){
                        if (isExist){
                            Toast.makeText(applicationContext, "Selamat Datang", Toast.LENGTH_SHORT).show()
                            save.apply{
                                putString("idLogin",emailId)
                            }.apply()
                            startActivity(Intent(applicationContext, DashboardPage::class.java))
                        } else {
                            Toast.makeText(this@LoginActivity, "Silahkan hubungi Admin yang sudah terdaftar sebelumnya", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                Toast.makeText(this, "Sepertinya masih ada yang salah, cek lagi", Toast.LENGTH_SHORT).show()
            }
        }

        mainBinding.tvBuatakunbaru?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("formtype","newacc")
            startActivity(Intent(this@LoginActivity, FormInputAdminActivity::class.java).putExtras(bundle))
        }

        mainBinding.tvlupapassword?.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CheckEmailActivity::class.java))
        }
    }
}