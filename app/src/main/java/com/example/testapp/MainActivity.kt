package com.example.testapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    companion object {
        const val ACTION_DATA = "com.example.testapp.DATA"
        private val filters = arrayOf(ACTION_DATA)
        private val intentFilter: IntentFilter by lazy {
            IntentFilter().apply {
                filters.forEach { addAction(it) }
            }
        }

    }
    inner class DataBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_DATA -> showData(intent)
            }
        }
    }

    fun showData(intent: Intent) {
        binding.messageTxt.text = intent.getStringExtra("inputExtra") ?: "Something went wrong"
    }

    private lateinit var broadcastReceiver: MainActivity.DataBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        broadcastReceiver = DataBroadcastReceiver()
        applicationContext.registerReceiver(broadcastReceiver, intentFilter)

        val startIntent = Intent(this, BootLogService::class.java)
        ContextCompat.startForegroundService(applicationContext, startIntent)

        val mPendingIntent = PendingIntent.getService(
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

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(broadcastReceiver)
    }
}