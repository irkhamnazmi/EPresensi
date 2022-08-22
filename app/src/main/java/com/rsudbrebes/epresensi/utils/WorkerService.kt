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
        getTimePicker()
        Log.d("workmanager", "Background Task is running ...")
        return Result.success()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimePicker() {

        for (i in 1..8) {

            when (i) {
                1 -> {
                    getTime(4, 50)
                    setAlarm(
                        "Sebentar lagi Absen Masuk Shift Pagi Pukul 5.00 WIB",
                        "Pastikan koneksi internet terhubung"
                    )

                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
//                    alarmManager.set(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//                        pendingIntent
//                    )
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )

                }
                2 -> {
                    getTime(5, 0)
                    setAlarm("Mulai Absen Masuk Shift Pagi", "Cepat absen sebelum Pukul 7.30 WIB")
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )

                }
                3 -> {
                    getTime(7, 10)
                    setAlarm(
                        "Sebentar lagi Absen Pulang Shift Malam",
                        "Pastikan koneksi internet terhubung"
                    )
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                4 -> {
                    getTime(7, 20)
                    setAlarm("Mulai Absen Pulang Shift Malam", "Cepat absen sebelum Pukul 7.30 WIB")
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                5 -> {
                    getTime(13, 50)
                    setAlarm(
                        "Sebentar lagi Absen Masuk Shift Siang dan Pulang Shift Pagi",
                        "Pastikan koneksi internet terhubung"
                    )
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                6 -> {
                    getTime(14, 0)
                    setAlarm(
                        "Mulai Absen Masuk Shift Siang dan Pulang Shift Pagi",
                        "Cepat absen sebelum Pukul 14.30 WIB"
                    )
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                7 -> {
                    getTime(20, 50)
                    setAlarm(
                        "Sebentar lagi Absen Masuk Shift Malam dan Shift Siang",
                        "Pastikan koneksi internet terhubung"
                    )
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }
                8 -> {
                    getTime(21, 0)
                    setAlarm(
                        "Mulai Absen Masuk Shift Malam dan Pulang Shift Siang",
                        "Cepat absen sebelum pukul 21.30 WIB"
                    )
                    if(calendar.before(Calendar.getInstance())){
                        calendar.add(Calendar.DATE,1)
                    }
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent
                    )
                }

            }
            Thread.sleep(3000)
        }

    }

    private fun getTime(hour: Int, minute: Int): Calendar {
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
        Log.d(
            ContentValues.TAG,
            "Alarm is running ${calendar[Calendar.HOUR_OF_DAY]} : ${calendar[Calendar.MINUTE]}"
        )
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