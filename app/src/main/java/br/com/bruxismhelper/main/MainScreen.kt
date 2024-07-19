package br.com.bruxismhelper.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun MainScreen(
    isRegistered: Boolean,
    onNewUser: () -> Unit,
    onUserRegistered: () -> Unit,
) {
    //TODO Show app explanation

    LaunchedEffect(Unit) {
        if (isRegistered) {
            onUserRegistered()
        } else {
            onNewUser()
        }
    }
}