package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReview
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReviewPhase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TalentReviewRepo {

    lateinit var context: Context

    fun getcontext(context: Context){
        this.context = context
    }

    fun getCompany(nik:String,token:String,compId:String): MutableLiveData<List<CompanyList>> {

        val mutableLiveData: MutableLiveData<List<CompanyList>> =
            MutableLiveData<List<CompanyList>>()

        nik?.let {
            APIConfig().getService()
                .getCompany("ALL",it,"Bearer "+token)
                .enqueue(object : Callback<List<CompanyList>> {
                    override fun onFailure(call: Call<List<CompanyList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<CompanyList>>,
                        response: Response<List<CompanyList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun gettargetPosisi(nik:String,token:String,compId:String): MutableLiveData<List<TargetPosisiList>> {

        val mutableLiveData: MutableLiveData<List<TargetPosisiList>> =
            MutableLiveData<List<TargetPosisiList>>()

        nik?.let {
            APIConfig().getService()
                .getTargetPosisi(compId,it,"Bearer "+token)
                .enqueue(object : Callback<List<TargetPosisiList>> {
                    override fun onFailure(call: Call<List<TargetPosisiList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<TargetPosisiList>>,
                        response: Response<List<TargetPosisiList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun getdirektorat(nik:String,token:String,compId:String): MutableLiveData<List<DirektoratList>> {

        val mutableLiveData: MutableLiveData<List<DirektoratList>> =
            MutableLiveData<List<DirektoratList>>()

        nik?.let {
            APIConfig().getService()
                .getDirektorat(it,compId,"ALL","Bearer "+token)
                .enqueue(object : Callback<List<DirektoratList>> {
                    override fun onFailure(call: Call<List<DirektoratList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<DirektoratList>>,
                        response: Response<List<DirektoratList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun insertCompanyList(companyListObj: MutableList<CompanyList>): MutableLiveData<MutableList<String>> {

        val mutableLiveData: MutableLiveData<MutableList<String>> =
            MutableLiveData<MutableList<String>>()


        var companyList = mutableListOf<String>()

        for (i in 0 until companyListObj?.size!!){
            companyList!!.add(companyListObj!![i]?.compname.toString())
        }

       mutableLiveData.setValue(companyList)
        return mutableLiveData
    }
    fun insertPosisiList(companyListObj: MutableList<TargetPosisiList>): MutableLiveData<MutableList<String>> {

        val mutableLiveData: MutableLiveData<MutableList<String>> =
            MutableLiveData<MutableList<String>>()


        var companyList = mutableListOf<String>()

        for (i in 0 until companyListObj?.size!!){
            companyList!!.add(companyListObj!![i]?.jobttlname.toString())
        }

        mutableLiveData.setValue(companyList)
        return mutableLiveData
    }
    fun insertDirektoratList(companyListObj: MutableList<DirektoratList>): MutableLiveData<MutableList<String>> {

        val mutableLiveData: MutableLiveData<MutableList<String>> =
            MutableLiveData<MutableList<String>>()


        var companyList = mutableListOf<String>()

        for (i in 0 until companyListObj?.size!!){
            companyList!!.add(companyListObj!![i]?.direktoratname.toString())
        }

        mutableLiveData.setValue(companyList)
        return mutableLiveData
    }

    fun getTalentReview(nik:String,token:String,compId:String): MutableLiveData<MutableList<TalentReview>> {

        val mutableLiveData: MutableLiveData<MutableList<TalentReview>> =
            MutableLiveData<MutableList<TalentReview>>()

        nik?.let {
            APIConfig().getService()
                .getTalentReview(compId,it,"Bearer "+token)
                .enqueue(object : Callback<MutableList<TalentReview>> {
                    override fun onFailure(call: Call<MutableList<TalentReview>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<MutableList<TalentReview>>,
                        response: Response<MutableList<TalentReview>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun createTalentReview(postTalentReview: MutableList<Any>?, token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        var par = postTalentReview as MutableList<POSTTalentReview>
            APIConfig().getService()
                .insertTalentReview(par.get(0),"Bearer "+token)
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {

                        if(response.code()==200){
                            mutableLiveData.setValue("Sukses")
                        }
                        else{
                            mutableLiveData.setValue(response.code().toString())

                        }
                    }
                })

        return mutableLiveData
    }
    fun deleteTalentReview(postTalentReview: MutableList<Any>?, token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        var par = postTalentReview as MutableList<POSTTalentReview>
        par.get(0).tr_id?.let {
            APIConfig().getService()
                .deleteTalentReview(it, par.get(0).upd_user.toString(),"Bearer "+token)
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {

                        if(response.code()==200){
                            mutableLiveData.setValue("Sukses")
                        } else{
                            mutableLiveData.setValue(response.code().toString())

                        }
                    }
                })
        }

        return mutableLiveData
    }

    fun getTalentReviewDetail(TRID:String,token:String): MutableLiveData<MutableList<TalentReviewDetail>> {

        val mutableLiveData: MutableLiveData<MutableList<TalentReviewDetail>> =
            MutableLiveData<MutableList<TalentReviewDetail>>()

            APIConfig().getService()
                .getTalentReviewDetail(TRID,"Bearer "+token)
                .enqueue(object : Callback<MutableList<TalentReviewDetail>> {
                    override fun onFailure(call: Call<MutableList<TalentReviewDetail>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<MutableList<TalentReviewDetail>>,
                        response: Response<MutableList<TalentReviewDetail>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })

        return mutableLiveData
    }

    fun getModerator(EMPNIK:String, DIRID:String,token:String): MutableLiveData<MutableList<Moderator>> {

        val mutableLiveData: MutableLiveData<MutableList<Moderator>> =
            MutableLiveData<MutableList<Moderator>>()

        APIConfig().getService()
            .getModerator(EMPNIK,DIRID,"Bearer "+token)
            .enqueue(object : Callback<MutableList<Moderator>> {
                override fun onFailure(call: Call<MutableList<Moderator>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<MutableList<Moderator>>,
                    response: Response<MutableList<Moderator>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }

    fun insertTalentReviewDetail(token:String, talentReviewDetail: TalentReviewDetail): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .postTalentReviewDetail(talentReviewDetail,"Bearer "+token)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Sukses")

                    }
                }
            })

        return mutableLiveData
    }

    fun updateTalentReviewDetail(token:String, talentReviewDetail: TalentReviewDetail): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .updateTalentReviewDetail(talentReviewDetail,"Bearer "+token)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Sukses")

                    }
                }
            })

        return mutableLiveData
    }


    fun deleteTalentReviewDetail(username:String,token:String, talentReviewDetail: TalentReviewGroupsItem): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        talentReviewDetail.phasEID?.let {
            APIConfig().getService()
                .deleteTalentReviewDetail(it,
                    talentReviewDetail.tRGROUPID.toString(),username,"Bearer "+token)
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {

                        if(response.code()==200){
                            mutableLiveData.setValue("Sukses")

                        }
                    }
                })
        }

        return mutableLiveData
    }

    fun insertTalentReviewPhase(token:String, postTalentReviewPhase: POSTTalentReviewPhase): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .insertTalentReviewPhase(postTalentReviewPhase,"Bearer "+token)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Sukses")

                    }
                }
            })

        return mutableLiveData
    }

    fun deleteTalentReviewPhase(token:String, postTalentReviewPhase: POSTTalentReviewPhase): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .deleteTalentReviewPhase(postTalentReviewPhase,"Bearer "+token)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Sukses")

                    }
                }
            })

        return mutableLiveData
    }


    fun getTalentReviewEmployeeFinal(TRGRPID:String,token:String): MutableLiveData<MutableList<EmpTalentReviewFinal>> {

        val mutableLiveData: MutableLiveData<MutableList<EmpTalentReviewFinal>> =
            MutableLiveData<MutableList<EmpTalentReviewFinal>>()

        APIConfig().getService()
            .getTalentReviewEmpFinal(TRGRPID,"Bearer "+token)
            .enqueue(object : Callback<MutableList<EmpTalentReviewFinal>> {
                override fun onFailure(call: Call<MutableList<EmpTalentReviewFinal>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<MutableList<EmpTalentReviewFinal>>,
                    response: Response<MutableList<EmpTalentReviewFinal>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }

    fun getTalentReviewKandidatFinal(TRGRPID:String,token:String): MutableLiveData<MutableList<EmpTalentReviewFinal>> {

        val mutableLiveData: MutableLiveData<MutableList<EmpTalentReviewFinal>> =
            MutableLiveData<MutableList<EmpTalentReviewFinal>>()

        APIConfig().getService()
            .getTalentReviewKandidatFinal(TRGRPID,"Bearer "+token)
            .enqueue(object : Callback<MutableList<EmpTalentReviewFinal>> {
                override fun onFailure(call: Call<MutableList<EmpTalentReviewFinal>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<MutableList<EmpTalentReviewFinal>>,
                    response: Response<MutableList<EmpTalentReviewFinal>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }



    fun insertRekomendasi(token:String, kandidatRekomendasi: KandidatRekomendasi): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .insertRekomendasi(kandidatRekomendasi,"Bearer "+token)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Sukses")

                    }
                }
            })

        return mutableLiveData
    }
    fun hapusRekomendasi(token:String, kandidatRekomendasi: KandidatRekomendasi): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .hapusRekomendasi(kandidatRekomendasi,"Bearer "+token)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Sukses")

                    }
                }
            })

        return mutableLiveData
    }

}