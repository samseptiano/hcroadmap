package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.Feedback360
import com.samseptiano.hcroadmap.Repo.AssessmentResultRepo
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo
import com.samseptiano.hcroadmap.Repo.FeedBack360Repo


class Feedback360ViewModel(nik:String, token:String,compGrpId:String,context: Context) : ViewModel() {
    private val feedback360Repo: FeedBack360Repo
    var niks = nik
    var tokens = token
    var compGrpId = compGrpId
    var context = context

    private var mutableLiveData: MutableLiveData<List<Feedback360>>? = null
    val feedback: LiveData<List<Feedback360>>
        get() {
            if (mutableLiveData == null) {
                feedback360Repo.getcontext(context)
                mutableLiveData = feedback360Repo.feedback(niks,tokens,compGrpId)
            }
            return mutableLiveData!!
        }

    init {
        feedback360Repo = FeedBack360Repo()
    }
}