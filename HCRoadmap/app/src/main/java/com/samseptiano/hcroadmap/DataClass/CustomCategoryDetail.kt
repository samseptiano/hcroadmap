package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class CustomCategoryDetail(

	@field:SerializedName("columN_NAME")
	var columNNAME: String? = "",

	@field:SerializedName("columN_TYPE")
	var columNTYPE: String? = "",

	@field:SerializedName("tablE_NAME")
	var tablENAME: String? = "",
	@field:SerializedName("datatype")
	var datatype: String? = ""
)
