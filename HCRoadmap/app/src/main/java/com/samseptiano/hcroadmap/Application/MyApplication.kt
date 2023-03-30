package com.samseptiano.hcroadmap.Application

import android.app.Application
import com.samseptiano.hcroadmap.DataClass.CompanyList
import com.samseptiano.hcroadmap.DataClass.DirektoratList
import com.samseptiano.hcroadmap.DataClass.TargetPosisiList


public class MyApp : Application() {
    var posId: String? = ""
    var dirId: String? = ""
    var compId: String? = ""


    var companyState: String? = ""
    var positionState: String? = ""
    var direktoratState: String? = ""


}