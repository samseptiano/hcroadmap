package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.TalentMappingRepo


class TalentMappingViewModel(nik:String, token:String,compId:String,posID:String,dirID:String,dirGroupID:String, list:MutableList<Any>?, context: Context) : ViewModel() {
    private val talentMappingRepo: TalentMappingRepo
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
    private var mutableLiveDataEmp: MutableLiveData<List<EmpTalentMappingAfter>>? = null

    private var mutableLiveDatainsComp: MutableLiveData<MutableList<String>>? = null
    private var mutableLiveDatinsEmp: MutableLiveData<MutableList<EmpList>>? = null
    private var mutableLiveDataOrgStructure: MutableLiveData<MutableList<OrgStructure>>? = null


    val company: LiveData<List<CompanyList>>
        get() {
            if (mutableLiveData == null) {
                talentMappingRepo.getcontext(context)
                mutableLiveData = talentMappingRepo.getCompany(niks,tokens,compId)
            }
            return mutableLiveData!!
        }
    val direktorat: LiveData<List<DirektoratList>>
        get() {
            if (mutableLiveDataDirektorat == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDataDirektorat = talentMappingRepo.getdirektorat(niks,tokens,compId)
            }
            return mutableLiveDataDirektorat!!
        }
    val direktoratAll: LiveData<List<DirektoratList>>
        get() {
            if (mutableLiveDataDirektorat == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDataDirektorat = talentMappingRepo.getdirektoratAll(niks,tokens,compId)
            }
            return mutableLiveDataDirektorat!!
        }
    val targetposisi: LiveData<List<TargetPosisiList>>
        get() {
            if (mutableLiveDataPosisi == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDataPosisi = talentMappingRepo.gettargetPosisi(niks,tokens,compId)
            }
            return mutableLiveDataPosisi!!
        }

    val empAftermapping: LiveData<List<EmpTalentMappingAfter>>
        get() {
            if (mutableLiveDataEmp == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDataEmp = talentMappingRepo.getEmpAfterMapping(niks,tokens,compId,posID,dirID,dirGroupID)
            }
            return mutableLiveDataEmp!!
        }



    val insertIntoCompanyList: LiveData<MutableList<String>>
        get() {
            if (mutableLiveDatainsComp == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDatainsComp = talentMappingRepo.insertCompanyList(list as MutableList<CompanyList>)
            }
            return mutableLiveDatainsComp!!
        }

    val insertIntoTargetPosisiList: LiveData<MutableList<String>>
        get() {
            if (mutableLiveDatainsComp == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDatainsComp = talentMappingRepo.insertPosisiList(list as MutableList<TargetPosisiList>)
            }
            return mutableLiveDatainsComp!!
        }

    val insertintoDirektoratList: LiveData<MutableList<String>>
        get() {
            if (mutableLiveDatainsComp == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDatainsComp = talentMappingRepo.insertDirektoratList(list as MutableList<DirektoratList>)
            }
            return mutableLiveDatainsComp!!
        }

    val insertintoEmpList: LiveData<MutableList<EmpList>>
        get() {
            if (mutableLiveDatinsEmp == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDatinsEmp = talentMappingRepo.insertToEmpList(list as MutableList<EmpTalentMappingAfter>)
            }
            return mutableLiveDatinsEmp!!
        }


    val orgStructure: LiveData<MutableList<OrgStructure>>
        get() {
            if (mutableLiveDataOrgStructure == null) {
                talentMappingRepo.getcontext(context)

                mutableLiveDataOrgStructure = talentMappingRepo.getOrgStructure(tokens,dirID)
            }
            return mutableLiveDataOrgStructure!!
        }

    init {
        talentMappingRepo = TalentMappingRepo()
    }
}