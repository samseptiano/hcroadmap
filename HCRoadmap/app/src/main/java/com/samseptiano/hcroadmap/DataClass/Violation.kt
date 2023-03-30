package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Violation(

	@field:SerializedName("empviolnote")
	val empviolnote: String? = "",

	@field:SerializedName("empvioldate")
	val empvioldate: String? = "",

	@field:SerializedName("violancename")
	val violancename: String? = "",

	@field:SerializedName("warninglevel")
	val warninglevel: String? = ""
)
