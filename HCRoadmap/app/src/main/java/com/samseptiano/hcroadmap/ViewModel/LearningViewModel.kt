package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.LDP
import com.samseptiano.hcroadmap.DataClass.Training
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo
import com.samseptiano.hcroadmap.Repo.TrainingRepo


class LearningViewModel(nik:String, token:String, compGrpId:String, type:String,context: Context) : ViewModel() {
    private val trainingRepo: TrainingRepo
    var niks = nik
    var tokens = token
    var compGrpId = compGrpId
    var type = type
    var context = context

    private var mutableLiveDataTraining: MutableLiveData<List<Training>>? = null
    private var mutableLiveDataLDP: MutableLiveData<List<LDP>>? = null
    val LDP: LiveData<List<LDP>>
        get() {
            if (mutableLiveDataLDP == null) {
                trainingRepo.getcontext(context)
                mutableLiveDataLDP = trainingRepo.getLDP(niks,tokens,compGrpId)
            }
            return mutableLiveDataLDP!!
        }

    val Training: LiveData<List<Training>>
        get() {
            if (mutableLiveDataTraining == null) {
                mutableLiveDataTraining = trainingRepo.getTraining(niks,tokens,type)
            }
            return mutableLiveDataTraining!!
        }

    init {
        trainingRepo = TrainingRepo()
    }
}