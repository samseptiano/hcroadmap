package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

data class fileOrgStructure(

	@field:SerializedName("image")
	var image: MultipartBody.Part? = null,

	@field:SerializedName("upduser")
	var upduser: RequestBody? = null,
	@field:SerializedName("filename")
	var filename: RequestBody? = null,
	@field:SerializedName("dirid")
	var dirid: RequestBody? = null
)
