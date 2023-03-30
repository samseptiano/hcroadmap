package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.*


class InovasiViewModel(nik:String, compGrpId:String, token:String, context:Context) : ViewModel() {
    private val inovasiRepo: InovasiRepo
    var niks = nik
    var compGrpIds = compGrpId

    var tokens = token

    var context = context

    private var mutableLiveData: MutableLiveData<List<Inovasi>>? = null
    val award: LiveData<List<Inovasi>>
        get() {
            if (mutableLiveData == null) {
                inovasiRepo.getcontext(context)
                mutableLiveData = inovasiRepo.getInovasi(niks,compGrpIds,tokens)
            }
            return mutableLiveData!!
        }

    init {
        inovasiRepo = InovasiRepo()
    }
}