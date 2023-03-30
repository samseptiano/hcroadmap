package com.samseptiano.hcroadmap.Room.UserLogin

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "userLogin")
//Parcelable annotation to make parcelable object
@Parcelize
data class UserLoginRoom(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "emp_id") var empId: String ?= "",
    @ColumnInfo(name = "emp_nik") var empNIK: String ?= "",
    @ColumnInfo(name = "emp_name") var empName: String ?= "",


    @ColumnInfo(name = "username") var username: String ?= "",
    @ColumnInfo(name = "token") var token: String ?= "",


    @ColumnInfo(name = "emp_fname") var empFirstName: String ?= "",
    @ColumnInfo(name = "emp_lname") var empLastName: String ?= "",
    @ColumnInfo(name = "emp_inisial") var empInisial: String ?= "",
    @ColumnInfo(name = "emp_jobttlid") var empJobTtlId: String ?= "",
    @ColumnInfo(name = "emp_positionid") var empPositionId: String ?= "",
    @ColumnInfo(name = "emp_orgid") var empOrgId: String ?= "",

    @ColumnInfo(name = "emp_joblvl") var empJobLvl: String ?= "",
    @ColumnInfo(name = "emp_locid") var empLocId: String ?= "",
    @ColumnInfo(name = "emp_compid") var empCompId: String ?= "",
    @ColumnInfo(name = "emp_typeid") var empTypeId: String ?= "",
    @ColumnInfo(name = "emp_vendorid") var empVendorId: String ?= "",
    @ColumnInfo(name = "emp_datestart") var empDateStart: String ?= "",
    @ColumnInfo(name = "emp_enddate") var empEndDate: String ?= "",
    @ColumnInfo(name = "emp_joindate") var empJoinDate: String ?= "",
    @ColumnInfo(name = "emp_signdate") var empSignDate: String ?= "",
    @ColumnInfo(name = "emp_costcentreid") var empCostCentreId: String ?= "",
    @ColumnInfo(name = "emp_locrecruitid") var empLocRecruitId: String ?= "",
    @ColumnInfo(name = "emp_exportdate") var empExportDate: String ?= "",
    @ColumnInfo(name = "emp_labourst") var labourSt: String ?= "",
    @ColumnInfo(name = "emp_datefrom") var empDateFrom: String ?= "",
    @ColumnInfo(name = "emp_datebirth") var empDateBirth: String ?= "",
    @ColumnInfo(name = "emp_birthplace") var empBirthPlace: String ?= "",
    @ColumnInfo(name = "emp_lastpromotiondate") var lastPromotionDate: String ?= "",
    @ColumnInfo(name = "emp_lasteducation") var lastEducation: String ?= "",
    @ColumnInfo(name = "emp_maritalstatus") var maritalStatus: String ?= "",
    @ColumnInfo(name = "emp_homebase") var homebase: String ?= "",
    @ColumnInfo(name = "emp_fgsuperadmin") var fgSuperAdmin: String ?= "",
    @ColumnInfo(name = "emp_fgadmin") var fgAdmin: String ?= "",
    @ColumnInfo(name = "emp_fgnonadmin") var fgNonAdmin: String ?= "",
    @ColumnInfo(name = "emp_fgsalary") var fgSalary: String ?= "",
    @ColumnInfo(name = "emp_fgactive") var fgActive: String ?= "",
    @ColumnInfo(name = "emp_organization") var organization: String ?= "",
    @ColumnInfo(name = "emp_compgrpid") var compGrpId: String ?= "",
    @ColumnInfo(name = "emp_location") var location: String ?= ""





    ) : Parcelable