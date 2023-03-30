package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part

class AttachmentRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getAttachment(nik:String,compGrpId:String,token:String): MutableLiveData<List<Attachment>> {

        val mutableLiveData: MutableLiveData<List<Attachment>> =
            MutableLiveData<List<Attachment>>()

        nik?.let {
            APIConfig().getService()
                .getattachment(it,compGrpId,"Bearer "+token)
                .enqueue(object : Callback<List<Attachment>> {
                    override fun onFailure(call: Call<List<Attachment>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<Attachment>>,
                        response: Response<List<Attachment>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun uploadAttachment(orgStructure: AttachmentGet, token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .uploadAttachment(orgStructure.image,
                orgStructure.extension,
                orgStructure.ATTACH_ID,
                orgStructure.FILE_NAME,
                orgStructure.FILE_DESC,
                orgStructure.COMP_GRP_ID,
                orgStructure.UPDUSER,
                orgStructure.EMPNIK)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    if(response.code()==200){
                        mutableLiveData.setValue("Upload berhasil!")
                    }
                    else{
                        mutableLiveData.setValue("Upload gagal!")

                    }
                }
            })

        return mutableLiveData
    }
    fun deleteAttachment(updUser:String,attachID:String,filename:String,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


            APIConfig().getService()
                .deleteAttachment(attachID,updUser,filename,"Bearer "+token)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })

        return mutableLiveData
    }

}