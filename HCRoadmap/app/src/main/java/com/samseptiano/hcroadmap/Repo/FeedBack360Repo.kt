package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.Feedback360
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedBack360Repo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun feedback(nik:String,token:String,compGrpId:String): MutableLiveData<List<Feedback360>> {

        val mutableLiveData: MutableLiveData<List<Feedback360>> =
            MutableLiveData<List<Feedback360>>()

        nik?.let {
            APIConfig().getService()
                .getFeedback360(it,compGrpId,"Bearer "+token)
                .enqueue(object : Callback<List<Feedback360>> {
                    override fun onFailure(call: Call<List<Feedback360>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<Feedback360>>,
                        response: Response<List<Feedback360>>
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