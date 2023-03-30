package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.*


class OrgStructureViewModel(obj:Any, token:String, context:Context) : ViewModel() {
    private val orgStructureRepo: OrgStructureRepo
    var tokens = token
    var context = context
    var obj = obj
    private var getLiveData: MutableLiveData<List<OrgStructureGet>>? = null

    private var uploadResLiveData: MutableLiveData<String>? = null

    val uploadOrgStructure: LiveData<String>
        get() {
            if (uploadResLiveData == null) {
                orgStructureRepo.getcontext(context)
                uploadResLiveData = orgStructureRepo.uploadOrgStructure(obj as fileOrgStructure,tokens)
            }
            return uploadResLiveData!!
        }


    val getOrgStructure: LiveData<List<OrgStructureGet>>
        get() {
            if (getLiveData == null) {
                orgStructureRepo.getcontext(context)
                getLiveData = orgStructureRepo.getOrgStructure(obj as OrgStructureGet,tokens)
            }
            return getLiveData!!
        }

    val deleteOrgStructure: LiveData<String>
        get() {
            if (uploadResLiveData == null) {
                orgStructureRepo.getcontext(context)
                uploadResLiveData = orgStructureRepo.deleteOrgStructure(obj as OrgStructureGet,tokens)
            }
            return uploadResLiveData!!
        }

    init {
        orgStructureRepo = OrgStructureRepo()
    }
}