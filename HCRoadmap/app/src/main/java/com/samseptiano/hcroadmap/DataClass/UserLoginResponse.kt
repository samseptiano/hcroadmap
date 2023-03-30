package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @field:SerializedName("token")
     val token: String? = null
)