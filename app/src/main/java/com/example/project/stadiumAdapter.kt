package com.example.project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        Glide.with(context).load(stadiumList!![position].stadium_img).into(binding.cardImg)
        binding.stadiumname.text = stadiumList!![position].stadium_name
        binding.stadiumprice.text = "Price : " +stadiumList!![position].stadium_price+" Bath Per 1 Hr. "
        binding.stadiumdetail.text = "Details : " +stadiumList!![position].stadium_detail
    }

    override fun getItemCount(): Int {
        return  stadiumList!!.size
    }
}