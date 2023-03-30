package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.BGCheck
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.Repo.AssessmentResultRepo
import com.samseptiano.hcroadmap.Repo.BackgroundCheckRepo
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo


class BackGroundCheckViewModel(nik:String, token:String,compGrpId:String,context: Context) : ViewModel() {
    private val backgroundCheckRepo: BackgroundCheckRepo
    var niks = nik
    var tokens = token
    var compGrpId = compGrpId
    var context = context

    private var mutableLiveData: MutableLiveData<List<BGCheck>>? = null
    val bgCheck: LiveData<List<BGCheck>>
        get() {
            if (mutableLiveData == null) {
                backgroundCheckRepo.getcontext(context)
                mutableLiveData = backgroundCheckRepo.BGCheck(niks,tokens,compGrpId)
            }
            return mutableLiveData!!
        }

    init {
        backgroundCheckRepo = BackgroundCheckRepo()
    }
}