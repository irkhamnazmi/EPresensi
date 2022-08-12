package com.rsudbrebes.epresensi

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.multidex.MultiDexApplication
import com.rsudbrebes.epresensi.network.HttpClient

class EPresensi : MultiDexApplication() {

    companion object {
        lateinit var instance : EPresensi

        fun getApp() : EPresensi{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getPreference() : SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

//    fun setToken(token:String) {
//        getPreference().edit().putString("PREFERENCE_TOKEN", token).apply()
//        HttpClient.getInstance().buildRetrofitClient(token)
//    }
//    fun getToken():String? {
//        return getPreference().getString("PREFERENCE_TOKEN",null)
//    }
//
    fun setUser(user:String){
        getPreference().edit().putString("PREFERENCE_USER", user).apply()
        HttpClient.getInstance().buildRetrofitClient(user)
    }

    fun getUser() :String?{
        return getPreference().getString("PREFERENCE_USER", null)
    }

    fun setActive(active:String){
        getPreference().edit().putString("PREFERENCE_ACTIVE", active).apply()
    }

    fun getActive() :String?{
        return getPreference().getString("PREFERENCE_ACTIVE", null)
    }

    fun setNotif(notif: String) {
        getPreference().edit().putString("PREFERENCE_NOTIF", notif).apply()
    }

    fun getNotif() : String?{
        return getPreference().getString("PREFERENCE_NOTIF", null)
    }

}