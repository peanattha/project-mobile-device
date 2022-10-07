package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadResponse(
    val error: Boolean,
    val message: String,
    val image: String,

    )
