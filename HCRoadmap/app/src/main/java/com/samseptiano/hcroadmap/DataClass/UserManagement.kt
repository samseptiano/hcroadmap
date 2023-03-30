package com.samseptiano.hcroadmap.DataClass

import com.google.gson.annotations.SerializedName

data class UserManagement(
	@field:SerializedName("empname")
	var empname: String? = null,
	@field:SerializedName("flagpic")
	var flagpic: String? = null,
	@field:SerializedName("nik")
	var nik: String? = null,
	@field:SerializedName("compgrpid")
	var compgrpid: String? = null,
	@field:SerializedName("groupcode")
	var groupcode: String? = "",
	@field:SerializedName("upduser")
	var upduser: String? = "",
	@field:SerializedName("flaguploadstruktur")
	var flaguploadstruktur: String? = null,
	@field:SerializedName("usermanagementorg")
	var usermanagementorg: List<UsermanagementorgItem?>? = null,
	@field:SerializedName("role")
	var role: String? = null,
	@field:SerializedName("totalusermanagementdir")
	var totalusermanagementdir: String? = "",
	@field:SerializedName("totalusermanagementorg")
	var totalusermanagementorg: String? = "",
	@field:SerializedName("flagsalary")
	var flagsalary: String? = null,
	@field:SerializedName("usermanagementdir")
	var usermanagementdir: List<UsermanagementdirItem?>? = null
)

data class UsermanagementorgItem(
	@field:SerializedName("compgrpid")
	var compgrpid: String? = null,
	@field:SerializedName("orgname")
	var orgname: String? = null,
	@field:SerializedName("dirid")
	var dirid: String? = null,
	@field:SerializedName("dirname")
	var dirname: String? = null,
	@field:SerializedName("orgid")
	var orgid: String? = null
)

data class UsermanagementdirItem(
	@field:SerializedName("dirid")
	var dirid: String? = null,
	@field:SerializedName("dirname")
	var dirname: String? = null
)

