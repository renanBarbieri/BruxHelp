package br.com.bruxismhelper.platform.kotlin

inline fun Boolean.whenTrue(block: () -> Unit): Boolean {
    if(this) block()

    return this
}

inline fun Boolean.whenFalse(block: () -> Unit): Boolean {
    if(this) block()

    return this
}