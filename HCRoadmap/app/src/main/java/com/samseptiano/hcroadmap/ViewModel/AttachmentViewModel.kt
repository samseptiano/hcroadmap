package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.AttachmentRepo


class AttachmentViewModel(nik:String, compGrpId:String, obj: Any?, token:String, context:Context) : ViewModel() {
    private val attachmentRepo: AttachmentRepo
    var niks = nik
    var compGrpIds = compGrpId
    var tokens = token
    var objs = obj

    var context = context

    private var mutableLiveData: MutableLiveData<List<Attachment>>? = null
    private var uploadResLiveData: MutableLiveData<String>? = null


    val attachment: LiveData<List<Attachment>>
        get() {
            if (mutableLiveData == null) {
                attachmentRepo.getcontext(context)
                mutableLiveData = attachmentRepo.getAttachment(niks,compGrpIds,tokens)
            }
            return mutableLiveData!!
        }

    val uploadAttachment: LiveData<String>
        get() {
            if (uploadResLiveData == null) {
                attachmentRepo.getcontext(context)
                uploadResLiveData = attachmentRepo.uploadAttachment(objs as AttachmentGet,tokens)
            }
            return uploadResLiveData!!
        }
    val deleteAttachment: LiveData<String>
        get() {
            if (uploadResLiveData == null) {
                attachmentRepo.getcontext(context)
                var aaa = objs as Attachment
                uploadResLiveData = attachmentRepo.deleteAttachment(aaa.UPDUSER.toString(),aaa.attacHID.toString(),aaa.filENAME.toString(),tokens)
            }
            return uploadResLiveData!!
        }
    init {
        attachmentRepo = AttachmentRepo()
    }
}