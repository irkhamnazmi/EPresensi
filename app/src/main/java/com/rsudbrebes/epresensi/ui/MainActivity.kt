package com.rsudbrebes.epresensi.ui

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.work.*
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.ActivityMainBinding
import com.rsudbrebes.epresensi.utils.AlarmReceiver
import com.rsudbrebes.epresensi.utils.WorkerService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        if(EPresensi.getApp().getNotif().isNullOrEmpty())
            workerService()
        val pageRequest = intent.getIntExtra("page_request", 0)
        if (pageRequest == 1) {
            val navOption = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentCheck, true)
                .build()

            Navigation.findNavController(findViewById(R.id.homeHostFragment))
                .navigate(R.id.action_check_success, null, navOption)

        }

        binding.swReload.setOnRefreshListener {
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }

    private fun workerService() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        val  mRequest = OneTimeWorkRequestBuilder<WorkerService>()
            .build()

        WorkManager.getInstance(this)
            .enqueue(mRequest)
        return
//        EPresensi.getApp().setNotif("running")

    }



    fun dispatchAction(i: Int) {
        if (i == 1) {
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

}