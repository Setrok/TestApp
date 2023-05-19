package com.example.testapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.example.testapp.constants.BOOT_TIME_KEY
import com.example.testapp.constants.SHARED_PREFERENCES_BOOT
import com.example.testapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCES_BOOT, Context.MODE_PRIVATE)
        val time = sharedPreferences.getLong(BOOT_TIME_KEY, 0L)
        val message = if (time == 0L) "Boot not detected" else time.toString()

        binding.messageTxt.text = message

        val startIntent = Intent(this, BootLogService::class.java)
        startIntent.putExtra("BOOT_MESSAGE", "" + System.currentTimeMillis())

        val mPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            startIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mAlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        mAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            1000*15*60, mPendingIntent
        )
    }
}