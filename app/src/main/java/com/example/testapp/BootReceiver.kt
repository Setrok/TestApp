package com.example.testapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.testapp.constants.BOOT_TIME_KEY
import com.example.testapp.constants.SHARED_PREFERENCES_BOOT

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_BOOT, Context.MODE_PRIVATE)
            sharedPreferences.edit().putLong(BOOT_TIME_KEY, System.currentTimeMillis()).apply()
            Log.i("From BootUp", "onStart" + System.currentTimeMillis())
        }
    }
}