package com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rhiquest.testmitrasinerjiteknoindo.FormInputAdminActivity
import com.rhiquest.testmitrasinerjiteknoindo.FormInputBarang
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ItemListAdminBinding
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ItemListBarangBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Admin
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Barang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AdminAdapter(
    private val admin: MutableList<Admin>
): RecyclerView.Adapter<AdminAdapter.TransaksiViewHolder>() {

    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao

    class TransaksiViewHolder(private val binding: ItemListAdminBinding): RecyclerView.ViewHolder(binding.root) {
        var kodeAdmin = binding.kodeadmin
        var namaAdmin = binding.namaadmin
        var emailAdmin = binding.emailadmin
        var nohpAdmin = binding.nohpadmin
        var btnEditAdmin = binding.btneditadmin
        var btnHapusAdmin = binding.btnhapusadmin
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransaksiViewHolder {
        val view = ItemListAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        dbInit = DbInit.getDatabase(holder.itemView.context.applicationContext)
        dbDao = dbInit.DbDao()
        var item = admin[position]

        holder.kodeAdmin.text = item.kodeadmin
        holder.namaAdmin.text = item.nama
        holder.emailAdmin.text = item.emailid
        holder.nohpAdmin.text = item.nohp

        holder.btnEditAdmin.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("idadmin", item.idadmin)
            bundle.putString("formtype", "edit")
            val intent = Intent(holder.itemView.context, FormInputAdminActivity::class.java)
                .putExtras(bundle)
           holder.itemView.context.startActivity(intent)
        }
        holder.btnHapusAdmin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbDao.deleteAdmin(admin[position])
                admin.removeAt(position)
                withContext(Dispatchers.Main){
                    notifyDataSetChanged()
                    notifyItemRangeChanged(position, admin.size)
                }
            }
        }
    }

    override fun getItemCount(): Int = admin.size
}