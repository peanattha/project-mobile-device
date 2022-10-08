package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.Global.putString
import android.text.method.TextKeyListener.clear
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.project.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val mode = Context.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = applicationContext.getSharedPreferences(name, mode)

        val loginStatus: String? = preferences.getString("loginStatus", null)
        if (loginStatus == null){
            startActivity(Intent(this@MainActivity, login::class.java))
        }

        val is_admin: String? = preferences.getString("is_admin", null)

        if(is_admin == "1"){
            startActivity(Intent(applicationContext, admin_main::class.java))
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                binding.frameLayout.id,
                Home()
            ).commit()
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.reserve -> replaceFragment(Reserve())
                R.id.profile -> replaceFragment(Profile())
                else ->{

                }
            }
            true
        }

    }
    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}