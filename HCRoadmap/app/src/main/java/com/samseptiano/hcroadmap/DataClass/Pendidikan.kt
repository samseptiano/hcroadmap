package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Pendidikan(

	@field:SerializedName("fgseq")
	val fgseq: String? = null,

	@field:SerializedName("edulvlname")
	val edulvlname: String? = null
)
