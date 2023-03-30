package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("empId")
	val empId: String? = "",

	@field:SerializedName("empdateend")
	val empdateend: Any? = "",

	@field:SerializedName("empnik")
	val empnik: String? = "",

	@field:SerializedName("labourst")
	val labourst: String? = "",

	@field:SerializedName("empjoindate")
	val empjoindate: String? = "",

	@field:SerializedName("joblevel")
	val joblevel: String? = "",

	@field:SerializedName("empvendorid")
	val empvendorid: Any? = "",

	@field:SerializedName("password")
	val password: String? = "",

	@field:SerializedName("emplastname")
	val emplastname: Any? = "",

	@field:SerializedName("lasteducation")
	val lasteducation: String? = "",

	@field:SerializedName("flaG_SUPERADMIN")
	val flaGSUPERADMIN: String? = "",

	@field:SerializedName("flaG_SALARY")
	val flaGSALARY: String? = "",

	@field:SerializedName("empinisial")
	val empinisial: Any? = "",

	@field:SerializedName("exportdate")
	val exportdate: Any? = "",

	@field:SerializedName("lastpromotiondate")
	val lastpromotiondate: String? = "",

	@field:SerializedName("empname")
	val empname: String? = "",

	@field:SerializedName("empfirstname")
	val empfirstname: Any? = "",

	@field:SerializedName("flaG_ADMIN")
	val flaGADMIN: String? = "",

	@field:SerializedName("empcostctreid")
	val empcostctreid: Any? = "",

	@field:SerializedName("empjobttlid")
	val empjobttlid: String? = "",

	@field:SerializedName("fgfreshgrad")
	val fgfreshgrad: String? = "",

	@field:SerializedName("empcompid")
	val empcompid: String? = "",

	@field:SerializedName("empdatestart")
	val empdatestart: String? = "",

	@field:SerializedName("emplocid")
	val emplocid: String? = "",

	@field:SerializedName("compgrpid")
	val compgrpid: String? = "",

	@field:SerializedName("flaG_NONADMIN")
	val flaGNONADMIN: String? = "",

	@field:SerializedName("empdatefrom")
	val empdatefrom: String? = "",

	@field:SerializedName("emptypeid")
	val emptypeid: String? = "",

	@field:SerializedName("birthplace")
	val birthplace: String? = "",

	@field:SerializedName("empdatebirth")
	val birthdate: String? = "",

	@field:SerializedName("maritalstatus")
	val maritalstatus: String? = "",

	@field:SerializedName("empsigndate")
	val empsigndate: String? = "",

	@field:SerializedName("flaG_ACTIVE")
	val flaGACTIVE: String? = "",

	@field:SerializedName("organization")
	val organization: String? = "",

	@field:SerializedName("emppositionid")
	val emppositionid: String? = "",

	@field:SerializedName("emporgid")
	val emporgid: String? = "",

	@field:SerializedName("emplocrecruitid")
	val emplocrecruitid: String? = "",

	@field:SerializedName("location")
	val location: String? = "",

	@field:SerializedName("homebase")
	val homebase: String? = "",

	@field:SerializedName("age")
	val age: String? = "",

	@field:SerializedName("username")
	val username: String? = "",

	@field:SerializedName("groupcode")
	val groupcode: String? = "",

	@field:SerializedName("groupdesc")
	val groupdesc: String? = "",

	@field:SerializedName("modulelist")
	val modulelist: List<Module>? = null
)
