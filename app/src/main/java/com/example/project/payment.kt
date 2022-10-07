package com.example.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.project.databinding.ActivityPaymentBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class payment : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    private var selectedImageUri: Uri? = null
    val createClient = paymentAPI.invoke()
    //    val serv = paymentAPI.create()
    var paymentList = arrayListOf<Bank>()
    var stadiumlist = arrayListOf<stadium>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener(View.OnClickListener {
            openImageChooser()
        })
        binding.buttonUpload.setOnClickListener{
            uploadImage()
        }
        val stadium_id = intent.getStringExtra("stadium_id")
        val reserve_date = intent.getStringExtra("reserve_date")
        val time_start = intent.getStringExtra("time_start")
        val time_end = intent.getStringExtra("time_end")
        val total_price = intent.getStringExtra("total_price")

        Toast.makeText(applicationContext, total_price, Toast.LENGTH_SHORT).show()

        binding.stadiumReserve.setText("stadium " + stadium_id)
        binding.dateReserve.setText("วันที่จอง : " + reserve_date)
        binding.startReserve.setText("เวลา : " + time_start+".00 - "+time_end+".00")
//        binding.endReserve.setText(time_end+".00")
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

                    binding.accountnumber.setText("เลขบัญชี : " + paymentList[0].account_number)
                    binding.bankaccount.setText("ธนาคาร : " + paymentList[0].bank_name)
                    binding.accountname.setText("ชื่อ : " + paymentList[0].firstName+" "+paymentList[0].lastName)
                    Glide.with(applicationContext).load(paymentList[0].qr_code).into(binding.qrCode)


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
                    binding.slipuser.setImageURI(selectedImageUri)
                }
            }
        }
    }
    private fun uploadImage() {
        val mode = Context.MODE_PRIVATE
        val name = "sign_in_preferences"
        val preferences = applicationContext.getSharedPreferences(name, mode)
        val user_id: String? = preferences.getString("user_id", null)

        if (selectedImageUri == null) {
            binding.root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image")
        paymentAPI().insertpay(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), paymentList[0].bank_admin_id),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),4.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), 1.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_id.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), intent.getStringExtra("stadium_id").toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), intent.getStringExtra("total_price").toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), intent.getStringExtra("reserve_date").toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), intent.getStringExtra("time_start").toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), intent.getStringExtra("time_end").toString())
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.root.snackbar(t.message!!)
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    binding.root.snackbar(it.message)
//                    progress_bar.progress = 100
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

    }
}
