package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssessmentResultRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getAssessmentResult(nik:String,token:String): MutableLiveData<List<AssessmentResult>> {

        val mutableLiveData: MutableLiveData<List<AssessmentResult>> =
            MutableLiveData<List<AssessmentResult>>()

        nik?.let {
            APIConfig().getService()
                .getAssessmentResult(it,"Bearer "+token)
                .enqueue(object : Callback<List<AssessmentResult>> {
                    override fun onFailure(call: Call<List<AssessmentResult>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<AssessmentResult>>,
                        response: Response<List<AssessmentResult>>
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