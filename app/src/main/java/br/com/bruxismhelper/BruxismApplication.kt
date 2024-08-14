package br.com.bruxismhelper

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class BruxismApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initFirebase()
    }

    private fun initLogger() {
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }

    private fun initFirebase(){
        val integrityProvider = if(BuildConfig.DEBUG) {
            DebugAppCheckProviderFactory.getInstance()
        } else {
            PlayIntegrityAppCheckProviderFactory.getInstance()
        }

        Firebase.appCheck.installAppCheckProviderFactory(integrityProvider)
        Firebase.initialize(context = this)
    }
}

//Icons attributions
//<a href="https://www.flaticon.com/free-icons/sad" title="sad icons">Sad icons created by Freepik - Flaticon</a>