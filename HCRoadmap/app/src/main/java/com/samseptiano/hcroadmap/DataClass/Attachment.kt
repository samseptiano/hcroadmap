package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Attachment(

	@field:SerializedName("filE_NAME")
	var filENAME: String? = "",

	@field:SerializedName("attacH_ID")
	var attacHID: String? = "",

	@field:SerializedName("updatE_DATE")
	var updatEDATE: String? = "",

	@field:SerializedName("filE_DESC")
	var filEDESC: String? = "",

	@field:SerializedName("comP_GRP_ID")
	var comPGRPID: String? = "",
	@field:SerializedName("UPDUSER")
	var UPDUSER: String? = ""
)
