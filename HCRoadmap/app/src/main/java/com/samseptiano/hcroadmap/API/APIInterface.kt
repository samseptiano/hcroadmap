package com.samseptiano.hcroadmap.API

import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.DataClass.POST.POSTMAPPING
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReview
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReviewPhase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    //================================================ GET TOKEN =======================================================================
    @Headers(
        "Content-Type: application/json;charset=UTF-8"
    )@POST("token")
    fun getToken(@Body userLogin: UserLogin): Call<UserLoginResponse?>?
    //==================================================================================================================================

    @Headers(
        "Content-Type: application/json;charset=UTF-8"
    )@POST("user")
    fun getUserLogin(@Body userBodyParameter: UserBodyParameter?, @Header("Authorization") auth:String): Call<User?>?

    @GET("talentmapping/{dirId}/{compID}/{NIK}")
    fun getUsers(@Path("dirId") dirId:String,@Path("compID") compID:String,@Path("NIK") NIK:String, @Header("Authorization") auth:String): Call<List<EmpTalentMapping>>

    @GET("setting/company/{compGrpId}/{NIK}")
    fun getCompany(@Path("compGrpId") compGrpId:String,@Path("NIK") NIK:String, @Header("Authorization") auth:String): Call<List<CompanyList>>

    @GET("setting/targetPosisi/{compGrpId}/{NIK}")
    fun getTargetPosisi(@Path("compGrpId") compGrpId:String,@Path("NIK") NIK:String, @Header("Authorization") auth:String): Call<List<TargetPosisiList>>

    @GET("setting/direktorat/{NIK}/{compGrpId}/{isAll}")
    fun getDirektorat(@Path("NIK") nik:String,@Path("compGrpId") compGrpId:String,@Path("isAll") isAll:String, @Header("Authorization") auth:String): Call<List<DirektoratList>>

       @GET("setting/goldanpend/{NIK}/{compGrpId}")
    fun getgolonganPendidikan(@Path("NIK") nik:String,@Path("compGrpId") compGrpId:String, @Header("Authorization") auth:String): Call<List<GolonganPendidikan>>


    @GET("setting/org/{dirid}")
    fun getOrg(@Path("dirid") dirid:String, @Header("Authorization") auth:String): Call<List<Org>>


    @GET("talentmapping/organizationstructure/{dirId}")
    fun getstructure(@Path("dirId") dirId:String, @Header("Authorization") auth:String): Call<MutableList<OrgStructure>>


    @Headers(
        "Content-Type: application/json;charset=UTF-8"
    )@POST("talentmapping/employeefiltered")
    fun getUsersFiltered(@Body empListFilter: EmpListFilter, @Header("Authorization") auth:String): Call<List<EmpTalentMapping?>>

    @GET("talentprofile/careerhistory/{NIK}")
    fun getCareerHistory(@Path("NIK") nik:String, @Header("Authorization") auth:String): Call<List<CareerHistory>>

    @GET("talentprofile/assessmentresult/{NIK}")
    fun getAssessmentResult(@Path("NIK") nik:String, @Header("Authorization") auth:String): Call<List<AssessmentResult>>


    @GET("talentmapping/aftermapping/{compId}/{dirId}/{dirGroupId}/{targetPosisi}")
    fun getUsersAfterMapping(@Path("compId") compId:String,@Path("dirId") dirId:String,@Path("dirGroupId") dirGroupId:String,@Path("targetPosisi") targetPosisi:String, @Header("Authorization") auth:String): Call<List<EmpTalentMappingAfter>>


    @Headers(
        "Content-Type: application/json;charset=UTF-8"
    )@POST("talentmapping/postmapping")
    fun postToMapping(@Body postmapping: POSTMAPPING, @Header("Authorization") auth:String): Call<Void>

    @Headers(
        "Content-Type: application/json;charset=UTF-8"
    )@POST("talentmapping/deletemapping")
    fun deleteToMapping(@Body postmapping: POSTMAPPING, @Header("Authorization") auth:String): Call<Void>


    @GET("talentprofile/training/{NIK}/{type}")
    fun getTraining(@Path("NIK") nik:String,@Path("type") type:String, @Header("Authorization") auth:String): Call<List<Training>>

    @GET("talentprofile/ldp/{NIK}/{compgrpid}")
    fun getLDP(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<List<LDP>>

    @GET("talentprofile/bgcheck/{NIK}/{compgrpid}")
    fun getBGCheck(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<List<BGCheck>>

    @GET("talentprofile/feedback360/{NIK}/{compgrpid}")
    fun getFeedback360(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<List<Feedback360>>

    @GET("talentprofile/constraint/{NIK}/{compgrpid}")
    fun getConstraint(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<ConstraintResponse>

    @GET("talentprofile/attachment/{NIK}/{compgrpid}")
    fun getattachment(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<List<Attachment>>

    @Multipart
    @POST("talentprofile/attachment/post")
    fun uploadAttachment(
        @Part file: MultipartBody.Part?,
        @Part("extension") extension: RequestBody?,
        @Part("attachid") ATTACH_ID: RequestBody?,
        @Part("filename") FILE_NAME: RequestBody?,
        @Part("filedesc") FILE_DESC: RequestBody?,
        @Part("compgrpid") COMP_GRP_ID: RequestBody?,
        @Part("upduser") UPDUSER: RequestBody?,
        @Part("empnik") EMPNIK: RequestBody?

    ): Call<String>

    @GET("talentprofile/attachment/del/{ATTACHID}/{updUser}/{filename}")
    fun deleteAttachment(@Path("ATTACHID") ATTACHID:String,@Path("updUser") updUser:String,@Path("filename") filename:String, @Header("Authorization") auth:String): Call<String>


    @GET("talentprofile/award/{NIK}/{compgrpid}/{pagesize}/{pagenumber}")
    fun getAward(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String,@Path("pagesize") pagesize:Int,@Path("pagenumber") pagenumber:Int, @Header("Authorization") auth:String): Call<List<Award>>

    @GET("talentprofile/inovasi/{NIK}/{compgrpid}")
    fun getInovasi(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<List<Inovasi>>


    @GET("talentprofile/pa/{NIK}")
    fun getPA(@Path("NIK") nik:String, @Header("Authorization") auth:String): Call<List<PA>>

    @GET("talentprofile/otherarchievement/{NIK}/{CompGrpId}")
    fun getOtherArchievement(@Path("NIK") nik:String,@Path("CompGrpId") CompGrpId:String, @Header("Authorization") auth:String): Call<List<OtherArchievement>>


    //=============== TALENT REVIEW =================
    @GET("talentreview/{NIK}/{compgrpid}")
    fun getTalentReview(@Path("NIK") nik:String,@Path("compgrpid") compgrpid:String, @Header("Authorization") auth:String): Call<MutableList<TalentReview>>

    @POST("talentreview/insert")
    fun insertTalentReview(@Body postTalentReview: POSTTalentReview, @Header("Authorization") auth:String): Call<Void>

    @DELETE("talentreview/delete/{trid}/{upduser}")
    fun deleteTalentReview(@Path("trid") trid:String,@Path("upduser") upduser:String, @Header("Authorization") auth:String): Call<Void>


    @GET("talentreview/details/{TRID}")
    fun getTalentReviewDetail(@Path("TRID") trid:String, @Header("Authorization") auth:String): Call<MutableList<TalentReviewDetail>>

    @GET("talentreview/moderator/{NIK}/{DIRID}")
    fun getModerator(@Path("NIK") nik:String,@Path("DIRID") dirid:String, @Header("Authorization") auth:String): Call<MutableList<Moderator>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("talentreview/detail/insert")
    fun postTalentReviewDetail(@Body talentReviewDetail: TalentReviewDetail, @Header("Authorization") auth:String): Call<Void>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("talentreview/detail/update")
    fun updateTalentReviewDetail(@Body talentReviewDetail: TalentReviewDetail, @Header("Authorization") auth:String): Call<Void>


    @GET("talentreview/detail/delete/{PHASEID}/{TRGROUPID}/{NIK}")
    fun deleteTalentReviewDetail(@Path("PHASEID") PHASEID:String,@Path("TRGROUPID") TRGROUPID:String,@Path("NIK") NIK:String, @Header("Authorization") auth:String): Call<Void>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("talentreview/phase/insert")
    fun insertTalentReviewPhase(@Body postTalentReviewPhase: POSTTalentReviewPhase, @Header("Authorization") auth:String): Call<Void>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("talentreview/phase/delete")
    fun deleteTalentReviewPhase(@Body postTalentReviewPhase: POSTTalentReviewPhase, @Header("Authorization") auth:String): Call<Void>

    @GET("talentreview/empFinal/{trgrpid}")
    fun getTalentReviewEmpFinal(@Path("trgrpid") trgrpid:String, @Header("Authorization") auth:String): Call<MutableList<EmpTalentReviewFinal>>

    @GET("talentreview/kandidatFinal/{trgrpid}")
    fun getTalentReviewKandidatFinal(@Path("trgrpid") trgrpid:String, @Header("Authorization") auth:String): Call<MutableList<EmpTalentReviewFinal>>

    @POST("talentreview/insertrekomendasi")
    fun insertRekomendasi(@Body kandidatRekomendasi: KandidatRekomendasi, @Header("Authorization") auth:String): Call<Void>
    @POST("talentreview/hapusrekomendasi")
    fun hapusRekomendasi(@Body kandidatRekomendasi: KandidatRekomendasi, @Header("Authorization") auth:String): Call<Void>


    //=============== ORG STRCTURE =================
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("orgstructure/post")
//    fun uploadOrgstructure(@Body fileOrgStructure: fileOrgStructure, @Header("Authorization") auth:String): Call<Void>

    //===========================================================================
    @GET("orgstructure/{DIRID}")
    fun getOrgStructure(@Path("DIRID") DIRID:String, @Header("Authorization") auth:String): Call<List<OrgStructureGet>>


    @Multipart
    @POST("orgstructure/post")
    fun uploadOrgstructure(
        @Part file: MultipartBody.Part?,
        @Part("dirid") dirid: RequestBody?,
        @Part("upduser") upduser: RequestBody?,
        @Part("filename") filename: RequestBody?

    ): Call<String>

    @POST("orgstructure/delete")
    fun deleteOrgStructure(@Body orgStructureGet: OrgStructureGet, @Header("Authorization") auth:String): Call<String>

    //================== LDP Setting ============================
    @GET("ldpsetting/{COMPGRPID}")
    fun getldpSetting(@Path("COMPGRPID") COMPGRPID:String, @Header("Authorization") auth:String): Call<List<LDPSetting>>

    @GET("ldpsetting/delete/{ldpid}")
    fun deleteldpSetting(@Path("ldpid") ldpid:String, @Header("Authorization") auth:String): Call<String>

    @POST("ldpsetting")
    fun insertldpSetting(@Body ldpSetting: LDPSetting, @Header("Authorization") auth:String): Call<String>


    //==================== Custom Category =====================
    @GET("customcategory/{NIK}")
    fun getCustomCategory(@Path("NIK") NIK:String, @Header("Authorization") auth:String): Call<List<CustomCategory>>
       @GET("customcategory/detail/{tableName}")
    fun getCustomCategoryDetail(@Path("tableName") tableName:String, @Header("Authorization") auth:String): Call<List<CustomCategoryDetail>>
    @POST("customcategory")
    fun insertCustomCategory(@Body customCategory: CustomCategory, @Header("Authorization") auth:String): Call<String>
    @POST("customcategory/delete")
    fun deleteCustomCategory(@Body customCategory: CustomCategory, @Header("Authorization") auth:String): Call<String>
    @POST("customcategory/delete/detail")
    fun deleteCustomCategoryDetail(@Body customCategoryDetail: CustomCategoryDetail, @Header("Authorization") auth:String): Call<String>
    @POST("customcategory/detail")
    fun insertCustomCategoryDetail(@Body customCategoryDetail: CustomCategoryDetail, @Header("Authorization") auth:String): Call<String>

    //===============User Management ===========================
    @GET("usermanagement/{pagesize}/{pagenumber}")
    fun getUserManagement(@Path("pagenumber") pagenumber:String,@Path("pagesize") pagesize:String, @Header("Authorization") auth:String): Call<List<UserManagement>>
    @GET("usermanagement/direktorat/{nik}/{compGrpid}")
    fun getUserManagementDir(@Path("nik") nik:String,@Path("compGrpid") compGrpid:String, @Header("Authorization") auth:String): Call<List<UsermanagementdirItem>>
    @GET("usermanagement/organization/{nik}/{compGrpid}")
    fun getUserManagementOrg(@Path("nik") nik:String,@Path("compGrpid") compGrpid:String, @Header("Authorization") auth:String): Call<List<UsermanagementorgItem>>

    @POST("usermanagement")
    fun insertUserManagement(@Body userManagement: UserManagement, @Header("Authorization") auth:String): Call<String>
    @POST("usermanagement/delete")
    fun deleteUserManagement(@Body userManagement: UserManagement, @Header("Authorization") auth:String): Call<String>

}