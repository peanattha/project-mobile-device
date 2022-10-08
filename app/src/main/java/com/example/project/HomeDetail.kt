package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.project.databinding.ActivityHomeDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent

class HomeDetail : AppCompatActivity() {
    private lateinit var bindingDetail: ActivityHomeDetailBinding
    var selectstart : String = ""
    var selectend : String = ""
    var searchList  = arrayListOf<search>()
    val createClient = stadiumAPI.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDetail = ActivityHomeDetailBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(bindingDetail.root)

        val sub = resources.getStringArray(R.array.TimeSelected)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, sub)
        bindingDetail.selectstarttime.setAdapter(arrayAdapter)
        bindingDetail.selectstarttime.setOnItemClickListener{ parent , _ , position,_ ->
            selectstart = parent.getItemAtPosition(position) as String

        }
        bindingDetail.selecttimeend.setAdapter(arrayAdapter)
        bindingDetail.selecttimeend.setOnItemClickListener{ parent , _ , position,_ ->
            selectend = parent.getItemAtPosition(position) as String

        }


        val std_id = intent.getStringExtra("stadium_id").toString()
        val std_name = intent.getStringExtra("stadium_name")
        val std_img = intent.getStringExtra("stadium_img")
        val std_detail = intent.getStringExtra("stadium_detail")
        val std_price = intent.getStringExtra("stadium_price")

        bindingDetail.stadiumname.text = "Name: $std_name"
        bindingDetail.stadiumdetail.text = "Detail: $std_detail"
        bindingDetail.stadiumprice.text = "Price: $std_price Bath Per 1 Hr."

        bindingDetail.btnDate.setOnClickListener {
            val newDateFragment = DatePickerFragment()
            newDateFragment.show(supportFragmentManager, "Date Picker")

        }

        bindingDetail.btnSearch.setOnClickListener {
            if(selectstart.toInt() < selectend.toInt()){
                searchList.clear();
                createClient.retrieveStadiumID(
                    std_id,
                    bindingDetail.btnDate.text.toString(),
                    bindingDetail.selectstarttime.text.toString(),
                    bindingDetail.selecttimeend.text.toString()
                ).enqueue(object : Callback<search> {
                    override fun onResponse(
                        call: Call<search>,
                        response: Response<search>
                    ) {
                        if(response.isSuccessful){
                            searchList.add(search(response.body()?.id.toString(), response.body()?.reserve_date.toString()
                                ,response.body()?.time_start.toString(),response.body()?.time_end.toString()))

                            var sum_time = bindingDetail.selecttimeend.text.toString().toInt() - bindingDetail.selectstarttime.text.toString().toInt()
                            var total_price = sum_time * std_price.toString().toInt()

                            val intent = Intent(this@HomeDetail, payment::class.java)
                            intent.putExtra("stadium_id",searchList[0].id)
                            intent.putExtra("reserve_date",searchList[0].reserve_date)
                            intent.putExtra("time_start",searchList[0].time_start)
                            intent.putExtra("time_end",searchList[0].time_end)
                            intent.putExtra("total_price",total_price.toString())
                            this@HomeDetail.startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext, "ไม่สามารถจองได้มีคนจองเเล้ว", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<search>, t: Throwable){
                        t.printStackTrace()
                        Toast.makeText(applicationContext,"Error2"+t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }else{
                Toast.makeText(applicationContext,"เลือกบ่ได้เด้อ", Toast.LENGTH_LONG).show()
            }
        }
    }
}