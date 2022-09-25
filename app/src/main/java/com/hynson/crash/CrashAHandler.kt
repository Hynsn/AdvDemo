package com.hynson.crash

class CrashAHandler(private val original: Thread.UncaughtExceptionHandler?) :
    Thread.UncaughtExceptionHandler {

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        original?.uncaughtException(p0, p1)
    }
}
