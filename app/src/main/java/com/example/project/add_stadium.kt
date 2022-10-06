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
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

class add_stadium : AppCompatActivity() {
    private lateinit var bindingAddStadium : ActivityAddStadiumBinding
    private var selectedImageUri: Uri? = null
    val createClient = stadiumAPI.create()

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
        bindingAddStadium.imageView.setOnClickListener(View.OnClickListener {
//            loadImage.launch("image/*")
            openImageChooser()
        })
//        bindingAddStadium.buttonUpload.setOnClickListener{
//            uploadImage()
//        }
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
    fun addStadium(v: View) {


//        if (selectedImageUri == null) {
//            bindingAddStadium.root.snackbar("Select an Image First")
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
////        progress_bar.progress = 0
//        val body = UploadRequestBody(file, "image", this)
//        MyAPI().uploadImage(
//            MultipartBody.Part.createFormData("image", file.name, body),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json"),
//        ).enqueue(object : Callback<UploadResponse> {
//            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                bindingAddStadium.root.snackbar(t.message!!)
////                progress_bar.progress = 0
//            }
//
//            override fun onResponse(
//                call: Call<UploadResponse>,
//                response: Response<UploadResponse>
//            ) {
//                response.body()?.let {
//                    bindingAddStadium.root.snackbar(it.message)
////                    progress_bar.progress = 100
//                }
//            }
//        })


        val myBuilder = AlertDialog.Builder(this)
        myBuilder.apply {
            setTitle("Confirm")
            setMessage("Do you want to add the stadium?")
            setNegativeButton("Confirm") { _ , _ ->
                val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val date = Date()
                val currentDate = formatter.format(date)

                createClient.insertStadium(
                    bindingAddStadium.addName.text.toString(),
                    bindingAddStadium.addPrice.text.toString().toInt(),
                    bindingAddStadium.addImg.text.toString(),
                    bindingAddStadium.addDetail.text.toString(),
                    currentDate
                ).enqueue(object : Callback<stadium> {
                    override fun onResponse(
                        call: Call<stadium>,
                        response: retrofit2.Response<stadium>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(applicationContext,"Add successful", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Error onFailure", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<stadium>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
            setPositiveButton("Cancle"){ dialog,_->dialog.cancel()}
            show()
        }
    }
}