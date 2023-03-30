package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrgStructureRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun uploadOrgStructure(orgStructure: fileOrgStructure,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

            APIConfig().getService()
                .uploadOrgstructure(orgStructure.image,orgStructure.dirid,orgStructure.upduser,orgStructure.filename)
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

    fun getOrgStructure(orgStructure: OrgStructureGet,token:String): MutableLiveData<List<OrgStructureGet>> {

        val mutableLiveData: MutableLiveData<List<OrgStructureGet>> =
            MutableLiveData<List<OrgStructureGet>>()

        APIConfig().getService()
            .getOrgStructure(orgStructure.dirid.toString(),token)
            .enqueue(object : Callback<List<OrgStructureGet>> {
                override fun onFailure(call: Call<List<OrgStructureGet>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<OrgStructureGet>>,
                    response: Response<List<OrgStructureGet>>
                ) {

                        if(response.body()!=null) {
                            mutableLiveData.setValue(response.body())
                        }

                }
            })

        return mutableLiveData
    }
    fun deleteOrgStructure(orgStructure: OrgStructureGet,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()

        APIConfig().getService()
            .deleteOrgStructure(orgStructure,token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        mutableLiveData.setValue("Sukses")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    if(response.code()==200) {
                        mutableLiveData.setValue("Sukses")
                    }

                }
            })

        return mutableLiveData
    }

}