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
import com.bumptech.glide.Glide
import com.example.project.databinding.ActivityEditStadiumBinding
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

class edit_stadium : AppCompatActivity() {
    private lateinit var bindingEditStadium : ActivityEditStadiumBinding
    val stadiumApi = stadiumAPI.create()
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEditStadium = ActivityEditStadiumBinding.inflate(layoutInflater)
        setContentView(bindingEditStadium.root)

        val loadImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                bindingEditStadium.imageView.setImageURI(it)
                selectedImageUri = it
            })

        Glide.with(applicationContext).load(intent.getStringExtra("imgData")).into(bindingEditStadium.imageView)
        bindingEditStadium.edtId.setText (intent.getStringExtra("idData").toString())
        bindingEditStadium.edtId.isEnabled=false
        bindingEditStadium.edtName.setText (intent.getStringExtra("nameData"))
        bindingEditStadium.edtPrice.setText (intent.getStringExtra("priceData").toString())
        bindingEditStadium.edtDetail.setText (intent.getStringExtra("detailData"))

        bindingEditStadium.btnSelect.setOnClickListener(View.OnClickListener {
//            loadImage.launch("image/*")
            openImageChooser()
        })
        bindingEditStadium.btnSave.setOnClickListener{
            saveStadium()
        }
//
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
                    bindingEditStadium.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun saveStadium() {
        val stadium_name = bindingEditStadium.edtName.text.toString()
        val stadium_price = bindingEditStadium.edtPrice.text.toString()
        val stadium_detail = bindingEditStadium.edtDetail.text.toString()

        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = Date()
        val currentDate = formatter.format(date)

//        if (selectedImageUri == null) {
//            bindingEditStadium.root.snackbar("Select an Image First")
//            return
//        }
//
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
            setMessage("Do you want to save?")
            setNegativeButton("Confirm") { _ , _ ->

                stadiumApi.updateStadium(
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        body
                    ),
                    bindingEditStadium.edtId.text.toString(),
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