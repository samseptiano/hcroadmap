package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.ConstraintResponse
import com.samseptiano.hcroadmap.DataClass.Feedback360
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConstraintRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun constraint(nik:String,token:String,compGrpId:String): MutableLiveData<ConstraintResponse> {

        val mutableLiveData: MutableLiveData<ConstraintResponse> =
            MutableLiveData<ConstraintResponse>()

        nik?.let {
            APIConfig().getService()
                .getConstraint(it,compGrpId,"Bearer "+token)
                .enqueue(object : Callback<ConstraintResponse> {
                    override fun onFailure(call: Call<ConstraintResponse>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<ConstraintResponse>,
                        response: Response<ConstraintResponse>
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