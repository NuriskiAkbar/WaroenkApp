package com.rhiquest.testmitrasinerjiteknoindo.room.RoomInit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rhiquest.testmitrasinerjiteknoindo.FormInputTransaksi
import com.rhiquest.testmitrasinerjiteknoindo.ListBarangActivity
import com.rhiquest.testmitrasinerjiteknoindo.databinding.ItemListTransaksiBinding
import com.rhiquest.testmitrasinerjiteknoindo.room.dataclass.Transaksi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class TransaksiAdapter(
    private val transaksi: MutableList<Transaksi>
): RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    private lateinit var dbInit: DbInit
    private lateinit var dbDao: DbDao

    class TransaksiViewHolder(private val binding: ItemListTransaksiBinding): RecyclerView.ViewHolder(binding.root) {
        var tvKodeTransaksi = binding.kodetransaksi
        var tvNamaCustomer = binding.namacustomer
        var tvJumlahTransaksi = binding.jumlahtransaksi
        var tvTanggalTransaksi = binding.tanggaltransaksi
        var tvTotalTransaksi = binding.totaltransaksi
        var btnEditTransaksi = binding.btnedittransaksi
        var btnHapusTransaksi = binding.btnhapustransaksi
        var clTransaksi = binding.cltransaksi

    /*fun bindData(transaksi: Transaksi) {
            binding.kodetransaksi.text = transaksi.kodetransaksi
            binding.namacustomer.text = transaksi.namacustomer
            binding.jumlahtransaksi.text = transaksi.jumlahbarang.toString()

            val originalDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val originalDate = originalDateFormat.parse(transaksi.tanggaltransaksi)

            val targetDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            val formattedData = targetDateFormat.format(originalDate)
            binding.tanggaltransaksi.text = formattedData
            binding.totaltransaksi.text = transaksi.total.toString()

            binding.btnedittransaksi.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("idtransaksi",transaksi.idtransaksi.toString())
                bundle.putString("formtype", "edit")
                val intent = Intent(binding.root.context, FormInputTransaksi::class.java).putExtras(bundle)
                binding.root.context.startActivity(intent)
            }

            binding.btnhapustransaksi.setOnClickListener {
            }
        }*/
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransaksiViewHolder {
        val view = ItemListTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        dbInit = DbInit.getDatabase(holder.itemView.context.applicationContext)
        dbDao = dbInit.DbDao()
        var item = transaksi[position]

        CoroutineScope(Dispatchers.IO).launch {
            val countBarang = dbDao.getCountBarang(idtransaksi = item.idtransaksi)
            val sumTotal = dbDao.getSumTotalBarang(idtransaksi = item.idtransaksi)
            withContext(Dispatchers.Main){
                holder.tvJumlahTransaksi.setText(countBarang.toString())
                holder.tvTotalTransaksi.setText(sumTotal.toString())
            }
        }
        holder.tvKodeTransaksi.text = item.kodetransaksi
        holder.tvNamaCustomer.text = item.namacustomer
        val originalDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val originalDate = originalDateFormat.parse(item.tanggaltransaksi)

        val targetDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedData = targetDateFormat.format(originalDate)
        holder.tvTanggalTransaksi.text = formattedData
        holder.tvJumlahTransaksi.text = item.jumlahbarang.toString()
        holder.tvTotalTransaksi.text = item.total.toString()

        holder.clTransaksi.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("idtransaksi", item.idtransaksi)
            val intent = Intent(holder.itemView.context, ListBarangActivity::class.java)
                .putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }

        holder.btnEditTransaksi.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("idtransaksi", item.idtransaksi)
            bundle.putString("formtype", "edit")
            val intent = Intent(holder.itemView.context, FormInputTransaksi::class.java)
                .putExtras(bundle)
           holder.itemView.context.startActivity(intent)
        }
        holder.btnHapusTransaksi.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbDao.deleteTransaksi(transaksi[position])
                transaksi.removeAt(position)
                withContext(Dispatchers.Main){
                    notifyDataSetChanged()
                    notifyItemRangeChanged(position, transaksi.size)
                }
            }

        }
    }

    override fun getItemCount(): Int = transaksi.size
}