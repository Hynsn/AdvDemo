package com.hynson.crash

class CrashBHandler(private val orginal: Thread.UncaughtExceptionHandler?) :
    Thread.UncaughtExceptionHandler {
    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        orginal?.uncaughtException(p0, p1)
    }
}
