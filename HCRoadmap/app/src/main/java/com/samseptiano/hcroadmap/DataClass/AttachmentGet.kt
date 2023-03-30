package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class AttachmentGet(

	@field:SerializedName("image")
	var image: MultipartBody.Part? = null,

	@field:SerializedName("FILE_NAME")
	var FILE_NAME: RequestBody? = null,

	@field:SerializedName("extension")
	var extension: RequestBody? = null,

	@field:SerializedName("ATTACH_ID")
	var ATTACH_ID: RequestBody? = null,

	@field:SerializedName("UPDUSER")
	var UPDUSER: RequestBody? = null,

	@field:SerializedName("FILE_DESC")
	var FILE_DESC: RequestBody? = null,

	@field:SerializedName("EMPNIK")
	var EMPNIK: RequestBody? = null,

	@field:SerializedName("COMP_GRP_ID")
	var COMP_GRP_ID: RequestBody? = null

)
