package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Moderator(

	@field:SerializedName("direktoratname")
	val direktoratname: String? = "",

	@field:SerializedName("empNIK")
	val empNIK: String? = "",

	@field:SerializedName("direktoratid")
	val direktoratid: String? = "",

	@field:SerializedName("empName")
	val empName: String? = "",

	@field:SerializedName("organization")
	val organization: String? = "",

	@field:SerializedName("empJobTtlId")
	val empJobTtlId: String? = "",

	@field:SerializedName("location")
	val location: String? = "",

	@field:SerializedName("joblevel")
	val joblevel: String? = "",

	@field:SerializedName("empOrgId")
	val empOrgId: String? = ""
)
