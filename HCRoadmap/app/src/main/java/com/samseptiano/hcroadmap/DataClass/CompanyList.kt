package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class CompanyList(

	@field:SerializedName("compgrpid")
	val compgrpid: String? = null,

	@field:SerializedName("compid")
	val compid: String? = null,

	@field:SerializedName("compalias")
	val compalias: String? = null,

	@field:SerializedName("compname")
	val compname: String? = null,

	@field:SerializedName("compcode")
	val compcode: String? = null
)
