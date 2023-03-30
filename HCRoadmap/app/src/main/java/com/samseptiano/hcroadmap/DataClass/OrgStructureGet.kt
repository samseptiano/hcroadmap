package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class OrgStructureGet(

	@field:SerializedName("image")
	var image: String? = "",

	@field:SerializedName("filename")
	var filename: String? = "",

	@field:SerializedName("structureid")
	var structureid: String? = "",

	@field:SerializedName("dirid")
	var dirid: String? = "",

	@field:SerializedName("upduser")
	var upduser: String? = ""
)
