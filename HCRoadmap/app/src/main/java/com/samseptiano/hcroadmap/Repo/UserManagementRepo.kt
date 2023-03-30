package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManagementRepo {

    lateinit var context: Context
    fun getcontext(context: Context){
        this.context = context
    }
    fun getUserManagement(pageSize:String,pageNumber:String,token:String): MutableLiveData<List<UserManagement>> {

        val mutableLiveData: MutableLiveData<List<UserManagement>> =
            MutableLiveData<List<UserManagement>>()


        APIConfig().getService()
            .getUserManagement(pageSize,pageNumber,"Bearer "+token)
            .enqueue(object : Callback<List<UserManagement>> {
                override fun onFailure(call: Call<List<UserManagement>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<UserManagement>>,
                    response: Response<List<UserManagement>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }
    fun getUserManagementDir(nik:String,compGrpid:String,token:String): MutableLiveData<List<UsermanagementdirItem>> {

        val mutableLiveData: MutableLiveData<List<UsermanagementdirItem>> =
            MutableLiveData<List<UsermanagementdirItem>>()


        APIConfig().getService()
            .getUserManagementDir(nik,compGrpid,"Bearer "+token)
            .enqueue(object : Callback<List<UsermanagementdirItem>> {
                override fun onFailure(call: Call<List<UsermanagementdirItem>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<UsermanagementdirItem>>,
                    response: Response<List<UsermanagementdirItem>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }
    fun getUserManagementOrg(nik:String,compGrpid:String,token:String): MutableLiveData<List<UsermanagementorgItem>> {

        val mutableLiveData: MutableLiveData<List<UsermanagementorgItem>> =
            MutableLiveData<List<UsermanagementorgItem>>()


        APIConfig().getService()
            .getUserManagementOrg(nik,compGrpid,"Bearer "+token)
            .enqueue(object : Callback<List<UsermanagementorgItem>> {
                override fun onFailure(call: Call<List<UsermanagementorgItem>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<UsermanagementorgItem>>,
                    response: Response<List<UsermanagementorgItem>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }


    fun getOrg(dirid:String,token:String): MutableLiveData<List<Org>> {

        val mutableLiveData: MutableLiveData<List<Org>> =
            MutableLiveData<List<Org>>()


        APIConfig().getService()
            .getOrg(dirid,"Bearer "+token)
            .enqueue(object : Callback<List<Org>> {
                override fun onFailure(call: Call<List<Org>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<Org>>,
                    response: Response<List<Org>>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue(response.body())

                    }
                }
            })

        return mutableLiveData
    }


    fun InsertUserManagement(userManagement: UserManagement,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .insertUserManagement(userManagement,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    mutableLiveData.setValue("Sukses!")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue("Sukses!")

                    }
                }
            })

        return mutableLiveData
    }

    fun deleteUserManagement(userManagement: UserManagement,token:String): MutableLiveData<String> {

        val mutableLiveData: MutableLiveData<String> =
            MutableLiveData<String>()


        APIConfig().getService()
            .deleteUserManagement(userManagement,"Bearer "+token)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    mutableLiveData.setValue("Sukses!")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    if(response.body()!=null){
                        mutableLiveData.setValue("Sukses!")

                    }
                }
            })

        return mutableLiveData
    }


}