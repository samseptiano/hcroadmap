package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Org(

	@field:SerializedName("orgname")
	var orgname: String? = "",

	@field:SerializedName("dirid")
	var dirid: String? = "",

	@field:SerializedName("orgid")
	var orgid: String? = ""
)
