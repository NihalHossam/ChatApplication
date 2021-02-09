package com.nihal.chatapplication.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *  Triggers Hilt's code generation.
 */
@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}