package com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rhiquest.testmitrasinerjiteknoindo.FormInputBarang
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ItemListBarangBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Barang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class BarangAdapter(
    private val barang: MutableList<Barang>
): RecyclerView.Adapter<BarangAdapter.TransaksiViewHolder>() {

    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao

    class TransaksiViewHolder(private val binding: ItemListBarangBinding): RecyclerView.ViewHolder(binding.root) {
        var noBarang = binding.nobarang
        var namaBarang = binding.namabarang
        var kodeBarang = binding.kodebarang
        var jumlahBarang = binding.jumlahbarang
        var totalBarang = binding.totalbarang
        var btnEditBarang = binding.btneditbarang
        var btnHapusBarang = binding.btnhapusbarang
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransaksiViewHolder {
        val view = ItemListBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        dbInit = DbInit.getDatabase(holder.itemView.context.applicationContext)
        dbDao = dbInit.DbDao()
        var item = barang[position]

        holder.noBarang.text = item.nobarang.toString().padStart(3,'0')
        holder.namaBarang.text = item.namabarang
        holder.kodeBarang.text = item.kodebarang
        holder.jumlahBarang.text = item.jumlahbarang.toString()
        holder.totalBarang.text = item.total.toString()

        holder.btnEditBarang.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("idtransaksi", item.idtransaksi)
            bundle.putInt("idbarang", item.idbarang)
            bundle.putString("formtype", "edit")
            val intent = Intent(holder.itemView.context, FormInputBarang::class.java)
                .putExtras(bundle)
           holder.itemView.context.startActivity(intent)
        }
        holder.btnHapusBarang.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbDao.deleteBarang(barang[position])
                barang.removeAt(position)
                withContext(Dispatchers.Main){
                    notifyDataSetChanged()
                    notifyItemRangeChanged(position, barang.size)
                }
            }
        }
    }

    override fun getItemCount(): Int = barang.size
}