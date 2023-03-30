package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.ConstraintResponse
import com.samseptiano.hcroadmap.DataClass.Feedback360
import com.samseptiano.hcroadmap.Repo.AssessmentResultRepo
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo
import com.samseptiano.hcroadmap.Repo.ConstraintRepo
import com.samseptiano.hcroadmap.Repo.FeedBack360Repo


class ConstraintViewModel(nik:String, token:String, compGrpId:String, context: Context) : ViewModel() {
    private val constraintRepo: ConstraintRepo
    var niks = nik
    var tokens = token
    var compGrpId = compGrpId
    var context = context

    private var mutableLiveData: MutableLiveData<ConstraintResponse>? = null
    val constraint: LiveData<ConstraintResponse>
        get() {
            if (mutableLiveData == null) {
                constraintRepo.getcontext(context)
                mutableLiveData = constraintRepo.constraint(niks,tokens,compGrpId)
            }
            return mutableLiveData!!
        }

    init {
        constraintRepo = ConstraintRepo()
    }
}