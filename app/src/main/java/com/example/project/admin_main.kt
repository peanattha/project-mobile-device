package com.example.project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.example.project.databinding.ActivityAdminMainBinding


class admin_main : AppCompatActivity() {
    private lateinit var binding : ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            val mode = MODE_PRIVATE
            val name = "sign_in_preferences"
            val preferences = applicationContext.getSharedPreferences(name, mode)
            preferences.edit {
                clear()
            }
            Toast.makeText(applicationContext, "Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, login::class.java))
        }

        binding.reservedAdmin.setOnClickListener {
            startActivity(Intent(applicationContext, reserved_admin::class.java))
        }

        binding.cancelReserved.setOnClickListener {
            startActivity(Intent(applicationContext, cancel_reserved_admin::class.java))
        }

        binding.confirmReserved.setOnClickListener {
            startActivity(Intent(applicationContext, confirme_reserved::class.java))
        }
        binding.manageBank.setOnClickListener {
            startActivity(Intent(applicationContext, manage_bank::class.java))
        }
        binding.manageStadium.setOnClickListener {
            startActivity(Intent(applicationContext, manage_stadium::class.java))
        }

    }

}