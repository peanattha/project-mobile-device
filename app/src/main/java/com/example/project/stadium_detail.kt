package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.project.databinding.ActivityStadiumDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class stadium_detail : AppCompatActivity() {
    private lateinit var bindingStadiumDetail : ActivityStadiumDetailBinding
    val stadiumApi = stadiumAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bindingStadiumDetail = ActivityStadiumDetailBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView( bindingStadiumDetail.root)

        val id = intent.getStringExtra("idData").toString()
        val name = intent.getStringExtra("nameData")
        val price = intent.getStringExtra("priceData").toString()
        val detail =intent.getStringExtra("detailData")


        bindingStadiumDetail.stadiumIdDPage.text = "ไอดีสนาม : ${id}"
        bindingStadiumDetail.stadiumNameDPage.text = "ชื่อสนาม : ${name}"
        bindingStadiumDetail.stadiumPriceDPage.text = "ราคา : ${price}/2 ชม."
        bindingStadiumDetail.stadiumDetailDPage.text = "รายละเอียด : ${detail}"

    }


    fun deleteStadium(v: View) {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = Date()
        val currentDate = formatter.format(date)
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.apply {
            setTitle("Warning")
            setMessage("Do you want to delete the stadium?")
            setNegativeButton("Yes") { _ , _ ->
                stadiumApi.softDeleteStadium(
                    intent.getStringExtra("idData").toString(),
                    currentDate
                ).enqueue(object : Callback<stadium> {
                    override fun onResponse(call: Call<stadium>, response: Response<stadium>) {
                        if (response.isSuccessful) {
                            Toast.makeText( applicationContext, "Delete successful",
                                Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(applicationContext, " Delete Failure",
                                Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    override fun onFailure(call: Call<stadium>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error onFailure " + t.message,
                            Toast.LENGTH_LONG).show()
                    }
                })
            }
            setPositiveButton("No"){ dialog,_->dialog.cancel()}
            show()
        }
    }

    //เปิดหน้าแก้ไขสนาม
    fun editStadiumPage(v: View) {
        val intentt = Intent(applicationContext, edit_stadium::class.java)
        intentt.putExtra("idData",intent.getStringExtra("idData").toString())
        intentt.putExtra("nameData",intent.getStringExtra("nameData"))
        intentt.putExtra("priceData",intent.getStringExtra("priceData").toString())
        intentt.putExtra("detailData",intent.getStringExtra("detailData"))
        startActivity(intentt)
        finish()
    }
}