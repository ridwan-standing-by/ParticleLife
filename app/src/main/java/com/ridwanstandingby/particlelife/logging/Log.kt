package com.ridwanstandingby.particlelife.logging

import android.content.Context
import com.bugsnag.android.Bugsnag
import com.ridwanstandingby.particlelife.BuildConfig
import com.ridwanstandingby.particlelife.R

object Log {

    private var bugsnagStarted = false

    fun init(context: Context) {
        if (context.getString(R.string.BUGSNAG_API_KEY) != "NONE" && !BuildConfig.DEBUG) {
            Bugsnag.start(context)
            bugsnagStarted = true
        }
    }

    fun i(message: String) {
        if (bugsnagStarted) {
            Bugsnag.leaveBreadcrumb(message)
        }
    }
}