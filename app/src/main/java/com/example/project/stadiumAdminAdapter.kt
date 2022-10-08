package com.example.project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.StadiumItemLayoutBinding
import com.example.project.url.baseUrl


class stadiumAdminAdapter (val stadiumList : List<stadium>, val context: Context)
    : RecyclerView.Adapter<stadiumAdminAdapter.ViewHolder>() {

    inner class ViewHolder(view: View, val binding: StadiumItemLayoutBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            binding.cardImg.setOnClickListener {
                val item = stadiumList[adapterPosition]
                val contextView:Context = view.context
                val intent = Intent(contextView, stadium_detail::class.java)
                intent.putExtra("idData",item.stadium_id.toString())
                intent.putExtra("nameData",item.stadium_name)
                intent.putExtra("priceData",item.stadium_price.toString())
                intent.putExtra("detailData",item.stadium_detail)
                intent.putExtra("imgData",item.stadium_img)
                contextView.startActivity(intent)
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
        Glide.with(context).load(baseUrl +stadiumList!![position].stadium_img).into(binding.cardImg)
        binding.stadiumname.text = stadiumList!![position].stadium_name
        binding.stadiumprice.text = "Price : " +stadiumList!![position].stadium_price+" Bath Per 1 Hr. "
        binding.stadiumdetail.text = "Details : " +stadiumList!![position].stadium_detail
    }

    override fun getItemCount(): Int {
        return  stadiumList!!.size
    }
}