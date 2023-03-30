package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Training(

	@field:SerializedName("trainingdate")
	val trainingdate: String? = "",

	@field:SerializedName("hasiltraining")
	val hasiltraining: String? = "",

	@field:SerializedName("trainingtype")
	val trainingtype: String? = "",

	@field:SerializedName("trainingname")
	val trainingname: String? = ""
)
