package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class BGCheck(

	@field:SerializedName("bgchecK_DATE")
	val bgchecKDATE: String? = null,

	@field:SerializedName("bgchecK_FILE")
	val bgchecKFILE: String? = null,

	@field:SerializedName("bgchecK_SUMMARY")
	val bgchecKSUMMARY: String? = null,

	@field:SerializedName("bgchecK_RECOMMEND")
	val bgchecKRECOMMEND: String? = null
)
