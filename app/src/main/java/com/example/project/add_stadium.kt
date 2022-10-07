package com.example.project

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.project.databinding.ActivityAddStadiumBinding
import com.example.project.databinding.ActivityManageStadiumBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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

class add_stadium : AppCompatActivity() {
    private lateinit var bindingAddStadium : ActivityAddStadiumBinding
    private var selectedImageUri: Uri? = null
    val stadiumApi = stadiumAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAddStadium = ActivityAddStadiumBinding.inflate(layoutInflater)
        setContentView(bindingAddStadium.root)

        val loadImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                bindingAddStadium.imageView.setImageURI(it)
                selectedImageUri = it
            })
        bindingAddStadium.btnSelect.setOnClickListener(View.OnClickListener {
//            loadImage.launch("image/*")
            openImageChooser()
        })
        bindingAddStadium.btnAdd.setOnClickListener{
            addStadium()
        }
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
                    bindingAddStadium.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

//    private fun uploadImage() {
//
//        val stadium_name = bindingAddStadium.addName.text.toString()
//        val stadium_price = bindingAddStadium.addPrice.text.toString()
//        val stadium_detail = bindingAddStadium.addDetail.text.toString()
//
//        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//        val date = Date()
//        val currentDate = formatter.format(date)
//
//        if (selectedImageUri == null) {
//            bindingAddStadium.root.snackbar("Select an Image First")
//            return
//        }
//
//        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
//
//        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//
////        progress_bar.progress = 0
//        val body = UploadRequestBody(file, "image", this)
//        MyAPI().add(
//            MultipartBody.Part.createFormData(
//                "image",
//                file.name,
//                body
//            ),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stadium_name),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stadium_price),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stadium_detail),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), currentDate)
//
//        ).enqueue(object : Callback<UploadResponse> {
//            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
////                binding.root.snackbar(t.message!!)
//                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
////                progress_bar.progress = 0
//            }
//
//            override fun onResponse(
//                call: Call<UploadResponse>,
//                response: Response<UploadResponse>
//            ) {
//                response.body()?.let {
//                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//
//    }

    private fun addStadium() {
        val stadium_name = bindingAddStadium.addName.text.toString()
        val stadium_price = bindingAddStadium.addPrice.text.toString()
        val stadium_detail = bindingAddStadium.addDetail.text.toString()

        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = Date()
        val currentDate = formatter.format(date)

        if (selectedImageUri == null) {
            bindingAddStadium.root.snackbar("Select an Image First")
            return
        }
        else{
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
            val myBuilder = AlertDialog.Builder(this)
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
//        progress_bar.progress = 0
            val body = UploadRequestBody(file, "image")

            myBuilder.apply {
                setTitle("Confirm")
                setMessage("Do you want to add the stadium?")
                setNegativeButton("Confirm") { _ , _ ->

                    stadiumApi.add(
                        MultipartBody.Part.createFormData(
                            "image",
                            file.name,
                            body
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stadium_name),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stadium_price),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stadium_detail),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), currentDate)

                    ).enqueue(object : Callback<UploadResponse> {
                        override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                binding.root.snackbar(t.message!!)
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
//                progress_bar.progress = 0
                        }

                        override fun onResponse(
                            call: Call<UploadResponse>,
                            response: Response<UploadResponse>
                        ) {
                            response.body()?.let {
                                Toast.makeText(applicationContext,  it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                    finish()
                }
                setPositiveButton("Cancle"){ dialog,_->dialog.cancel()}
                show()
            }
        }

    }

}

