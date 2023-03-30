package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Award(

	@field:SerializedName("empname")
	val empname: String? = "",

	@field:SerializedName("honordescription")
	val honordescription: String? = "",

	@field:SerializedName("emphondate")
	val emphondate: String? = "",

	@field:SerializedName("honorname")
	val honorname: String? = "",

	@field:SerializedName("empnik")
	val empnik: String? = "",

	@field:SerializedName("emphonspno")
	val emphonspno: String? = ""
)
