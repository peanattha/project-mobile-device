package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.project.databinding.ActivityHomeDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDetail : AppCompatActivity() {
    private lateinit var bindingDetail: ActivityHomeDetailBinding
    var selectstart : String = ""
    var selectend : String = ""
    var stadiumList  = arrayListOf<stadium>()
    val createClient = stadiumAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDetail = ActivityHomeDetailBinding.inflate(layoutInflater)
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
        bindingDetail.stadiumprice.text = "Price: $std_price Per 1 HR."

        bindingDetail.btnDate.setOnClickListener {
            val newDateFragment = DatePickerFragment()
            newDateFragment.show(supportFragmentManager, "Date Picker")
        }

        bindingDetail.btnSearch.setOnClickListener {
            Toast.makeText(applicationContext, ""+std_id, Toast.LENGTH_SHORT).show()
            if(selectstart.toInt() < selectend.toInt()){
                stadiumList.clear();
                createClient.retrieveStadiumID(
                    std_id,
                    bindingDetail.btnDate.text.toString(),
                    bindingDetail.selectstarttime.text.toString(),
                    bindingDetail.selecttimeend.text.toString()
                ).enqueue(object : Callback<List<search>> {
                    override fun onResponse(
                        call: Call<List<search>>,
                        response: Response<List<search>>
                    ) {}
                    override fun onFailure(call: Call<List<search>>, t: Throwable){
                        t.printStackTrace()
                        Toast.makeText(applicationContext,"Error2", Toast.LENGTH_LONG).show()
                    }
                })
            }else{
                Toast.makeText(applicationContext,"เลือกบ่ได้เด้อ", Toast.LENGTH_LONG).show()
            }
            var sum_time = bindingDetail.selecttimeend.text.toString().toInt() - bindingDetail.selectstarttime.text.toString().toInt()
            var total_price = sum_time * std_price.toString().toInt()
        }
    }
}