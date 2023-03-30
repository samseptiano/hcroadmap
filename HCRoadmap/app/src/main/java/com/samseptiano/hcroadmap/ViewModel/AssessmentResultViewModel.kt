package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.Repo.AssessmentResultRepo
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo


class AssessmentResultViewModel(nik:String, token:String,context:Context) : ViewModel() {
    private val assessmentResultRepo: AssessmentResultRepo
    var niks = nik
    var tokens = token
    var context = context

    private var mutableLiveData: MutableLiveData<List<AssessmentResult>>? = null
    val assessmentresult: LiveData<List<AssessmentResult>>
        get() {
            if (mutableLiveData == null) {
                assessmentResultRepo.getcontext(context)
                mutableLiveData = assessmentResultRepo.getAssessmentResult(niks,tokens)
            }
            return mutableLiveData!!
        }

    init {
        assessmentResultRepo = AssessmentResultRepo()
    }
}