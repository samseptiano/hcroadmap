package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class LDP(

	@field:SerializedName("kmmP_PROGRAM")
	val kmmPPROGRAM: String? = null,

	@field:SerializedName("kmmP_TITLE")
	val kmmPTITLE: String? = null,

	@field:SerializedName("kmmP_RESULT")
	val kmmPRESULT: String? = null,

	@field:SerializedName("kmmP_PROGRESS")
	val kmmPPROGRESS: String? = null,

	@field:SerializedName("kmmP_YEAR")
	val kmmPYEAR: String? = null
)
