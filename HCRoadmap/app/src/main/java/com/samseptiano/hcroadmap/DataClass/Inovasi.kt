package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Inovasi(

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null
)
