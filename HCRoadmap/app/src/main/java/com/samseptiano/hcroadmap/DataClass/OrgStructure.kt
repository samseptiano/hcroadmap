package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class OrgStructure(

	@field:SerializedName("directoratE_ID")
	var directoratEID: String? = "",

	@field:SerializedName("structurE_ID")
	var structurEID: String? = "",

	@field:SerializedName("structurE_FILE")
	var structurEFILE: String? = ""
)
