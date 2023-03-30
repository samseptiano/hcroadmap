package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.*


class PerfomanceViewModel(nik:String, compGrpId:String, token:String, context:Context) : ViewModel() {
    private val performanceRepo: PerformanceRepo
    var niks = nik
    var compGrpIds = compGrpId

    var tokens = token

    var context = context

    private var mutableLiveData: MutableLiveData<List<PA>>? = null
    private var mutableLiveDataOtherArchievement: MutableLiveData<List<OtherArchievement>>? = null

    val pa: LiveData<List<PA>>
        get() {
            if (mutableLiveData == null) {
                performanceRepo.getcontext(context)
                mutableLiveData = performanceRepo.getPA(niks,tokens)
            }
            return mutableLiveData!!
        }

    val otherArchievement: LiveData<List<OtherArchievement>>
        get() {
            if (mutableLiveDataOtherArchievement == null) {
                performanceRepo.getcontext(context)
                mutableLiveDataOtherArchievement = performanceRepo.getOtherArchievement(niks,compGrpIds,tokens)
            }
            return mutableLiveDataOtherArchievement!!
        }

    init {
        performanceRepo = PerformanceRepo()
    }
}