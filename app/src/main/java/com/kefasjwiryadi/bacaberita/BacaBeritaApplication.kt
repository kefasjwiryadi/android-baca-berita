package com.kefasjwiryadi.bacaberita

import android.app.Application
import android.content.Context
import timber.log.Timber


class BacaBeritaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private lateinit var context: Context

        fun getContext(): Context {
            return context
        }

        fun getString(stringResId: Int): String {
            return getContext().getString(stringResId)
        }
    }
}