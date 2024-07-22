package br.com.bruxismhelper

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class BruxismApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}

//Icons attributions
//<a href="https://www.flaticon.com/free-icons/sad" title="sad icons">Sad icons created by Freepik - Flaticon</a>