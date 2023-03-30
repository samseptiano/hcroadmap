package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class TalentReview(

	@field:SerializedName("tR_STATUS")
	val tRSTATUS: String? = "",

	@field:SerializedName("targeT_POSITION_ID")
	val targeTPOSITIONID: String? = "",

	@field:SerializedName("direktoratid")
	val direktoratid: String? = "",

	@field:SerializedName("jobttlname")
	val jobttlname: String? = "",

	@field:SerializedName("jobttlid")
	val jobttlid: String? = "",

	@field:SerializedName("compgrpid")
	val compgrpid: String? = "",

	@field:SerializedName("compid")
	val compid: String? = "",

	@field:SerializedName("origiN_COMP_ID")
	val origiNCOMPID: String? = "",

	@field:SerializedName("creatE_DATE")
	val creatEDATE: String? = "",

	@field:SerializedName("direktoratname")
	val direktoratname: String? = "",

	@field:SerializedName("tR_NAME")
	val tRNAME: String? = "",

	@field:SerializedName("tR_ID")
	val tRID: String? = "",

	@field:SerializedName("tR_CREATE_DATE")
	val tRCREATEDATE: String? = "",

	@field:SerializedName("compname")
	val compname: String? = "",

	@field:SerializedName("isOpened")
	var isOpened: Boolean? = false

)
