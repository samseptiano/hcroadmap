package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Golongan(

	@field:SerializedName("joblevelname")
	val joblevelname: String? = null,

	@field:SerializedName("joblevel")
	val joblevel: String? = null
)
