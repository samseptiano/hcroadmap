package com.samseptiano.hcroadmap.DataClass.POST

import com.google.gson.annotations.SerializedName

data class POSTTalentReviewPhase(
    @field:SerializedName("trid")
    var trid: String? = "",
    @field:SerializedName("phasename")
    var phasename: String? = "",
    @field:SerializedName("phasestatus")
    var phasestatus: String? = "",
    @field:SerializedName("upduser")
    var upduser: String? = ""
)