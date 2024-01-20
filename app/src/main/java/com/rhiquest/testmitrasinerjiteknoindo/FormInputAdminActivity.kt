package com.rhiquest.testmitrasinerjiteknoindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ActivityFormInputAdminBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbDao
import com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit.DbInit
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Admin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormInputAdminActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityFormInputAdminBinding
    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao
    var seqNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityFormInputAdminBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()

        dbInit = DbInit.getDatabase(applicationContext)
        dbDao = dbInit.DbDao()

        val bundle = intent.extras
        val idAdmin = bundle!!.getInt("idadmin", 0)
        val formType = bundle.getString("formtype", null)
        var checkPasswordError = 0

        mainBinding.rePasswordAdmin.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = mainBinding.passwordAdmin.text.toString()
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

        if (formType == "newdata"){
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    val newKodeAdmin = generateKode()
                    mainBinding.tfkodeadmin.setText(newKodeAdmin)
                }
            }

            mainBinding.btnsimpanadmin.setOnClickListener {
                val newKodeAdmin = mainBinding.tfkodeadmin.text.toString()
                val newNamaAdmin = mainBinding.namaAdmin.text.toString()
                val newEmailAdmin = mainBinding.emailAdmin.text.toString()
                val newNoHpAdmin = mainBinding.nomorHpAdmin.text.toString()
                val passwordAdmin = mainBinding.passwordAdmin.text.toString()
                val rePasswordAdmin = mainBinding.rePasswordAdmin.text.toString()
                var error = 0

                if (newKodeAdmin.isEmpty()){
                    mainBinding.tfkodeadmin.error = "Field ini harusnya terisi sih"
                    mainBinding.tfkodeadmin.requestFocus()
                    error++
                }
                if (newNamaAdmin.isEmpty()){
                    mainBinding.namaAdmin.error = "Field ini masih kosong"
                    mainBinding.namaAdmin.requestFocus()
                    error++
                }
                if (newEmailAdmin.isEmpty()){
                    mainBinding.emailAdmin.error = "Field ini masih kosong"
                    mainBinding.emailAdmin.requestFocus()
                    error++
                }
                if (newNoHpAdmin.isEmpty()){
                    mainBinding.nomorHpAdmin.error = "Field ini masih kosong"
                    mainBinding.nomorHpAdmin.requestFocus()
                    error++
                }
                if (passwordAdmin.isEmpty()){
                    mainBinding.passwordAdmin.error= "Field ini masih kosong"
                    mainBinding.passwordAdmin.requestFocus()
                    error++
                }
                if (rePasswordAdmin.isEmpty()){
                    mainBinding.rePasswordAdmin.error = "Field ini masih kosong"
                    mainBinding.rePasswordAdmin.requestFocus()
                    error++
                }

                if (error == 0 && checkPasswordError == 0){
                    val admin = Admin(
                        kodeadmin = newKodeAdmin,
                        nama = newNamaAdmin,
                        emailid = newEmailAdmin,
                        nohp = newNoHpAdmin,
                        password = passwordAdmin
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        dbDao.insertAdmin(admin)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@FormInputAdminActivity, "Data admin berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@FormInputAdminActivity, ListAdminActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, "Masih ada kesalahan pada field atau masih ada yang kosong", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (formType == "newacc"){
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    val newKodeAdmin = generateKode()
                    mainBinding.tfkodeadmin.setText(newKodeAdmin)
                }
            }

            mainBinding.btnsimpanadmin.setOnClickListener {
                val newKodeAdmin = mainBinding.tfkodeadmin.text.toString()
                val newNamaAdmin = mainBinding.namaAdmin.text.toString()
                val newEmailAdmin = mainBinding.emailAdmin.text.toString()
                val newNoHpAdmin = mainBinding.nomorHpAdmin.text.toString()
                val passwordAdmin = mainBinding.passwordAdmin.text.toString()
                val rePasswordAdmin = mainBinding.rePasswordAdmin.text.toString()
                var error = 0

                if (newKodeAdmin.isEmpty()){
                    mainBinding.tfkodeadmin.error = "Field ini harusnya terisi sih"
                    mainBinding.tfkodeadmin.requestFocus()
                    error++
                }
                if (newNamaAdmin.isEmpty()){
                    mainBinding.namaAdmin.error = "Field ini masih kosong"
                    mainBinding.namaAdmin.requestFocus()
                    error++
                }
                if (newEmailAdmin.isEmpty()){
                    mainBinding.emailAdmin.error = "Field ini masih kosong"
                    mainBinding.emailAdmin.requestFocus()
                    error++
                }
                if (newNoHpAdmin.isEmpty()){
                    mainBinding.nomorHpAdmin.error = "Field ini masih kosong"
                    mainBinding.nomorHpAdmin.requestFocus()
                    error++
                }
                if (passwordAdmin.isEmpty()){
                    mainBinding.passwordAdmin.error= "Field ini masih kosong"
                    mainBinding.passwordAdmin.requestFocus()
                    error++
                }
                if (rePasswordAdmin.isEmpty()){
                    mainBinding.rePasswordAdmin.error = "Field ini masih kosong"
                    mainBinding.rePasswordAdmin.requestFocus()
                    error++
                }

                if (error == 0 && checkPasswordError == 0){
                    val admin = Admin(
                        kodeadmin = newKodeAdmin,
                        nama = newNamaAdmin,
                        emailid = newEmailAdmin,
                        nohp = newNoHpAdmin,
                        password = passwordAdmin
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        dbDao.insertAdmin(admin)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@FormInputAdminActivity, "Akun admin berhasil dibuat, silahkan login", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@FormInputAdminActivity, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, "Masih ada kesalahan pada field atau masih ada yang kosong", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val data = dbDao.getDetailAdminByID(idAdmin)
                withContext(Dispatchers.Main){
                    mainBinding.tfkodeadmin.setText(data.kodeadmin)
                    mainBinding.namaAdmin.setText(data.nama)
                    mainBinding.emailAdmin.setText(data.emailid)
                    mainBinding.nomorHpAdmin.setText(data.nohp)
                    mainBinding.passwordAdmin.setText(data.password)
                    mainBinding.rePasswordAdmin.setText(data.password)
                }
            }
            mainBinding.btnsimpanadmin.setOnClickListener {
                val newKodeAdmin = mainBinding.tfkodeadmin.text.toString()
                val newNamaAdmin = mainBinding.namaAdmin.text.toString()
                val newEmailAdmin = mainBinding.emailAdmin.text.toString()
                val newNoHpAdmin = mainBinding.nomorHpAdmin.text.toString()
                val passwordAdmin = mainBinding.passwordAdmin.text.toString()
                val rePasswordAdmin = mainBinding.rePasswordAdmin.text.toString()
                var error = 0

                if (newKodeAdmin.isEmpty()){
                    mainBinding.tfkodeadmin.error = "Field ini harusnya terisi sih"
                    mainBinding.tfkodeadmin.requestFocus()
                    error++
                }
                if (newNamaAdmin.isEmpty()){
                    mainBinding.namaAdmin.error = "Field ini masih kosong"
                    mainBinding.namaAdmin.requestFocus()
                    error++
                }
                if (newEmailAdmin.isEmpty()){
                    mainBinding.emailAdmin.error = "Field ini masih kosong"
                    mainBinding.emailAdmin.requestFocus()
                    error++
                }
                if (newNoHpAdmin.isEmpty()){
                    mainBinding.nomorHpAdmin.error = "Field ini masih kosong"
                    mainBinding.nomorHpAdmin.requestFocus()
                    error++
                }
                if (passwordAdmin.isEmpty()){
                    mainBinding.passwordAdmin.error= "Field ini masih kosong"
                    mainBinding.passwordAdmin.requestFocus()
                    error++
                }
                if (rePasswordAdmin.isEmpty()){
                    mainBinding.rePasswordAdmin.error = "Field ini masih kosong"
                    mainBinding.rePasswordAdmin.requestFocus()
                    error++
                }

                if (error == 0 && checkPasswordError == 0){
                    val admin = Admin(
                        idadmin = idAdmin,
                        kodeadmin = newKodeAdmin,
                        nama = newNamaAdmin,
                        emailid = newEmailAdmin,
                        nohp = newNoHpAdmin,
                        password = passwordAdmin
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        dbDao.updateAdmin(admin)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@FormInputAdminActivity, "Data admin berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@FormInputAdminActivity, ListAdminActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, "Masih ada kesalahan pada field atau masih ada yang kosong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun generateKode(): String {
        return withContext(Dispatchers.IO){
            dbInit = DbInit.getDatabase(applicationContext)
            dbDao = dbInit.DbDao()
            val listItems = arrayListOf<Admin>()
            listItems.addAll(dbDao.getAllAdmin())

            var charList = ('A'.. 'Z')
            val numberList = (0..99)
            val randomNumber = numberList.random().toString().padStart(2, '0')
            val randomChar = charList.random()

            if (listItems.isEmpty()){
                seqNumber += 1
            } else {
                val lastItem = listItems.last()
                val lastId = lastItem.idadmin
                seqNumber = lastId + 1
            }
            "$randomChar$randomNumber.${seqNumber.toString().padStart(3, '0')}"
        }
    }
}