package com.winhtetnaing.thutasone

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class ThutasoneApp : Application(){
    private val TAG = javaClass.simpleName

    companion object {
        const val TAG = "Thutasone App"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        //set in-app defaults
        val remoteConfigDefaults: HashMap<String, Any> = HashMap()
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false)
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0")
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
            "https://play.google.com/store/apps/details?id=com.winhtetnaing.thutasone")

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(60) // fetch every minutes
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d(TAG, "remote config is fetched.")
                    firebaseRemoteConfig.activateFetched()
                }
            }

    }

    fun getContext(): Context {
        return context
    }
}
