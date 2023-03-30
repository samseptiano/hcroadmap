package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class TargetPosisiList(

	@field:SerializedName("jobttlcompgrpid")
	var jobttlcompgrpid: String? = "",

	@field:SerializedName("jobttlid")
	var jobttlid: String? = "",

	@field:SerializedName("jobttlcode")
	var jobttlcode: String? = "",

	@field:SerializedName("jobttlname")
	var jobttlname: String? = "",

	@field:SerializedName("compcode")
	var compcode: String? = ""

)
