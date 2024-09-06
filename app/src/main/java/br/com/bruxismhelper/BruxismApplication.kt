package br.com.bruxismhelper

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.auth
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.logcat

@HiltAndroidApp
class BruxismApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initFirebase()
    }

    private fun initLogger() {
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }

    /**
     * [Official documentation](https://firebase.google.com/docs/app-check/android/play-integrity-provider?authuser=0#initialize)
     *
     */
    private fun initFirebase() {
        Firebase.initialize(context = this)

        /**
         * TODO Error getting App Check token; using placeholder token instead.
         *      Error: com.google.firebase.FirebaseException:
         *      Error returned from API. code: 403 body: App attestation failed.
         */
        val integrityProvider = if (BuildConfig.DEBUG) {
            DebugAppCheckProviderFactory.getInstance()
        } else {
            PlayIntegrityAppCheckProviderFactory.getInstance()
        }

        Firebase.appCheck.installAppCheckProviderFactory(integrityProvider)

        val firebaseAuth = Firebase.auth

        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInAnonymously()
                .addOnSuccessListener {
                    logcat { "signInAnonymously:success" }
                }
                .addOnFailureListener { exception ->
                    logcat { "signInAnonymously:failure -> ${exception.message}" }
                }
        }

    }
}

//Icons attributions
//<a href="https://www.flaticon.com/free-icons/sad" title="sad icons">Sad icons created by Freepik - Flaticon</a>