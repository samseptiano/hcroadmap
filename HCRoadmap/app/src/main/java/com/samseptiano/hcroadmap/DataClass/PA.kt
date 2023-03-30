package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class PA(

	@field:SerializedName("pascore")
	val pascore: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("empnik")
	val empnik: String? = null
)
