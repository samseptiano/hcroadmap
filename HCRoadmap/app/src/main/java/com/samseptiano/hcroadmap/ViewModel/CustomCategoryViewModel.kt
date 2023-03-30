package com.samseptiano.hcroadmap.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Repo.*


class CustomCategoryViewModel(customCategory: Any?, empNIK:String, token:String, context:Context) : ViewModel() {
    private val customCategoryRepo: CustomCategoryRepo
    var empNIKs = empNIK
    var tokens = token
    var context = context
    var customCategorys = customCategory

    private var mutableLiveDataDetail: MutableLiveData<List<CustomCategoryDetail>>? = null
    private var mutableLiveData: MutableLiveData<List<CustomCategory>>? = null
    private var mutableLiveDataInsert: MutableLiveData<String>? = null

    val customCategory: LiveData<List<CustomCategory>>
        get() {
            if (mutableLiveData == null) {
                customCategoryRepo.getcontext(context)
                mutableLiveData = customCategoryRepo.getCustomCategory(empNIKs,tokens)
            }
            return mutableLiveData!!
        }

    val customCategoryDetail: LiveData<List<CustomCategoryDetail>>
        get() {
            var tableName = customCategorys as CustomCategoryDetail
            if (mutableLiveDataDetail == null) {
                customCategoryRepo.getcontext(context)
                mutableLiveDataDetail = customCategoryRepo.getCustomCategoryDetail(tableName.tablENAME.toString(),tokens)
            }
            return mutableLiveDataDetail!!
        }
    val insertCustomCategory: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                customCategoryRepo.getcontext(context)
                mutableLiveDataInsert = customCategoryRepo.insertCustomCategory(customCategorys as CustomCategory,tokens)
            }
            return mutableLiveDataInsert!!
        }

    val deleteCustomCategory: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                customCategoryRepo.getcontext(context)
                mutableLiveDataInsert = customCategoryRepo.deleteCustomCategory(customCategorys as CustomCategory,tokens)
            }
            return mutableLiveDataInsert!!
        }

    val deleteCustomCategoryDetail: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                customCategoryRepo.getcontext(context)
                mutableLiveDataInsert = customCategoryRepo.deleteCustomCategoryDetail(customCategorys as CustomCategoryDetail,tokens)
            }
            return mutableLiveDataInsert!!
        }

    val insertCustomCategoryDetail: LiveData<String>
        get() {
            if (mutableLiveDataInsert == null) {
                customCategoryRepo.getcontext(context)
                mutableLiveDataInsert = customCategoryRepo.insertCustomCategoryDetail(customCategorys as CustomCategoryDetail,tokens)
            }
            return mutableLiveDataInsert!!
        }



    init {
        customCategoryRepo = CustomCategoryRepo()
    }
}