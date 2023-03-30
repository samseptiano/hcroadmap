package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class LDPSetting(

	@field:SerializedName("ldpid")
	var ldp_id: String? = "",

	@field:SerializedName("compgrpid")
	var compgrpid: String? = "",

	@field:SerializedName("ldptitle")
	var ldp_title: String? = "",

	@field:SerializedName("compname")
	var compname: String? = "",

	@field:SerializedName("upduser")
	var upduser: String? = "",

	@field:SerializedName("compid")
	var compid: String? = ""
)
