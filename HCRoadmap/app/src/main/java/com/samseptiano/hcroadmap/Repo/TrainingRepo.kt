package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.LDP
import com.samseptiano.hcroadmap.DataClass.Training
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainingRepo{

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getLDP(nik:String,token:String, compGrpId:String): MutableLiveData<List<LDP>> {

        val mutableLiveData: MutableLiveData<List<LDP>> =
            MutableLiveData<List<LDP>>()

        nik?.let {
            APIConfig().getService()
                .getLDP(it,compGrpId,"Bearer "+token)
                .enqueue(object : Callback<List<LDP>> {
                    override fun onFailure(call: Call<List<LDP>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<LDP>>,
                        response: Response<List<LDP>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun getTraining(nik:String,token:String, trainingType:String): MutableLiveData<List<Training>> {

        val mutableLiveData: MutableLiveData<List<Training>> =
            MutableLiveData<List<Training>>()

        nik?.let {
            APIConfig().getService()
                .getTraining(it,trainingType,"Bearer "+token)
                .enqueue(object : Callback<List<Training>> {
                    override fun onFailure(call: Call<List<Training>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<Training>>,
                        response: Response<List<Training>>
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