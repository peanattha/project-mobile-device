package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.project.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    val isAdmin :String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
    }

    fun ADD(view: View) {
        var mail = binding.resMail.text.toString()
        var pass =binding.resPassword.text.toString()
        var fname =binding.resName.text.toString()
        var lname =binding.resSurname.text.toString()
        var tel =binding.resPhonenumber.text.toString()

        if(fname.isEmpty()){
            Toast.makeText(applicationContext,"Enter First Name",
                Toast.LENGTH_SHORT).show()
        }else if(lname.isEmpty()) {
            Toast.makeText(applicationContext, "Enter SurName",
                Toast.LENGTH_SHORT).show()
        }else if(mail.isEmpty()){
            Toast.makeText(applicationContext,"Enter E-mail",
                Toast.LENGTH_SHORT).show()
        }else if(tel.isEmpty()){
            Toast.makeText(applicationContext,"Enter Phone Number",
                Toast.LENGTH_SHORT).show()
        }else if(pass.isEmpty()){
            Toast.makeText(applicationContext,"Enter password",
                Toast.LENGTH_SHORT).show()
        }else if(tel.isNotEmpty()){
            if(tel.length != 10){
                Toast.makeText(applicationContext,"phone number is not correct. ",
                    Toast.LENGTH_LONG).show()
            }else{
                val api: RegisterAPI = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RegisterAPI::class.java)
                api.insertUser(
                    binding.resMail.text.toString(),
                    binding.resPassword.text.toString(),
                    binding.resName.text.toString(),
                    binding.resSurname.text.toString(),
                    binding.resPhonenumber.text.toString(),
                    isAdmin
                ).enqueue(object : Callback<RegisterData> {
                    override fun onResponse(
                        call: Call<RegisterData>,
                        response: retrofit2.Response<RegisterData>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(applicationContext,"Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Error ", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}