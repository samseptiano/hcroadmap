package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TalentReviewDetail(

	@field:SerializedName("talentReviewGroups")
	var talentReviewGroups: MutableList<TalentReviewGroupsItem?>? = null,

	@field:SerializedName("phasE_STATUS")
	var phasESTATUS: String? = "",

	@field:SerializedName("upduser")
	var upduser: String? = "",

	@field:SerializedName("phasE_NAME")
	var phasENAME: String? = "",

	@field:SerializedName("phasE_ID")
	var phasEID: String? = ""
)

data class TalentReviewReviewersItem(

	@field:SerializedName("empname")
	var empname: String? = "",

	@field:SerializedName("revieweR_NIK")
	var revieweRNIK: String? = "",

	@field:SerializedName("revieweR_ID")
	var revieweRID: String? = ""
)

data class TalentReviewDirektoratsItem(

	@field:SerializedName("origiN_DIR_ID")
	var origiNDIRID: String? = "",

	@field:SerializedName("compgrpid")
	var compgrpid: String? = "",

	@field:SerializedName("direktoratName")
	var direktoratName: String? = "",

	@field:SerializedName("tR_GROUP_DIR_ID")
	var tRGROUPDIRID: String? = ""
)

data class TalentReviewGroupsItem(

	@field:SerializedName("starT_DATE")
	var starTDATE: String? = "",

	@field:SerializedName("talentReviewDirektorats")
	var talentReviewDirektorats: List<TalentReviewDirektoratsItem?>? = null,

	@field:SerializedName("talentReviewModerators")
	var talentReviewModerators: List<TalentReviewModeratorsItem?>? = null,

	@field:SerializedName("phasE_ID")
	var phasEID: String? = "",

	@field:SerializedName("tR_GROUP_ID")
	var tRGROUPID: String? = "",

	@field:SerializedName("duE_DATE")
	var duEDATE: String? = "",

	@field:SerializedName("tR_GROUP_STATUS")
	var tRGROUPSTATUS: String? = "",

	@field:SerializedName("talentReviewReviewers")
	var talentReviewReviewers: List<TalentReviewReviewersItem?>? = null
) : Serializable

data class TalentReviewModeratorsItem(

	@field:SerializedName("empname")
	var empname: String? = "",

	@field:SerializedName("moderatoR_ID")
	var moderatoRID: String? = "",

	@field:SerializedName("moderatoR_NIK")
	var moderatoRNIK: String? = "",

	@field:SerializedName("flaG_FINISH")
	var flaGFINISH: String? = "",

	@field:SerializedName("finisH_DATE")
	var finisHDATE: String? = ""
)
