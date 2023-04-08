package com.rsudbrebes.epresensi.utils

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rsudbrebes.epresensi.EPresensi
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class WorkerService(context: Context, workerParameter: WorkerParameters) :
    Worker(context, workerParameter) {
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent



    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        createNotificationChannel()
        getTimePicker()
//        Log.d("workmanager", "Background Task is running ...")
        return Result.success()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimePicker() {

        for (i in 1 .. 8) {
//            Thread.sleep(1500)
            when (i) {
                1 -> {
                    setAlarm(
                        i,
                        4,
                        50,
                        "Bersiap - siap",
                        "Sebentar lagi Absen Masuk Shift Pagi Pukul 5.00 WIB \n" +
                                "Pastikan koneksi internet terhubung"
                    )


                }
                2 -> {

                    setAlarm(
                        i,
                        5,
                        0,
                        "Waktunya Absensi",
                        "Mulai Absen Masuk Shift Pagi \n"+
                        "Cepat absen sebelum Pukul 7.30 WIB")


                }
                3 -> {
                    setAlarm(
                        i,
                        7,
                        10,
                        "Bersiap - siap",
                        "Sebentar lagi Absen Pulang Shift Malam \n"+
                        "Pastikan koneksi internet terhubung"
                    )


                }
                4 -> {
                    setAlarm(
                        i,
                        7,
                        20,
                        "Waktunya Absensi",
                        "Mulai Absen Pulang Shift Malam \n" +
                                "Cepat absen sebelum Pukul 7.30 WIB")


                }
                5 -> {
                    setAlarm(
                        i,
                        13,
                        50,
                        "Bersiap - siap",
                        "Sebentar lagi Absen Masuk Shift Siang dan Pulang Shift Pagi \n"+
                        "Pastikan koneksi internet terhubung"

                    )
                }
                6 -> {

                    setAlarm(
                        i,
                        14,
                        0,
                        "Waktunya Absensi",
                        "Mulai Absen Masuk Shift Siang dan Pulang Shift Pagi \n"+
                        "Cepat absen sebelum Pukul 14.30 WIB"
                    )



                }
                7 -> {
                    setAlarm(
                        i,
                        20,
                        50,
                        "Bersiap - siap",
                        "Sebentar lagi Absen Masuk Shift Malam dan Shift Siang \n"+
                        "Pastikan koneksi internet terhubung"
                    )

                }
                8 -> {
                    setAlarm(
                        i,
                        21,
                        0,
                        "Waktunya Absensi",
                        "Mulai Absen Masuk Shift Malam dan Pulang Shift Siang \n"+
                        "Cepat absen sebelum pukul 21.30 WIB"
                    )
                }
            }

        }

    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(id:Int, hour: Int, minute: Int, title: String, content: String){

        alarmManager =
            applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra("title", title)
        intent.action = title
        intent.putExtra("content", content)
        intent.action = content
        pendingIntent = PendingIntent.getBroadcast(applicationContext, id, intent, PendingIntent.FLAG_ONE_SHOT)




        calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }



        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            pendingIntent
        )


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
            channel.setSound(
                Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/raw/doorbell"),
                Notification.AUDIO_ATTRIBUTES_DEFAULT
            )
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )


            notificationManager.createNotificationChannel(channel)
        }
    }


}