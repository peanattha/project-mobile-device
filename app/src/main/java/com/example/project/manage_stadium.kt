package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityManageStadiumBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class manage_stadium : AppCompatActivity() {
    private lateinit var binding : ActivityManageStadiumBinding
    var stadiumList = arrayListOf<stadium>()
    val stadiumApi = stadiumAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageStadiumBinding.inflate(layoutInflater)
        setContentView(binding.root )

        binding.stadiumRecycleView.layoutManager = LinearLayoutManager(applicationContext)
        binding.stadiumRecycleView.addItemDecoration(
            DividerItemDecoration(binding.stadiumRecycleView.context,
                DividerItemDecoration.VERTICAL) )
    }
    override fun onResume() {
        super.onResume()
        callStadiumData()
    }

    fun callStadiumData(){
        stadiumList.clear()
        stadiumApi.retrieveStadium()
            .enqueue(object : Callback<List<stadium>> {
                override fun onResponse(
                    call: Call<List<stadium>>,
                    response: Response<List<stadium>>
                ) {
                    response.body()?.forEach {
                        stadiumList.add(stadium(it.stadium_id, it.stadium_name, it.stadium_price, it.stadium_img,it.stadium_detail))
                    }
                    binding.stadiumRecycleView.adapter = stadiumAdminAdapter(stadiumList, applicationContext)
                }

                override fun onFailure(call: Call<List<stadium>>, t: Throwable){
                    t.printStackTrace()
                    Toast.makeText(applicationContext,"Error", Toast.LENGTH_LONG).show()
                }
            })
    }
    fun addStadiumPage(v: View) {
        val intent = Intent(applicationContext, add_stadium::class.java)
        startActivity(intent)
    }

}