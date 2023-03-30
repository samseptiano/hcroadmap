package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class GolonganPendidikan(

	@field:SerializedName("joblevel")
	val joblevel: MutableList<Golongan>? = null,

	@field:SerializedName("pendidikan")
	val pendidikan: MutableList<Pendidikan>? = null
)
