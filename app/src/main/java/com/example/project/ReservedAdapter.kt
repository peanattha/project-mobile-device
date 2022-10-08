package com.example.project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.ResveredItemLayoutBinding
import com.example.project.url.baseUrl

class ReservedAdapter(val reservedList : ArrayList<Reserved>?,val context: Context)
    : RecyclerView.Adapter<ReservedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View, val binding: ResveredItemLayoutBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            binding.cardImg.setOnClickListener {
                val item = reservedList!![adapterPosition]
                val context:Context = view.context
                val intent = Intent(context, HisReservedActivity::class.java)
                intent.putExtra("payment_id",item.payment_id)
                intent.putExtra("payment_status",item.payment_status)
                intent.putExtra("user_id",item.user_id)
                intent.putExtra("stadium_name",item.stadium_name)
                intent.putExtra("reserve_date",item.reserve_date)
                intent.putExtra("time_start",item.time_start)
                intent.putExtra("time_end",item.time_end)
                intent.putExtra("slip_img",item.slip_img)
                intent.putExtra("total_price",item.total_price)
                intent.putExtra("stadium_img",item.stadium_img)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ResveredItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        Glide.with(context).load(baseUrl +reservedList!![position].stadium_img).into(binding.cardImg)
        binding.stadiumName.text = reservedList!![position].stadium_name.capitalize()
        binding.date.text = "Date: " +reservedList!![position].reserve_date.toString().substring(0, 10)
        binding.time.text = "Time: " +reservedList!![position].time_start+":00 - "+reservedList!![position].time_end+":00"
    }

    override fun getItemCount(): Int {
        return  reservedList!!.size
    }
}