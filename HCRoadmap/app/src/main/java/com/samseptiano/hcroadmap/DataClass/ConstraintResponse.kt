package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class ConstraintResponse(

	@field:SerializedName("violations")
	val violations: MutableList<Violation>? = null,

	@field:SerializedName("constraint")
	val constraint: Constraint? = null

)
