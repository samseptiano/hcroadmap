package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo


class DataDiriViewModel(nik:String, token:String,context: Context) : ViewModel() {
    private val careerHistoryRepo: CareerHistoryRepo
    var niks = nik
    var tokens = token
    var context = context

    private var mutableLiveData: MutableLiveData<List<CareerHistory>>? = null
    val careerHistory: LiveData<List<CareerHistory>>
        get() {
            if (mutableLiveData == null) {
                careerHistoryRepo.getcontext(context)
                mutableLiveData = careerHistoryRepo.getCareerHistory(niks,tokens)
            }
            return mutableLiveData!!
        }

    init {
        careerHistoryRepo = CareerHistoryRepo()
    }
}