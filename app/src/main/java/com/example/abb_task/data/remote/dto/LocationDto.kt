package com.example.abb_task.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
