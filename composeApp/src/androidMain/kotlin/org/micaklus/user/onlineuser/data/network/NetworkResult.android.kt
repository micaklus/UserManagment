package org.micaklus.user.onlineuser.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.micaklus.user.MyApplication

actual fun isNetworkAvailable(): Boolean {
    return isAndroidNetworkAvailable()
}

fun isAndroidNetworkAvailable(): Boolean {
    val connectivityManager =
        MyApplication.getInstance()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}