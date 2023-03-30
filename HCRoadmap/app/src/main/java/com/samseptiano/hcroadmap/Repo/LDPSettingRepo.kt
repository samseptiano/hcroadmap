package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LDPSettingRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getLDPSetting(compGrpId:String,token:String): MutableLiveData<List<LDPSetting>> {

        val mutableLiveData: MutableLiveData<List<LDPSetting>> =
            MutableLiveData<List<LDPSetting>>()


            APIConfig().getService()
                .getldpSetting(compGrpId,"Bearer "+token)
                .enqueue(object : Callback<List<LDPSetting>> {
                    override fun onFailure(call: Call<List<LDPSetting>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<LDPSetting>>,
                        response: Response<List<LDPSetting>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })

        return mutableLiveData
    }

    fun deleteLDPSetting(ldpID:String,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .deleteldpSetting(ldpID,"Bearer "+token)
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

    fun insertLDPSetting(ldpSetting: LDPSetting,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .insertldpSetting(ldpSetting,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    mutableLiveData.setValue("Sukses!")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue("Sukses!")

                    }
                }
            })

        return mutableLiveData
    }


}