package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.project.databinding.ActivityManageBankBinding
import com.example.project.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class manage_bank : AppCompatActivity() {
    private lateinit var binding : ActivityManageBankBinding
    val serv = BankAdminAPI.create()
    var BankDataarrayList = arrayListOf<Bank>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callBankData()

    }

    fun callBankData(){
        BankDataarrayList.clear()
        serv.receieveBank()
            .enqueue(object : Callback<List<Bank>> {
                override fun onResponse(call: Call<List<Bank>>, response: Response<List<Bank>>) {
                    response.body()?.forEach {
                        if(it.bank_admin_id != null){
                            BankDataarrayList.add(Bank(it.bank_admin_id,it.bank_name, it.account_number,it.firstName,it.lastName,it.qr_code))
                            if(BankDataarrayList.size != 0){
                                binding.btnAddBank.setText("Edit Bank")
                                binding.editBankName.setText(BankDataarrayList[0].bank_name)
                                binding.editBankNumber.setText(BankDataarrayList[0].account_number)
                                binding.editBankAccountFirstName.setText(BankDataarrayList[0].firstName)
                                binding.editBankAccountLastName.setText(BankDataarrayList[0].lastName)
                            }
                        }
                    }

                }
                override fun onFailure(call: Call<List<Bank>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun addBankaccount(v: View) {
        if (BankDataarrayList.size != 0) {
            var bank_name = binding.editBankName.text.toString()
            var account_number =binding.editBankNumber.text.toString()
            var firstName =binding.editBankAccountFirstName.text.toString()
            var lastName =binding.editBankAccountLastName.text.toString()
            val qr_code: String = "testImage"
            if(bank_name.isEmpty() || account_number.isEmpty() || firstName.isEmpty() || lastName.isEmpty()){
                Toast.makeText(applicationContext, "Input is NULL", Toast.LENGTH_SHORT).show()
            }else{
                serv.editBankAccount(
                    BankDataarrayList[0].bank_admin_id,
                    bank_name,
                    account_number
                    ,firstName,
                    lastName,
                    qr_code)
                    .enqueue(object :Callback<Bank>{
                        override fun onResponse(call: Call<Bank>, response: Response<Bank>) {
                            if (response.isSuccessful){
                                Toast.makeText(applicationContext,"Successful", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(applicationContext,"Error onFailure ", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<Bank>, t: Throwable) {
                            Toast.makeText(applicationContext,"Error onFailure " + t.message,
                                Toast.LENGTH_LONG).show()
                        }
                    })
            }
        } else {
            val qr_code: String = "testImage"
            serv.addBankAccount(
                "",
                binding.editBankName.text.toString(),
                binding.editBankNumber.text.toString(),
                binding.editBankAccountFirstName.text.toString(),
                binding.editBankAccountLastName.text.toString(),
                qr_code
            ).enqueue(object : Callback<Bank> {
                override fun onResponse(
                    call: Call<Bank>,
                    response: Response<Bank>
                ) {
                    if (response.isSuccessful()) {
                        Toast.makeText(
                            applicationContext,
                            "Successfully",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Error ", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Bank>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
        }
    }

}