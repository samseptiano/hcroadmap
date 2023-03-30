package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class CareerHistory(

	@field:SerializedName("locationname")
	val locationname: String? = "",

	@field:SerializedName("orgname")
	val orgname: String? = "",

	@field:SerializedName("sktype")
	val sktype: String? = "",

	@field:SerializedName("skid")
	val skid: String? = "",

	@field:SerializedName("jobttlname")
	val jobttlname: String? = "",

	@field:SerializedName("joblevel")
	val joblevel: String? = "",

	@field:SerializedName("skeffective")
	val skeffective: String? = "",

	@field:SerializedName("skno")
	val skno: String? = ""
)
