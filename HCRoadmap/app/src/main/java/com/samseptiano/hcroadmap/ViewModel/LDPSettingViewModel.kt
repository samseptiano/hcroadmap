package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.*


class LDPSettingViewModel(ldpSetting: Any?, compGrpId:String, token:String, context:Context) : ViewModel() {
    private val ldpSettingRepo: LDPSettingRepo
    var compGrpIds = compGrpId
    var tokens = token
    var context = context
    var ldpSettings = ldpSetting

    private var mutableLiveData: MutableLiveData<List<LDPSetting>>? = null
    private var mutableLiveDataInsert: MutableLiveData<String>? = null

    val ldpSetting: LiveData<List<LDPSetting>>
        get() {
            if (mutableLiveData == null) {
                ldpSettingRepo.getcontext(context)
                mutableLiveData = ldpSettingRepo.getLDPSetting(compGrpIds,tokens)
            }
            return mutableLiveData!!
        }

    val insertLdpSetting: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                ldpSettingRepo.getcontext(context)
                mutableLiveDataInsert = ldpSettingRepo.insertLDPSetting(ldpSettings as LDPSetting,tokens)
            }
            return mutableLiveDataInsert!!
        }

    val deleteLdpSetting: LiveData<String>
        get() {
            var sss = ldpSettings as LDPSetting
            if (mutableLiveDataInsert == null) {
                ldpSettingRepo.getcontext(context)
                mutableLiveDataInsert = ldpSettingRepo.deleteLDPSetting(sss.ldp_id.toString(),tokens)
            }
            return mutableLiveDataInsert!!
        }

    init {
        ldpSettingRepo = LDPSettingRepo()
    }
}