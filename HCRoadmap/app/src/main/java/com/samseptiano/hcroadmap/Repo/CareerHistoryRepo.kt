package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CareerHistoryRepo {

    lateinit var context:Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getCareerHistory(nik:String,token:String): MutableLiveData<List<CareerHistory>> {

        val mutableLiveData: MutableLiveData<List<CareerHistory>> =
            MutableLiveData<List<CareerHistory>>()

        nik?.let {
            APIConfig().getService()
                .getCareerHistory(it,"Bearer "+token)
                .enqueue(object : Callback<List<CareerHistory>> {
                    override fun onFailure(call: Call<List<CareerHistory>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<CareerHistory>>,
                        response: Response<List<CareerHistory>>
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