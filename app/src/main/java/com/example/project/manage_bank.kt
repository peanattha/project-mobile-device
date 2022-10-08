package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.project.databinding.ActivityManageBankBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.example.project.url.baseUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
//import com.example.imageuploader.databinding.ActivityMainBinding
//import com.example.testuploadimg.BankAdminAPI
//import com.example.testuploadimg.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class manage_bank : AppCompatActivity() {
    private lateinit var binding: ActivityManageBankBinding
    private var selectedImageUri: Uri? = null
    val serv = BankAdminAPI.create()

    var BankDataarrayList = arrayListOf<Bank>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBankBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        callBankData()

        binding.uploadSlip.setOnClickListener(View.OnClickListener {
            openImageChooser()
        })
        binding.btnAddBank.setOnClickListener {
            uploadImage()
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
                    binding.qrCode.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage() {
        if (BankDataarrayList.size != 0) {
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

            val bank_admin_id = BankDataarrayList[0].bank_admin_id
            var bank_name = binding.editBankName.text.toString()
            var account_number = binding.editBankNumber.text.toString()
            var firstName = binding.editBankAccountFirstName.text.toString()
            var lastName = binding.editBankAccountLastName.text.toString()

            if (bank_name.isEmpty() || account_number.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(applicationContext, "Input is NULL", Toast.LENGTH_SHORT).show()
            } else {
                serv.editBankAccount(
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        body
                    ),
                    bank_admin_id,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), bank_name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), account_number),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), firstName),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lastName)
                ).enqueue(object : Callback<UploadResponse> {
                    override fun onResponse(
                        call: Call<UploadResponse>,
                        response: Response<UploadResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Successful", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error onFailure ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, "Error onFailure " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        } else {
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

            serv.addBankAccount(
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    body
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    binding.editBankName.text.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    binding.editBankNumber.text.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    binding.editBankAccountFirstName.text.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    binding.editBankAccountLastName.text.toString()
                )
            ).enqueue(object : Callback<UploadResponse> {
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
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

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
        }
    }

    fun callBankData() {
        BankDataarrayList.clear()
        serv.receieveBank()
            .enqueue(object : Callback<List<Bank>> {
                override fun onResponse(call: Call<List<Bank>>, response: Response<List<Bank>>) {
                    response.body()?.forEach {
                        if (it.bank_admin_id != null) {
                            BankDataarrayList.add(
                                Bank(
                                    it.bank_admin_id,
                                    it.bank_name,
                                    it.account_number,
                                    it.firstName,
                                    it.lastName,
                                    it.qr_code
                                )
                            )
                            if (BankDataarrayList.size != 0) {
                                binding.btnAddBank.setText("Edit Bank")
                                binding.editBankName.setText(BankDataarrayList[0].bank_name)
                                binding.editBankNumber.setText(BankDataarrayList[0].account_number)
                                binding.editBankAccountFirstName.setText(BankDataarrayList[0].firstName)
                                binding.editBankAccountLastName.setText(BankDataarrayList[0].lastName)
                                Glide.with(applicationContext).load(baseUrl+BankDataarrayList[0].qr_code)
                                    .into(binding.qrCode)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<List<Bank>>, a: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + a.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}





