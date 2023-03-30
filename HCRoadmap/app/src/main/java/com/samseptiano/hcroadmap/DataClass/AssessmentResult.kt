package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class AssessmentResult(

	@field:SerializedName("assessmenT_IMG2")
	val assessmenTIMG2: String? = "",

	@field:SerializedName("assessmenT_CHART_PIC")
	val assessmenTCHARTPIC: String? = "",

	@field:SerializedName("assesmenT_DATE")
	val assesmenTDATE: String? = "",

	@field:SerializedName("assesmenT_RESULT")
	val assesmenTRESULT: String? = "",

	@field:SerializedName("assessmenT_HAV_PIC")
	val assessmenTHAVPIC: String? = "",

	@field:SerializedName("assesmenT_PIC")
	val assesmenTPIC: String? = "",

	@field:SerializedName("assesmenT_IMPROVEMENT")
	val assesmenTIMPROVEMENT: String? = "",

	@field:SerializedName("assessmenT_IMG1")
	val assessmenTIMG1: String? = "",

	@field:SerializedName("assesmenT_ID")
	val assesmenTID: String? = "",

	@field:SerializedName("comP_GRP_ID")
	val comPGRPID: String? = "",

	@field:SerializedName("assesmenT_STRENGTH")
	val assesmenTSTRENGTH: String? = "",

	@field:SerializedName("assesmenT_SCORE")
	val assesmenTSCORE: String? = "",

	@field:SerializedName("assessmenT_CAPACITY")
	val assessmenTCAPACITY: String? = "",

	@field:SerializedName("assessmenT_DISC")
	val assessmenTDISC: String? = ""
)
