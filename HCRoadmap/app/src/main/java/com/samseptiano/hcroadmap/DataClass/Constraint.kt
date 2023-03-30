package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Constraint(

	@field:SerializedName("constrainT_NOTE")
	val constrainTNOTE: String? = "",

	@field:SerializedName("constrainT_VALUE")
	val constrainTVALUE: String? = ""
)
