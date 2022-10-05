package com.example.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : Fragment() {

    private lateinit var bindingHome : FragmentHomeBinding
    var stadiumList  = arrayListOf<stadium>()
    val createClient = stadiumAPI.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        bindingHome = FragmentHomeBinding.inflate(layoutInflater)
        bindingHome.recyclerView.adapter = stadiumAdapter(this.stadiumList,requireContext())
        bindingHome.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return bindingHome.root
    }

    override fun onResume() {
        super.onResume()
        callStadium()
    }

    fun callStadium(){
        stadiumList.clear();
        createClient.retrieveStadium()
            .enqueue(object : Callback<List<stadium>> {
                override fun onResponse(
                    call: Call<List<stadium>>,
                    response: Response<List<stadium>>
                ) {
                    response.body()?.forEach {//ลูปข้อมูล
                        stadiumList.add(
                            stadium(
                                it.stadium_id,
                                it.stadium_name,
                                it.stadium_price,
                                it.stadium_img,
                                it.stadium_detail
                            )
                        )//นำข้อมูลที่ลูปมาเก็บไว้
                    }
                    //// Set Data to RecyclerRecyclerView
                    bindingHome.recyclerView.adapter = stadiumAdapter(stadiumList, requireContext())
//                    bindingHome.TextPage.text = "สนามทั้งหมด : "+ stadiumList.size.toString()
                }
                override fun onFailure(call: Call<List<stadium>>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
