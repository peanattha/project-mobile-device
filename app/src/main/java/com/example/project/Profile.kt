package com.example.project

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.project.databinding.FragmentProfileBinding
import com.example.project.databinding.FragmentReserveBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : Fragment() {

    private lateinit var bindingProfile : FragmentProfileBinding
    val serv = UserAPI.create()
    var userList = arrayListOf<User>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingProfile = FragmentProfileBinding.inflate(layoutInflater)
        bindingProfile.btnEdit.setOnClickListener {
            var mail = bindingProfile.email.text.toString()
            var fname =bindingProfile.FirstName.text.toString()
            var lname =bindingProfile.LastName.text.toString()
            var tel =bindingProfile.tel.text.toString()
            if(mail.isEmpty() || fname.isEmpty() || lname.isEmpty() || tel.isEmpty()){
                Toast.makeText(context, "Input is NULL", Toast.LENGTH_SHORT).show()
            }else{
                serv.editUser(userList[0].user_id,
                    mail,
                    fname,
                    lname,
                    tel)
                    .enqueue(object :Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful){
                                Toast.makeText(context,"isSuccessful", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(context,"Email Duplicate", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(context,"Error onFailure " + t.message,
                                Toast.LENGTH_LONG).show()
                        }
                    })
            }

        }
        bindingProfile.btnLogout.setOnClickListener {
            val mode = Context.MODE_PRIVATE
            val name = "sign_in_preferences"
            val preferences = requireContext().getSharedPreferences(name, mode)
            preferences.edit {
                clear()
            }
            Toast.makeText(context, "logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), login::class.java))
        }

        return bindingProfile.root
    }

    override fun onResume() {
        userList.clear()
        super.onResume()
        val mode = AppCompatActivity.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = requireContext().getSharedPreferences(name, mode)
        val loginStatus: String? = preferences.getString("loginStatus", null)
        if(loginStatus == null){
            startActivity(Intent(context, login::class.java))
        }else{
            callUserData()
        }
    }

    fun callUserData(){
        val mode = Context.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = requireContext().getSharedPreferences(name, mode)
        val user_id: String? = preferences.getString("user_id", null)
        serv.userDetail(user_id.toString()) //user id
            .enqueue(object :Callback<List<User>>{
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful){
                        response.body()?.forEach {
                            userList.add(User(it.user_id, it.user_email
                                ,it.user_password,it.user_firstName,it.user_lastName,it.user_tel,it.is_admin))
                        }
                        bindingProfile.FirstName.setText(userList[0].user_firstName)
                        bindingProfile.LastName.setText(userList[0].user_lastName)
                        bindingProfile.email.setText(userList[0].user_email)
                        bindingProfile.tel.setText(userList[0].user_tel)
                    }else{
                        Toast.makeText(context,"Error onFailure ", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(context,"Error onFailure " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })

    }


}