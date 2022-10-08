package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityCancelReservedAdminBinding
import com.example.project.databinding.ActivityReservedAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class cancel_reserved_admin : AppCompatActivity() {
    private lateinit var binding : ActivityCancelReservedAdminBinding
    var reservedListAdmin = arrayListOf<Reserved>()
    val serv = ReservedAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelReservedAdminBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.recyclerView.adapter = ReservedAdapter(this.reservedListAdmin,applicationContext)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        reservedListAdmin.clear()
        super.onResume()
        callReservedData()
    }

    fun callReservedData(){
        reservedListAdmin.clear()
        serv.reserved_admin_cancel()
            .enqueue(object : Callback<List<Reserved>> {
                override fun onResponse(call: Call<List<Reserved>>, response: Response<List<Reserved>>) {
                    response.body()?.forEach {
                        reservedListAdmin.add(Reserved(it.reserve_id, it.user_id
                            ,it.total_price,it.reserve_date,it.time_start,it.time_end,it.slip_img
                            ,it.payment_status,it.stadium_name,it.stadium_img,it.payment_id))
                    }
                    binding.recyclerView.adapter = ReservedAdapter(reservedListAdmin,applicationContext)
                }
                override fun onFailure(call: Call<List<Reserved>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}