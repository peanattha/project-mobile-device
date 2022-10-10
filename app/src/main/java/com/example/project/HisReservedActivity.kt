package com.example.project

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.project.databinding.ActivityHisReservedBinding
import com.example.project.url.baseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HisReservedActivity : AppCompatActivity() {
    private lateinit var bindingHisResDetail: ActivityHisReservedBinding
    val serv = ReservedAPI.create()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHisResDetail = ActivityHisReservedBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(bindingHisResDetail.root)

        val payment_id = intent.getStringExtra("payment_id")
        val payment_status = intent.getStringExtra("payment_status")
        val stadium_name = intent.getStringExtra("stadium_name")
        val reserve_date = intent.getStringExtra("reserve_date")
        val reserve_date_format = reserve_date.toString().substring(0, 10)
        val time_start = intent.getStringExtra("time_start")
        val time_end = intent.getStringExtra("time_end")
        val user_id = intent.getStringExtra("user_id")
        val slip_img = intent.getStringExtra("slip_img")
        val total_price = intent.getStringExtra("total_price")
        val stadium_img = intent.getStringExtra("stadium_img")

        bindingHisResDetail.stadiumName.setText(stadium_name.toString().capitalize())
        bindingHisResDetail.date.setText(reserve_date_format)
        bindingHisResDetail.time.setText("Time: "+time_start+":00 - "+time_end+":00")
        bindingHisResDetail.price.setText("Price: "+total_price+" Bath")
        Glide.with(applicationContext).load(baseUrl+slip_img).into(bindingHisResDetail.slip)
        Glide.with(applicationContext).load(baseUrl+stadium_img).into(bindingHisResDetail.cardImg)

        val zoneId:ZoneId = ZoneId.of( "America/Montreal" );
        val today:LocalDate = LocalDate.now( zoneId );

        var simpleFormat =  DateTimeFormatter.ISO_DATE;
        var d2 = LocalDate.parse(reserve_date_format, simpleFormat)

        val mode = Context.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = applicationContext.getSharedPreferences(name, mode)
        val is_admin: String? = preferences.getString("is_admin", null)

        val btnCancel = findViewById<View>(R.id.btnCancel) as Button
        val btnSec = findViewById<View>(R.id.btnSec) as Button
        if (payment_status.toString() == "4" && is_admin == "1"){
            btnCancel.setVisibility(VISIBLE)
            btnCancel.setText("Confirme Reserved")
            btnSec.setVisibility(VISIBLE)
            btnSec.setText("Cancel Reserved")
            btnCancel.setBackgroundColor(Color.GREEN)
            btnSec.setBackgroundColor(Color.RED)
            bindingHisResDetail.btnCancel.setOnClickListener {
                val myBuilder = AlertDialog.Builder(this)
                myBuilder.apply {
                    setTitle("Warning")
                    setMessage("คุณเเน่ใจที่จะยืนยันการจอง ?")
                    setNegativeButton("Yes") { _, _ ->
                        serv.cancelReserved(payment_id.toString(), "1")
                            .enqueue(object : Callback<Reserved> {
                                override fun onResponse(
                                    call: Call<Reserved>,
                                    response: Response<Reserved>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Error onFailure ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<Reserved>, t: Throwable) {
                                    Toast.makeText(
                                        applicationContext, "Error onFailure " + t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        finish()
                    }
                    setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                    show()
                }
            }
            bindingHisResDetail.btnSec.setOnClickListener {
                val myBuilder = AlertDialog.Builder(this)
                myBuilder.apply {
                    setTitle("Warning")
                    setMessage("คุณเเน่ใจที่จะยกเลิกการจอง ?")
                    setNegativeButton("Yes") { _, _ ->
                        serv.cancelReserved(payment_id.toString(), "3")
                            .enqueue(object : Callback<Reserved> {
                                override fun onResponse(
                                    call: Call<Reserved>,
                                    response: Response<Reserved>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Error onFailure ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<Reserved>, t: Throwable) {
                                    Toast.makeText(
                                        applicationContext, "Error onFailure " + t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        finish()
                    }
                    setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                    show()
                }
            }
        }else if(payment_status.toString() == "2" && is_admin == "1"){
            btnCancel.setVisibility(VISIBLE)
            btnSec.setVisibility(INVISIBLE)
            btnCancel.setText("Confirme Cancel")
            btnCancel.setBackgroundColor(Color.RED)
            bindingHisResDetail.btnCancel.setOnClickListener {
                val myBuilder = AlertDialog.Builder(this)
                myBuilder.apply {
                    setTitle("Warning")
                    setMessage("คุณเเน่ใจที่จะยืนยันยกเลิกการจอง ?")
                    setNegativeButton("Yes") { _, _ ->
                        serv.cancelReserved(payment_id.toString(), "3")
                            .enqueue(object : Callback<Reserved> {
                                override fun onResponse(
                                    call: Call<Reserved>,
                                    response: Response<Reserved>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Error onFailure ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<Reserved>, t: Throwable) {
                                    Toast.makeText(
                                        applicationContext, "Error onFailure " + t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        finish()
                    }
                    setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                    show()
                }
            }
        }else if((d2?.compareTo(today)!! >0) && payment_status.toString() == "1" || payment_status.toString() == "4") {
            if(is_admin == "0"){
                btnCancel.setVisibility(VISIBLE)
                btnCancel.setText("Cancel")
                btnSec.setVisibility(INVISIBLE)
                btnCancel.setBackgroundColor(Color.RED)
                bindingHisResDetail.btnCancel.setOnClickListener {
                    val myBuilder = AlertDialog.Builder(this)
                    myBuilder.apply {
                        setTitle("Warning")
                        setMessage("คุณเเน่ใจที่จะยกเลิกการจอง ?")
                        setNegativeButton("Yes") { _, _ ->
                            serv.cancelReserved(payment_id.toString(), "2")
                                .enqueue(object : Callback<Reserved> {
                                    override fun onResponse(
                                        call: Call<Reserved>,
                                        response: Response<Reserved>
                                    ) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                applicationContext,
                                                "Error onFailure ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Reserved>, t: Throwable) {
                                        Toast.makeText(
                                            applicationContext, "Error onFailure " + t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            finish()
                        }
                        setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                        show()
                    }
                }
            }else{
                btnCancel.setVisibility(VISIBLE)
                btnCancel.setText("Cancel by Admin")
                btnSec.setVisibility(INVISIBLE)
                btnCancel.setBackgroundColor(Color.RED)
                bindingHisResDetail.btnCancel.setOnClickListener {
                    val myBuilder = AlertDialog.Builder(this)
                    myBuilder.apply {
                        setTitle("Warning")
                        setMessage("คุณเเน่ใจที่จะยกเลิกการจอง ?")
                        setNegativeButton("Yes") { _, _ ->
                            serv.cancelReserved(payment_id.toString(), "3")
                                .enqueue(object : Callback<Reserved> {
                                    override fun onResponse(
                                        call: Call<Reserved>,
                                        response: Response<Reserved>
                                    ) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                applicationContext,
                                                "Error onFailure ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Reserved>, t: Throwable) {
                                        Toast.makeText(
                                            applicationContext, "Error onFailure " + t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            finish()
                        }
                        setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                        show()
                    }
                }
            }
        }else{
            btnCancel.setVisibility(INVISIBLE)
            btnSec.setVisibility(INVISIBLE)
        }
    }

}