package com.task.movie.utilis

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :23-11-2019 17:27
 * Updated on : 
 * File Name : NetworkConnectivity.kt
 * ClassName : 
 * Module Name : app
 * Desc :  Analyzing the network connection is open, including mobile data connection
 */
object NetworkConnectivity {
    /**
     * Check's the network Connectivity
     *
     * @return  Boolean status of the network connection
     */
    @SuppressWarnings("deprecation")
    internal fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

}