package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class CustomCategory(

	@field:SerializedName("attributE_ID")
	var attribute_id: String? = "",

	@field:SerializedName("attributE_TITLE")
	var attribute_title: String? = "",

	@field:SerializedName("tablE_NAME")
	var table_name: String? = "",

	@field:SerializedName("flaG_UPLOAD")
	var flag_upload: String? = "",

	@field:SerializedName("flaG_AUTOMATED_TABLE")
	var flag_automated_table: String? = "",

	@field:SerializedName("flaG_NIK_PK")
	var flag_nik_pk: String? = ""
)
