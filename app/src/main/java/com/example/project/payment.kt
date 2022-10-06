package com.example.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.project.databinding.ActivityPaymentBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class payment : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    private var selectedImageUri: Uri? = null
    val createClient = paymentAPI.invoke()
    var paymentList = arrayListOf<Bank>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener(View.OnClickListener {
            openImageChooser()
        })
        binding.buttonUpload.setOnClickListener{
//            uploadImage()
        }
        val stadium_id = intent.getStringExtra("stadium_id")
        val reserve_date = intent.getStringExtra("reserve_date")
        val time_start = intent.getStringExtra("time_start")
        val time_end = intent.getStringExtra("time_end")
        val total_price = intent.getStringExtra("total_price")

        Toast.makeText(applicationContext, total_price, Toast.LENGTH_SHORT).show()

        binding.stadiumReserve.setText(stadium_id)
        binding.dateReserve.setText(reserve_date)
        binding.startReserve.setText(time_start)
        binding.endReserve.setText(time_end)
        binding.buttonUpload.text= "ชำระเงิน "+total_price+" บาท"

        val mode = Context.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = applicationContext.getSharedPreferences(name, mode)
        val user_id: String? = preferences.getString("user_id", null)


    }

    override fun onResume() {
        super.onResume()
        callpayment()
    }

    fun callpayment(){
        paymentList.clear()
        createClient.callpayment()
            .enqueue(object : Callback<List<Bank>> {
                override fun onResponse(call: Call<List<Bank>>, response: Response<List<Bank>>) {
                    response.body()?.forEach {
                        paymentList.add(Bank(it.bank_admin_id, it.bank_name
                            ,it.account_number,it.firstName,it.lastName,it.qr_code))
                    }

                    binding.accountnumber.setText(paymentList[0].account_number)
                    binding.bankaccount.setText(paymentList[0].bank_name)
                    binding.accountname.setText(paymentList[0].firstName+" "+paymentList[0].lastName)
                }
                override fun onFailure(call: Call<List<Bank>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.qrCode.setImageURI(selectedImageUri)
                }
            }
        }
    }

//    private fun uploadImage() {
//        if (selectedImageUri == null) {
//            binding.root.snackbar("Select an Image First")
//            return
//        }
//
//        val parcelFileDescriptor =
//            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
//
//        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//
//        val body = UploadRequestBody(file, "image")
//        createClient.uploadImage(
//            MultipartBody.Part.createFormData(
//                "image",
//                file.name,
//                body
//            ),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
//        ).enqueue(object : Callback<UploadResponse> {
//            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                binding.root.snackbar(t.message!!)
//            }
//
//            override fun onResponse(
//                call: Call<UploadResponse>,
//                response: Response<UploadResponse>
//            ) {
//                response.body()?.let {
//                    binding.root.snackbar(it.message)
////                    progress_bar.progress = 100
//                }
//            }
//        })
//    }
}