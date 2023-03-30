package com.samseptiano.hcroadmap.DataClass.POST

import com.google.gson.annotations.SerializedName

data class POSTTalentReview(
    @field:SerializedName("tr_id")
    var tr_id: String? = "",
    @field:SerializedName("tr_name")
    var tr_name: String? = "",
    @field:SerializedName("target_position_id")
    var target_position_id: String? = "",
    @field:SerializedName("origin_comp_id")
    var origin_comp_id: String? = "",
    @field:SerializedName("origin_dir_id")
    var origin_dir_id: String? = "",
    @field:SerializedName("tr_status")
    var tr_status: String? = "",
    @field:SerializedName("upd_user")
    var upd_user: String? = "",
    @field:SerializedName("comp_id")
var comp_id: String? = ""
)