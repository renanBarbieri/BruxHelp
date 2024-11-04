package br.com.bruxismhelper.platform.firebase

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import logcat.logcat

fun Any.logMessage(message: () -> String) {
    logcat { message() }
    Firebase.crashlytics.log(message())
}

fun Any.logNonFatalException(message: () -> String) {
    logcat { message() }
    Firebase.crashlytics.recordException(Throwable(message()))
}