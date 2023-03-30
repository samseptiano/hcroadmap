package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @field:SerializedName("username")
     val username: String? = null,

    @field:SerializedName("password")
     val password: String? = null

)