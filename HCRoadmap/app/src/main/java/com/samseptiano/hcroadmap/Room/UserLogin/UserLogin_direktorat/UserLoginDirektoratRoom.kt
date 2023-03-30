package com.samseptiano.hcroadmap.Room.UserLogin.UserLogin_direktorat

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "userLoginDirektorat")
//Parcelable annotation to make parcelable object
@Parcelize
data class UserLoginDirektoratRoom(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "emp_id") var empId: String = "",
    @ColumnInfo(name = "emp_userdirid") var userDirId: String = "",
    @ColumnInfo(name = "emp_nik") var empNIK: String = "",
    @ColumnInfo(name = "emp_compgrpid") var compGrpId: String = "",
    @ColumnInfo(name = "emp_direktoratid") var direktoratid: String = "",
    @ColumnInfo(name = "emp_flagactive") var flagActive: String = ""


    ) : Parcelable