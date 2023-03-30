package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.Attachment
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.Repo.AssessmentResultRepo
import com.samseptiano.hcroadmap.Repo.AttachmentRepo
import com.samseptiano.hcroadmap.Repo.AwardRepo
import com.samseptiano.hcroadmap.Repo.CareerHistoryRepo


class AwardViewModel(nik:String, compGrpId:String, pagesize:Int,pagenumber:Int, token:String, context:Context) : ViewModel() {
    private val awardRepo: AwardRepo
    var niks = nik
    var compGrpIds = compGrpId
    var pagesizes = pagesize
    var pagenumbers = pagenumber

    var tokens = token

    var context = context

    private var mutableLiveData: MutableLiveData<List<Award>>? = null
    val award: LiveData<List<Award>>
        get() {
            if (mutableLiveData == null) {
                awardRepo.getcontext(context)
                mutableLiveData = awardRepo.getAward(niks,compGrpIds,pagesizes,pagenumbers,tokens)
            }
            return mutableLiveData!!
        }

    init {
        awardRepo = AwardRepo()
    }
}