package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReviewPhase
import com.samseptiano.hcroadmap.Repo.TalentReviewRepo

class TalentReviewDetailViewModel(NIK:String, DIRID:String,trid:String,username:String?="", token:String,list:MutableList<Any>?, context: Context) : ViewModel() {
    private val talentReviewRepo: TalentReviewRepo
    var NIKS = NIK
    var DIRIDS= DIRID
    var usernames= username

    var trids = trid
    var tokens = token
    var list = list
    var context = context
    private var mutableLiveData: MutableLiveData<List<TalentReviewDetail>>? = null
    private var moderatorLiveData: MutableLiveData<List<Moderator>>? = null
    private var insertDetailLiveData: MutableLiveData<String>? = null
    private var mutableLiveDataEmpFinal: MutableLiveData<List<EmpTalentReviewFinal>>? = null


    val insertKandidatRekomendasi: LiveData<String>
        get() {
            if (insertDetailLiveData == null) {
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.insertRekomendasi(tokens,
                    list?.get(0) as KandidatRekomendasi
                ) as MutableLiveData<String>
            }
            return insertDetailLiveData!!
        }

    val hapusKandidatRekomendasi: LiveData<String>
        get() {
            if (insertDetailLiveData == null) {
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.hapusRekomendasi(tokens,
                    list?.get(0) as KandidatRekomendasi
                ) as MutableLiveData<String>
            }
            return insertDetailLiveData!!
        }
    val getEmpFinal: LiveData<List<EmpTalentReviewFinal>>
        get() {
            if (mutableLiveDataEmpFinal == null) {
                talentReviewRepo.getcontext(context)
                mutableLiveDataEmpFinal = talentReviewRepo.getTalentReviewEmployeeFinal(trids,tokens) as MutableLiveData<List<EmpTalentReviewFinal>>
            }
            return mutableLiveDataEmpFinal!!
        }

    val getKandidatFinal: LiveData<List<EmpTalentReviewFinal>>
        get() {
            if (mutableLiveDataEmpFinal == null) {
                talentReviewRepo.getcontext(context)
                mutableLiveDataEmpFinal = talentReviewRepo.getTalentReviewKandidatFinal(trids,tokens) as MutableLiveData<List<EmpTalentReviewFinal>>
            }
            return mutableLiveDataEmpFinal!!
        }

    val getTalentReviewDetail: LiveData<List<TalentReviewDetail>>
        get() {
            if (mutableLiveData == null) {
                talentReviewRepo.getcontext(context)
                mutableLiveData = talentReviewRepo.getTalentReviewDetail(trids,tokens) as MutableLiveData<List<TalentReviewDetail>>


            }
            return mutableLiveData!!
        }

    val getModerator: LiveData<List<Moderator>>
        get() {
            if (moderatorLiveData == null) {
                talentReviewRepo.getcontext(context)
                moderatorLiveData = talentReviewRepo.getModerator(NIKS,DIRIDS,tokens) as MutableLiveData<List<Moderator>>


            }
            return moderatorLiveData!!
        }

    val insertTalentReviewDetail: LiveData<String>
        get() {
            if (moderatorLiveData == null) {
                var listCast:MutableList<TalentReviewDetail> = list as MutableList<TalentReviewDetail>
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.insertTalentReviewDetail(tokens,listCast.get(0)) as MutableLiveData<String>


            }
            return insertDetailLiveData!!
        }

    val updateTalentReviewDetail: LiveData<String>
        get() {
            if (moderatorLiveData == null) {
                var listCast:MutableList<TalentReviewDetail> = list as MutableList<TalentReviewDetail>
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.updateTalentReviewDetail(tokens,listCast.get(0)) as MutableLiveData<String>


            }
            return insertDetailLiveData!!
        }

    val deleteTalentReviewDetail: LiveData<String>
        get() {
            if (moderatorLiveData == null) {
                var listCast:MutableList<TalentReviewGroupsItem> = list as MutableList<TalentReviewGroupsItem>
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.deleteTalentReviewDetail(usernames?:"",tokens,listCast.get(0)) as MutableLiveData<String>


            }
            return insertDetailLiveData!!
        }

    val insertTalentReviewPhase: LiveData<String>
        get() {
            if (moderatorLiveData == null) {
                var listCast:MutableList<POSTTalentReviewPhase> = list as MutableList<POSTTalentReviewPhase>
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.insertTalentReviewPhase(tokens,listCast.get(0)) as MutableLiveData<String>


            }
            return insertDetailLiveData!!
        }
    val deleteTalentReviewPhase: LiveData<String>
        get() {
            if (moderatorLiveData == null) {
                var listCast:MutableList<POSTTalentReviewPhase> = list as MutableList<POSTTalentReviewPhase>
                talentReviewRepo.getcontext(context)
                insertDetailLiveData = talentReviewRepo.deleteTalentReviewPhase(tokens,listCast.get(0)) as MutableLiveData<String>


            }
            return insertDetailLiveData!!
        }



    init {
        talentReviewRepo = TalentReviewRepo()
    }
}
