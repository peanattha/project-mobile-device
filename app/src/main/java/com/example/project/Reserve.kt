package com.example.project

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.FragmentReserveBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Reserve : Fragment() {
    private lateinit var bindingReserved : FragmentReserveBinding
    var reservedList = arrayListOf<Reserved>()
    val serv = ReservedAPI.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        bindingReserved = FragmentReserveBinding.inflate(layoutInflater)
        bindingReserved.recyclerView.adapter = ReservedAdapter(this.reservedList,requireContext())
        bindingReserved.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return bindingReserved.root
    }

    override fun onResume() {
        reservedList.clear()
        super.onResume()
        callReservedData()
    }

    fun callReservedData(){
        val mode = Context.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = requireContext().getSharedPreferences(name, mode)
        val user_id: String? = preferences.getString("user_id", null)
        serv.reserved_his(user_id.toString()) //user id
            .enqueue(object : Callback<List<Reserved>> {
                override fun onResponse(call: Call<List<Reserved>>, response: Response<List<Reserved>>) {
                    response.body()?.forEach {
                        reservedList.add(Reserved(it.reserve_id, it.user_id
                            ,it.total_price,it.reserve_date,it.time_start,it.time_end,it.slip_img
                            ,it.payment_status,it.stadium_name,it.stadium_img,it.payment_id))
                    }
                    bindingReserved.recyclerView.adapter = ReservedAdapter(reservedList,requireContext())
                }
                override fun onFailure(call: Call<List<Reserved>>, t: Throwable) {
                    Toast.makeText(requireContext(),"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}