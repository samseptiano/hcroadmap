package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerformanceRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getPA(nik:String,token:String): MutableLiveData<List<PA>> {

        val mutableLiveData: MutableLiveData<List<PA>> =
            MutableLiveData<List<PA>>()

        nik?.let {
            APIConfig().getService()
                .getPA(it,"Bearer "+token)
                .enqueue(object : Callback<List<PA>> {
                    override fun onFailure(call: Call<List<PA>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<PA>>,
                        response: Response<List<PA>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }


    fun getOtherArchievement(nik:String,compGrpId:String,token:String): MutableLiveData<List<OtherArchievement>> {

        val mutableLiveData: MutableLiveData<List<OtherArchievement>> =
            MutableLiveData<List<OtherArchievement>>()

        nik?.let {
            APIConfig().getService()
                .getOtherArchievement(it,compGrpId,"Bearer "+token)
                .enqueue(object : Callback<List<OtherArchievement>> {
                    override fun onFailure(call: Call<List<OtherArchievement>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<OtherArchievement>>,
                        response: Response<List<OtherArchievement>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }


}