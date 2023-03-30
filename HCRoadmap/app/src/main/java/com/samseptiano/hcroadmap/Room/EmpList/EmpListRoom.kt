package com.samseptiano.hcroadmap.Room.EmpList

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "empList")
//Parcelable annotation to make parcelable object
@Parcelize
data class EmpListRoom(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "emp_name") var empName: String = "",
    @ColumnInfo(name = "emp_nik") var empNIK: String = "",
    @ColumnInfo(name = "emp_dept") var empDept: String = "",
    @ColumnInfo(name = "emp_jobtitle") var empJobTitle: String = "",
    @ColumnInfo(name = "emp_company") var empCompany: String = "",
    @ColumnInfo(name = "emp_branch") var empBranch: String = "",
    @ColumnInfo(name = "emp_org") var empOrg: String = "",
    @ColumnInfo(name = "emp_age") var empAge: String = "",
    @ColumnInfo(name = "emp_photo") var empPhoto: String = "",
    @ColumnInfo(name = "emp_compgroupid") var compGroupID: String = "",
    @ColumnInfo(name = "emp_archievement") var archievement: String = "",
    @ColumnInfo(name = "emp_recommendation") var recommendation: String = ""

) : Parcelable