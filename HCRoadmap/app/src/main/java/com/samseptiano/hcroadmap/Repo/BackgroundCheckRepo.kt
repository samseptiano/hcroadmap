package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.BGCheck
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BackgroundCheckRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun BGCheck(nik:String,token:String,cmpGrpID:String): MutableLiveData<List<BGCheck>> {

        val mutableLiveData: MutableLiveData<List<BGCheck>> =
            MutableLiveData<List<BGCheck>>()

        nik?.let {
            APIConfig().getService()
                .getBGCheck(it,cmpGrpID,"Bearer "+token)
                .enqueue(object : Callback<List<BGCheck>> {
                    override fun onFailure(call: Call<List<BGCheck>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<BGCheck>>,
                        response: Response<List<BGCheck>>
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