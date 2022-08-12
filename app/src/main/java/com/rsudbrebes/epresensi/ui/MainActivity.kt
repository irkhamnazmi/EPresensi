package com.rsudbrebes.epresensi.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
        if(EPresensi.getApp().getNotif().isNullOrEmpty()) workerService()
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
        val  mRequest = OneTimeWorkRequestBuilder<WorkerService>()
            .build()

        WorkManager.getInstance(this)
            .enqueue(mRequest)
        return EPresensi.getApp().setNotif("running")

    }

//    private fun doAlarm() {
//        getTimePicker()
//        Log.d(TAG, "doAlarm: is running")
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getTimePicker() {
//
//        createNotificationChannel()
//        for (i in 1..8) {
//            when (i) {
//                1 -> {
//                    getTime(10,18)
//
//
//                    setAlarm("1","1")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
////                    alarmManager.setRepeating(
////                        AlarmManager.RTC_WAKEUP, time + (5 * 1000), 5 * 1000, pendingIntent
////                    )
//
//                }
//                2 -> {
//                    getTime(10,19)
//                    setAlarm("2","2")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//
//                }
//                3 -> {
//                    getTime(10,20)
//                    setAlarm("3","3")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//                }
//                4 -> {
//                    getTime(10,21)
//                    setAlarm("4","4")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//                }
//                5 -> {
//                    getTime(10,22)
//                    setAlarm("5","5")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//                }
//                6 -> {
//                    getTime(10,23)
//                    setAlarm("6","6")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//                }
//                7 -> {
//                    getTime(10,24)
//                    setAlarm("7","7")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//                }
//                8 -> {
//                    getTime(10,25)
//                    setAlarm("8","8")
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY, pendingIntent
//                    )
//                }
//
//            }
//            Thread.sleep(7000)
//        }
//
//    }
//
//    private fun getTime(hour : Int, minute: Int): Calendar {
//        calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, hour)
//        calendar.set(Calendar.MINUTE, minute)
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MILLISECOND, 0)
//
////        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
////        if (System.currentTimeMillis() > time) {
////            // setting time as AM and PM
////            time = time + (1000 * 60 * 60 * 24)
////
////        }
//        return calendar
//    }
//
//    private fun setAlarm(title: String, content: String): PendingIntent {
//        alarmManager =
//            applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(applicationContext, AlarmReceiver::class.java)
//        intent.putExtra("title", title)
//        intent.action = title
//        intent.putExtra("content", content)
//        intent.action = content
//        pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
//        Log.d(ContentValues.TAG, "Alarm is running ${calendar[Calendar.HOUR_OF_DAY]} : ${calendar[Calendar.MINUTE]}")
//        return pendingIntent
//    }
//
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name: CharSequence = "absensionline"
//            val description = "Absensi Online RSUD BREBES"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel("absensionline", name, importance)
//            channel.description = description
//            channel.enableLights(true)
//            channel.lightColor = Color.GREEN
//            val notificationManager = applicationContext.getSystemService(
//                NotificationManager::class.java
//            )
//
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    /** TODO WorkerService running every 15 minutes
//    private fun simpleWorker() {
//        val constraints = Constraints.Builder()
//            .setRequiresBatteryNotLow(false)
//            .build()
//
//        val myRequest = PeriodicWorkRequest.Builder(
//            WorkerService::class.java,
//            10,
//            TimeUnit.MINUTES
//        )   .setConstraints(constraints)
//            .build()
//
//        //minimum interval is 15min, just wait 15 min,
//        // I will cut this.. to show you
//        //quickly
//
//        //now is 0:15 let's wait until 0:30min
//
//        WorkManager.getInstance(this)
//            .enqueueUniquePeriodicWork(
//                "my_id",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                myRequest
//            )
//    } */

    fun dispatchAction(i: Int) {
        if (i == 1) {
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

}