package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.TalentMappingRepo
import com.samseptiano.hcroadmap.Repo.TalentReviewRepo


class TalentReviewViewModel(nik:String, token:String, compId:String, posID:String, dirID:String, dirGroupID:String, list:MutableList<Any>?, context: Context) : ViewModel() {
    private val talentReviewRepo: TalentReviewRepo
    var niks = nik
    var tokens = token
    var compId = compId
    var posID = posID
    var dirID = dirID
    var dirGroupID = dirGroupID
    var list = list
    var context = context

    private var mutableLiveData: MutableLiveData<List<CompanyList>>? = null
    private var mutableLiveDataDirektorat: MutableLiveData<List<DirektoratList>>? = null
    private var mutableLiveDataPosisi: MutableLiveData<List<TargetPosisiList>>? = null
    private var mutableLiveDatainsComp: MutableLiveData<MutableList<String>>? = null
    private var mutableLiveDataTalentReview: MutableLiveData<MutableList<TalentReview>>? = null
    private var mutableLiveDataTalentReviewInsert: MutableLiveData<String>? = null


    val company: LiveData<List<CompanyList>>
        get() {
            if (mutableLiveData == null) {
                talentReviewRepo.getcontext(context)
                mutableLiveData = talentReviewRepo.getCompany(niks,tokens,compId)
            }
            return mutableLiveData!!
        }
    val direktorat: LiveData<List<DirektoratList>>
        get() {
            if (mutableLiveDataDirektorat == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDataDirektorat = talentReviewRepo.getdirektorat(niks,tokens,compId)
            }
            return mutableLiveDataDirektorat!!
        }
    val targetposisi: LiveData<List<TargetPosisiList>>
        get() {
            if (mutableLiveDataPosisi == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDataPosisi = talentReviewRepo.gettargetPosisi(niks,tokens,compId)
            }
            return mutableLiveDataPosisi!!
        }

    val insertIntoCompanyList: LiveData<MutableList<String>>
        get() {
            if (mutableLiveDatainsComp == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDatainsComp = talentReviewRepo.insertCompanyList(list as MutableList<CompanyList>)
            }
            return mutableLiveDatainsComp!!
        }

    val insertIntoTargetPosisiList: LiveData<MutableList<String>>
        get() {
            if (mutableLiveDatainsComp == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDatainsComp = talentReviewRepo.insertPosisiList(list as MutableList<TargetPosisiList>)
            }
            return mutableLiveDatainsComp!!
        }

    val insertintoDirektoratList: LiveData<MutableList<String>>
        get() {
            if (mutableLiveDatainsComp == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDatainsComp = talentReviewRepo.insertDirektoratList(list as MutableList<DirektoratList>)
            }
            return mutableLiveDatainsComp!!
        }

    val talentReview: LiveData<MutableList<TalentReview>>
        get() {
            if (mutableLiveDataTalentReview == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDataTalentReview = talentReviewRepo.getTalentReview(niks,tokens,compId)
            }
            return mutableLiveDataTalentReview!!
        }


    val insertTalentReview: LiveData<String>
        get() {
            if (mutableLiveDataTalentReviewInsert == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDataTalentReviewInsert = talentReviewRepo.createTalentReview(list,tokens)
            }
            return mutableLiveDataTalentReviewInsert!!
        }

    val deleteTalentReview: LiveData<String>
        get() {
            if (mutableLiveDataTalentReviewInsert == null) {
                talentReviewRepo.getcontext(context)

                mutableLiveDataTalentReviewInsert = talentReviewRepo.deleteTalentReview(list,tokens)
            }
            return mutableLiveDataTalentReviewInsert!!
        }

    init {
        talentReviewRepo = TalentReviewRepo()
    }
}