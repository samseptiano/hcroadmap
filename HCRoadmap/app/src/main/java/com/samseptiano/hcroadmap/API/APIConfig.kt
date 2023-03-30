package com.samseptiano.hcroadmap.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {
    // set interceptor

    val baseURL = "http://10.163.202.133:45455/api/"
    val baseURLImage = "http://10.102.9.153/hcroadmap/Uploads/Assessment%20Result/"
    val baseURLStructure = "http://10.102.9.153/hcroadmap/Uploads/OrganizationStructure/"

    var baseURLBGCHECKATTACHMENT = "http://10.102.9.153/hcroadmap/Uploads/Background%20Check%20Attachment/"
    var baseURLATTACHMENT = "http://10.102.9.153/hcroadmap/Uploads/Attachment/"

    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return  okHttpClient
    }
    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getService() = getRetrofit().create(APIInterface::class.java)
}