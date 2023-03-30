package com.samseptiano.hcroadmap.DataClass

import android.os.Parcelable
import java.io.Serializable

data class EmpList(
    var id:String?="",
    var empName:String?="",
    var empNIK:String?="",
    var empDept:String?="",
    var empJobTitle:String?="",
    var empCompany:String?="",
    var empBranch:String?="",
    var empOrg:String?="",
    var empAge:String?="",
    var empPhoto:String?="",
    var compGroupID:String?="",
    var archievement:String?="",
    var recommendation:String?="",
    var empJobLevel:String?="",
    var posId:String?="",
    var compID:String?="",
    var dirId:String?="",
    //data diri
    var dataDiri: EmpListDataDiri?=null,

    //career history
    var careerHistoryList: MutableList<EmpCareerHistory>?=null,

    //assessment result
    var assessmentResult: empAssessmentResult?=null,

    //learning
    var learning: empLearning?=null,
    //background check
    var bgCheck: empBgCheck?=null,

    //feedback 360
    var feedback360: empfeedback360?=null,

    //constraint
    var constraint: empConstraint?=null,

    //engagement profiling

    //file attachment

    //inovasi
    var innovation: empInnovation?=null
):Serializable

data class EmpListDataDiri(
    var empSignDate:String?="",
    var empLastPromotion:String?="",
    var emplastEducation:String?="",
    var empHomeBase:String?="",
    var empBirthPlace:String?="",
    var empBirthDate:String?="",
    var empMaritalStatus:String?="",
    var empAge:String?="",
    var empRetirementDate:String?="",
    var compGroupID:String?="",
    var location:String?=""

):Serializable

data class EmpCareerHistory(
    var tanggal:String?="",
    var jabatan:String?="",
    var direktorat:String?="",
    var golongan:String?="",
    var cabang:String?="",
    var compGroupID:String?=""

    )

data class empAssessmentResult(
    var tanggalAssessment:String?="",
    var picAssessment:String?="",
    var scoreAssessment:String?="",
    var capacityAssessment:String?="",
    var resultAssessment:String?="",
    var discAssessment:String?="",
    var compGroupID:String?="",
    var chartAssessment:String?="",
    var havAssessment:String?="",
    var resultAssessmentImage1:String?="",
    var resultAssessmentImage2:String?=""

)

data class empBgCheck(
    var id:String?="",
    var empNIK:String?="",
    var bgCheckDate:String?="",
    var bgCheckFile:String?="",
    var bgCheckSummary:String?="",
    var bgCheckRecommend:String?="",
    var compGroupID:String?=""
)

data class empfeedback360(
    var id:String?="",
    var date:String?="",
    var empNIK:String?="",
    var feedbackStrength:String?="",
    var feedbackImprovement:String?="",
    var compGroupID:String?=""
)

data class empConstraint(
    var id:String?="",
    var empNIK:String?="",
    var constraintValue:String?="",
    var constraintNote:String?="",
    var compGroupID:String?=""
)


data class empInnovation(
    var id:String?="",
    var empNIK:String?="",
    var kategory:String?="",
    var judul:String?="",
    var tahun:String?="",
    var inovasi:String?="",
    var hasil:String?="",
    var fileProject:String?="",
    var compGroupID:String?=""
)

data class empLearning(
    var id:String?="",
    var empNIK:String?="",
    var kmmpProgram:String?="",
    var kmmpTitle:String?="",
    var kmmpResult:String?="",
    var kmmpProgress:String?="",
    var compGroupID:String?=""
)