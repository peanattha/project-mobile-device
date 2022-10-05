package com.example.project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.StadiumItemLayoutBinding

class stadiumAdapter (val stadiumList : ArrayList<stadium>?, val context: Context)
    : RecyclerView.Adapter<stadiumAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: StadiumItemLayoutBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            binding.cardImg.setOnClickListener {
                val item = stadiumList!![adapterPosition]
                val context:Context = view.context
                val intent = Intent(context, HomeDetail::class.java)
                intent.putExtra("stadium_id",item.stadium_id.toString())
                intent.putExtra("stadium_name",item.stadium_name)
                intent.putExtra("stadium_img",item.stadium_img)
                intent.putExtra("stadium_price",item.stadium_price.toString())
                intent.putExtra("stadium_detail",item.stadium_detail)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StadiumItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        binding.stadiumid.text = "สนามที่ : " +stadiumList!![position].stadium_id
        binding.stadiumname.text = "ชื่อสนาม" +stadiumList!![position].stadium_name
        binding.stadiumprice.text = "ราคา : " +stadiumList!![position].stadium_price
        binding.stadiumdetail.text = "รายละเอียด : " +stadiumList!![position].stadium_detail
    }

    override fun getItemCount(): Int {
        return  stadiumList!!.size
    }
}