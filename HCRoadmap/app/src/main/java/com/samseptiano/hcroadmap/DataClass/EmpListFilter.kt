package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class EmpListFilter(

	@field:SerializedName("direktoratid")
	var direktoratid: String? = "",

	@field:SerializedName("compgrpid")
	var compgrpid: String? = "",

	@field:SerializedName("jobttlid")
	var jobttlid: String? = "",
	@field:SerializedName("compid")
	var compid: String? = "",

	@field:SerializedName("empnik")
	var empnik: String? = "",

	@field:SerializedName("umurmin")
	var umurmin: String? = "",
	@field:SerializedName("umurmax")
	var umurmax: String? = "",
	@field:SerializedName("pensiunmin")
	var pensiunmin: String? = "",
	@field:SerializedName("pensiunmax")
	var pensiunmax: String? = "",

	@field:SerializedName("pendidikan")
	var pendidikan: String? = "",
	@field:SerializedName("golongan")
	var golongan: String? = "",

	@field:SerializedName("compgrpidawal")
	var compgrpidawal: String? = "",

	@field:SerializedName("jobttlidawal")
	var jobttlidawal: String? = ""

)
