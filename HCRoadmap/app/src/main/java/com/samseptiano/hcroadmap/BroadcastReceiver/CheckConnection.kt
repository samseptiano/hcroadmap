package com.samseptiano.hcroadmap.BroadcastReceiver

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class CheckConnection{

    fun isConnected(ctx:Context):Boolean{
        val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnectedd: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnectedd
    }



}