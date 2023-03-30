package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class Feedback360(

	@field:SerializedName("feedbacK_DATE")
	val feedbacKDATE: String? = "",

	@field:SerializedName("feedbackT_IMPROVEMENT")
	val feedbackTIMPROVEMENT: String? = "",

	@field:SerializedName("feedbackT_STRENGTH")
	val feedbackTSTRENGTH: String? = ""
)
