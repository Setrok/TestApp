package com.example.testapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.testapp.constants.BOOT_TIME_KEY
import com.example.testapp.constants.SHARED_PREFERENCES_BOOT

class BootLogService : Service() {
    private val CHANNEL_ID = "bootLogService"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        Log.d("TrackService", "started")
        val bootMessage = getBootMessage()
        sendData(bootMessage)
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent,  PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText(bootMessage)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun getBootMessage(): String {
        val sharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES_BOOT, Context.MODE_PRIVATE)
        val time = sharedPreferences.getLong(BOOT_TIME_KEY, 0L)
        return if (time == 0L) "Boot not detected" else time.toString()
    }

    private fun sendData(input: String) {
        Intent().run {
            action = MainActivity.ACTION_DATA
            putExtra("inputExtra", input)
            applicationContext.sendBroadcast(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)

    }
}