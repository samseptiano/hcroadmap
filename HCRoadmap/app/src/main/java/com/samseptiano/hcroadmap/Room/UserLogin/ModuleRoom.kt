package com.samseptiano.hcroadmap.Room.UserLogin

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "module")
//Parcelable annotation to make parcelable object
@Parcelize
data class ModuleRoom(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "modulecode") var modulecode: String ?= "",
    @ColumnInfo(name = "moduledesc") var moduledesc: String ?= "",
    @ColumnInfo(name = "icon") var icon: String ?= "",
    @ColumnInfo(name = "fgapp") var fgapp: String ?= "",
    @ColumnInfo(name = "ordinal") var ordinal: String ?= "",
    @ColumnInfo(name = "pagename") var pagename: String ?= ""
    ) : Parcelable