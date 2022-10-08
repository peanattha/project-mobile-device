package com.example.project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.example.project.databinding.ActivityAdminMainBinding
import com.example.project.databinding.ActivityLoginBinding
import com.example.project.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    val serv = UserAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(applicationContext, register::class.java))
        }

    }

    fun login(v: View){

        if(binding.emailEDTT.text!!.isEmpty() || binding.passwordEDTT.text!!.isEmpty()){
            Toast.makeText(applicationContext,"Email or password wrong or not set",
                Toast.LENGTH_SHORT).show()
        }

        serv.retrieveUser(binding.emailEDTT.text.toString(), binding.passwordEDTT.text.toString())
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        val mode = Context.MODE_PRIVATE
                        val name = "sign_in_preferences"
                        val preferences = applicationContext.getSharedPreferences(name, mode)

                        //      Write
                        preferences.edit {
                            putString("loginStatus", "Yes")
                        }
                        preferences.edit {
                            putString("user_id", response.body()?.user_id.toString())
                        }
                        preferences.edit {
                            putString("is_admin", response.body()?.is_admin.toString())
                        }

                        val is_admin: String? = preferences.getString("is_admin", null)

                        if(is_admin == "1"){
                            startActivity(Intent(applicationContext, admin_main::class.java))
                            Toast.makeText(applicationContext, "Login Admin", Toast.LENGTH_SHORT).show()
                        }else{
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            Toast.makeText(applicationContext, "Login User", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(applicationContext,"User not in Database",
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

}