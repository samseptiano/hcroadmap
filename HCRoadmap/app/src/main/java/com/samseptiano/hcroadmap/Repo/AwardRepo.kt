package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.Attachment
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AwardRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getAward(nik:String,compGrpId:String, pagesize:Int,pagenumber:Int,token:String): MutableLiveData<List<Award>> {

        val mutableLiveData: MutableLiveData<List<Award>> =
            MutableLiveData<List<Award>>()

        nik?.let {
            APIConfig().getService()
                .getAward(it,compGrpId,pagesize,pagenumber,"Bearer "+token)
                .enqueue(object : Callback<List<Award>> {
                    override fun onFailure(call: Call<List<Award>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<Award>>,
                        response: Response<List<Award>>
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