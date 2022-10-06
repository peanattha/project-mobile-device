package com.example.project

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.project.databinding.ActivityEditStadiumBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class edit_stadium : AppCompatActivity() {
    private lateinit var bindingEditStadium: ActivityEditStadiumBinding
    val stadiumApi = stadiumAPI.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEditStadium = ActivityEditStadiumBinding.inflate(layoutInflater)
        setContentView(bindingEditStadium.root)

        bindingEditStadium.edtId.setText(intent.getStringExtra("idData").toString())
        bindingEditStadium.edtId.isEnabled = false
        bindingEditStadium.edtName.setText(intent.getStringExtra("nameData"))
        bindingEditStadium.edtPrice.setText(intent.getStringExtra("priceData").toString())
        bindingEditStadium.edtDetail.setText(intent.getStringExtra("detailData"))

//
    }

    fun saveStadium(v: View) {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = Date()
        val currentDate = formatter.format(date)
        stadiumApi.updateStadium(
            bindingEditStadium.edtId.text.toString(),
            bindingEditStadium.edtName.text.toString(),
            bindingEditStadium.edtPrice.text.toString().toInt(),
            bindingEditStadium.edtImg.text.toString(),
            bindingEditStadium.edtDetail.text.toString(),
            currentDate
        ).enqueue(object : Callback<stadium> {
            override fun onResponse(call: Call<stadium>, response: Response<stadium>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext, "Update successful",
                        Toast.LENGTH_LONG
                    ).show()

                    val intentt = Intent(applicationContext, stadium_detail::class.java)
                    intentt.putExtra("idData", bindingEditStadium.edtId.text.toString())
                    intentt.putExtra("nameData", bindingEditStadium.edtName.text.toString())
                    intentt.putExtra("priceData", bindingEditStadium.edtPrice.text.toString())
                    intentt.putExtra("detailData", bindingEditStadium.edtDetail.text.toString())
                    finish()
                    startActivity(intentt)
                } else {
                    Toast.makeText(
                        applicationContext, " Update Failure",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<stadium>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Error onFailure " + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}