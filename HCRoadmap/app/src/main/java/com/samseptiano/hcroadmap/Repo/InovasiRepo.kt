package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InovasiRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getInovasi(nik:String,compGrpId:String,token:String): MutableLiveData<List<Inovasi>> {

        val mutableLiveData: MutableLiveData<List<Inovasi>> =
            MutableLiveData<List<Inovasi>>()

        nik?.let {
            APIConfig().getService()
                .getInovasi(it,compGrpId,"Bearer "+token)
                .enqueue(object : Callback<List<Inovasi>> {
                    override fun onFailure(call: Call<List<Inovasi>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<Inovasi>>,
                        response: Response<List<Inovasi>>
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