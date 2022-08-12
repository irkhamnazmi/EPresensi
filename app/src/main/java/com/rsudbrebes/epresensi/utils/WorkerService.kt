package com.rsudbrebes.epresensi.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rsudbrebes.epresensi.EPresensi
import java.time.LocalDateTime
import java.util.*

class WorkerService(context: Context, workerParameter: WorkerParameters) :
    Worker(context, workerParameter) {
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private var time: Long = 0




    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        createNotificationChannel()
        doAlarm()
        Log.d("workmanager", "Background Task is running ...")
        return Result.success()
    }

    private fun doAlarm() {
        getTimePicker()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimePicker() {


        for (i in 1..8) {
            when (i) {
                1 -> {
                    getTime(12,48)


                    setAlarm("1","1")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
//                    alarmManager.setRepeating(
//                        AlarmManager.RTC_WAKEUP, time + (5 * 1000), 5 * 1000, pendingIntent
//                    )

                }
                2 -> {
                    getTime(12,49)
                    setAlarm("2","2")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )

                }
                3 -> {
                    getTime(12,50)
                    setAlarm("3","3")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                4 -> {
                    getTime(12,51)
                    setAlarm("4","4")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                5 -> {
                    getTime(12,52)
                    setAlarm("5","5")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                6 -> {
                    getTime(12,53)
                    setAlarm("6","6")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                7 -> {
                    getTime(12,54)
                    setAlarm("7","7")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                8 -> {
                    getTime(12,55)
                    setAlarm("8","8")
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }

            }
            Thread.sleep(7000)
        }

    }

    private fun getTime(hour : Int, minute: Int): Calendar {
        calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

//        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
//        if (System.currentTimeMillis() > time) {
//            // setting time as AM and PM
//            time = time + (1000 * 60 * 60 * 24)
//
//        }
        return calendar
    }

    private fun setAlarm(title: String, content: String): PendingIntent {
        alarmManager =
            applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra("title", title)
        intent.action = title
        intent.putExtra("content", content)
        intent.action = content
        pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        Log.d(ContentValues.TAG, "Alarm is running ${calendar[Calendar.HOUR_OF_DAY]} : ${calendar[Calendar.MINUTE]}")
        return pendingIntent
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "absensionline"
            val description = "Absensi Online RSUD BREBES"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("absensionline", name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }


}