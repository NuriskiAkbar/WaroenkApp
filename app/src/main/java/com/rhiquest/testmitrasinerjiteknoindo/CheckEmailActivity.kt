package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityCheckEmailBinding
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityMainBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckEmailActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityCheckEmailBinding
    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao
    var idAdmin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        dbInit = DbInit.getDatabase(applicationContext)
        dbDao = dbInit.DbDao()
        var checkPasswordError = 0

        mainBinding.btncheckemail.setOnClickListener {
            val email =  mainBinding.tfcheckemail.text.toString()
            CoroutineScope(Dispatchers.IO).launch{
                val isExist = dbDao.checkEmailAdmin(email)
                withContext(Dispatchers.Main){
                    if (isExist){
                        mainBinding.llUbahpassword.visibility = View.VISIBLE
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = dbDao.getDetailAdminByEmail(email)
                            withContext(Dispatchers.Main){
                                idAdmin = data.idadmin
                            }
                        }
                    } else {
                        mainBinding.llUbahpassword.visibility = View.GONE
                        mainBinding.tfcheckemail.error = "Email tersebut tidak terdaftar"
                    }
                }
            }
        }

        mainBinding.rePasswordAdmin.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = mainBinding.rePasswordAdmin.text.toString()
                if (s?.contentEquals(password) == true){
                    checkPasswordError = 0
                    mainBinding.rePasswordAdmin.error = null
                } else if (s.isNullOrBlank()){
                    mainBinding.rePasswordAdmin.error = "re Password masih kosong"
                    checkPasswordError++
                } else {
                    mainBinding.rePasswordAdmin.error = "re Password tidak sama"
                    checkPasswordError++
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mainBinding.btnsimpannewpassword.setOnClickListener {
            val password = mainBinding.passwordAdmin.text.toString()
            val rePassword = mainBinding.rePasswordAdmin.text.toString()
            var error = 0
            if (password.isEmpty()){
                mainBinding.passwordAdmin.error = "Field ini masih kosong"
                mainBinding.passwordAdmin.requestFocus()
                error++
            }
            if (rePassword.isEmpty()){
                mainBinding.rePasswordAdmin.error = "Field ini masih kosong"
                mainBinding.rePasswordAdmin.requestFocus()
                error++
            }

            if (error == 0 && checkPasswordError == 0){
                CoroutineScope(Dispatchers.IO).launch {
                    val update = dbDao.updatePasswordAdmin(idadmin = idAdmin, password = password)
                    withContext(Dispatchers.Main){
                        if (update != 0){
                            startActivity(Intent(this@CheckEmailActivity, LoginActivity::class.java))
                            finishAffinity()
                        } else {
                            Toast.makeText(this@CheckEmailActivity, "Sepertinya ada kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this@CheckEmailActivity, "Sepertinya ada kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}