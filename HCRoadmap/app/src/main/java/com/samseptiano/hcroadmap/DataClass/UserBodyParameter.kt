package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class UserBodyParameter(
    @field:SerializedName("userId")
     val userId: String? = null,

    @field:SerializedName("password")
     val password: String? = null,

    @field:SerializedName("firebaseToken")
     val firebaseToken: String? = null

)