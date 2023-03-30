package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.*


class UserManagementViewModel(userManagement: Any?, pageNumber:String,pageSize:String, token:String, context:Context) : ViewModel() {
    private val userManagementRepo: UserManagementRepo
    var pageNumbers = pageNumber
    var pageSizes = pageSize
    var tokens = token
    var context = context
    var userManagements = userManagement

    private var mutableLiveDataOrg: MutableLiveData<List<Org>>? = null
    private var mutableLiveDataORG: MutableLiveData<List<UsermanagementorgItem>>? = null
    private var mutableLiveDataDIR: MutableLiveData<List<UsermanagementdirItem>>? = null

    private var mutableLiveData: MutableLiveData<List<UserManagement>>? = null
    private var mutableLiveDataInsert: MutableLiveData<String>? = null

    val getOrg: LiveData<List<Org>>
        get() {
            if (mutableLiveDataOrg == null) {
                userManagementRepo.getcontext(context)

                var aaa = userManagements as Org
                mutableLiveDataOrg = userManagementRepo.getOrg(aaa.dirid.toString(),tokens)
            }
            return mutableLiveDataOrg!!
        }

    val userManagement: LiveData<List<UserManagement>>
        get() {
            if (mutableLiveData == null) {
                userManagementRepo.getcontext(context)
                mutableLiveData = userManagementRepo.getUserManagement(pageSizes,pageNumbers,tokens)
            }
            return mutableLiveData!!
        }

    val userManagementDir: LiveData<List<UsermanagementdirItem>>
        get() {
            if (mutableLiveDataDIR == null) {
                userManagementRepo.getcontext(context)
                var xxx = userManagements as UserManagement
                mutableLiveDataDIR = userManagementRepo.getUserManagementDir(xxx.nik.toString(),xxx.compgrpid.toString(),tokens)
            }
            return mutableLiveDataDIR!!
        }
    val userManagementOrg: LiveData<List<UsermanagementorgItem>>
        get() {
            if (mutableLiveDataORG == null) {
                userManagementRepo.getcontext(context)
                var xxx = userManagements as UserManagement
                mutableLiveDataORG = userManagementRepo.getUserManagementOrg(xxx.nik.toString(),xxx.compgrpid.toString(),tokens)
            }
            return mutableLiveDataORG!!
        }
    val insertuserManagement: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                userManagementRepo.getcontext(context)
                mutableLiveDataInsert = userManagementRepo.InsertUserManagement(userManagements as UserManagement,tokens)
            }
            return mutableLiveDataInsert!!
        }
    val deleteuserManagement: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                userManagementRepo.getcontext(context)
                mutableLiveDataInsert = userManagementRepo.deleteUserManagement(userManagements as UserManagement,tokens)
            }
            return mutableLiveDataInsert!!
        }
//
//    val deleteLdpSetting: LiveData<String>
//        get() {
//            var sss = ldpSettings as LDPSetting
//            if (mutableLiveDataInsert == null) {
//                ldpSettingRepo.getcontext(context)
//                mutableLiveDataInsert = ldpSettingRepo.deleteLDPSetting(sss.ldp_id.toString(),tokens)
//            }
//            return mutableLiveDataInsert!!
//        }

    init {
        userManagementRepo = UserManagementRepo()
    }
}