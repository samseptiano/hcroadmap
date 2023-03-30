package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomCategoryRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getCustomCategory(NIK:String,token:String): MutableLiveData<List<CustomCategory>> {

        val mutableLiveData: MutableLiveData<List<CustomCategory>> =
            MutableLiveData<List<CustomCategory>>()


            APIConfig().getService()
                .getCustomCategory(NIK,"Bearer "+token)
                .enqueue(object : Callback<List<CustomCategory>> {
                    override fun onFailure(call: Call<List<CustomCategory>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<CustomCategory>>,
                        response: Response<List<CustomCategory>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })

        return mutableLiveData
    }

    fun getCustomCategoryDetail(tablename:String,token:String): MutableLiveData<List<CustomCategoryDetail>> {

        val mutableLiveData: MutableLiveData<List<CustomCategoryDetail>> =
            MutableLiveData<List<CustomCategoryDetail>>()


        APIConfig().getService()
            .getCustomCategoryDetail(tablename,"Bearer "+token)
            .enqueue(object : Callback<List<CustomCategoryDetail>> {
                override fun onFailure(call: Call<List<CustomCategoryDetail>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<CustomCategoryDetail>>,
                    response: Response<List<CustomCategoryDetail>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }


    fun deleteCustomCategory(customCategory: CustomCategory,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .deleteCustomCategory(customCategory,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    mutableLiveData.setValue("Sukses!")
                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                        mutableLiveData.setValue("Sukses!")
                }
            })

        return mutableLiveData
    }

    fun insertCustomCategory(customCategory: CustomCategory,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .insertCustomCategory(customCategory,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    mutableLiveData.setValue("Sukses!")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                        mutableLiveData.setValue("Sukses!")


                }
            })

        return mutableLiveData
    }

    fun deleteCustomCategoryDetail(customCategoryDetail: CustomCategoryDetail,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .deleteCustomCategoryDetail(customCategoryDetail,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    mutableLiveData.setValue("Sukses!")
                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    mutableLiveData.setValue("Sukses!")
                }
            })

        return mutableLiveData
    }

    fun insertCustomCategoryDetail(customCategoryDetail: CustomCategoryDetail,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .insertCustomCategoryDetail(customCategoryDetail,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    mutableLiveData.setValue("Sukses!")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    mutableLiveData.setValue("Sukses!")


                }
            })

        return mutableLiveData
    }

}