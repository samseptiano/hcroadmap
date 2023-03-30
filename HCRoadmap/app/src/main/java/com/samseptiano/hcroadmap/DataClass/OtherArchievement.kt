package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class OtherArchievement(

	@field:SerializedName("otheR_YEAR")
	val otheRYEAR: String? = null,

	@field:SerializedName("otheR_TITLE")
	val otheRTITLE: String? = null,

	@field:SerializedName("otheR_STATUS")
	val otheRSTATUS: String? = null
)
