package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Module(

	@field:SerializedName("modulecode")
	var modulecode: String? = "",

	@field:SerializedName("moduledesc")
	var moduledesc: String? = "",

	@field:SerializedName("icon")
	var icon: String? = "",

	@field:SerializedName("fgapp")
	var fgapp: String? = "",

	@field:SerializedName("ordinal")
	var ordinal: String? = "",

	@field:SerializedName("pagename")
	var pagename: String? = ""
)
