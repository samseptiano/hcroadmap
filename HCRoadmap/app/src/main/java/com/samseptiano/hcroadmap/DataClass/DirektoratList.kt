package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class DirektoratList(

	@field:SerializedName("direktoratid")
	var direktoratid: String? = "",

	@field:SerializedName("direktoratname")
	var direktoratname: String? = "",

	@field:SerializedName("compalias")
	var compalias: String? = ""

)
