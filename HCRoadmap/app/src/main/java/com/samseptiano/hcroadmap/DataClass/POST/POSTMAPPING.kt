package com.samseptiano.hcroadmap.DataClass.POST

import com.google.gson.annotations.SerializedName

data class POSTMAPPING(
    @field:SerializedName("empnik")
    var empnik: String? = "",
    @field:SerializedName("posid")
    var posid: String? = "",
    @field:SerializedName("dirid")
    var dirid: String? = "",
    @field:SerializedName("compid")
    var compid: String? = "",
    @field:SerializedName("trgrpid")
    var trgrpid: String? = "",
    @field:SerializedName("archievement")
    var archievement: String? = "",
    @field:SerializedName("reason")
    var reason: String? = "",
    @field:SerializedName("upduser")
    var upduser: String? = ""
)