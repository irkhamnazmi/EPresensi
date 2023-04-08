package com.rsudbrebes.epresensi.utils

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.splashscreen.SplashScreenActivity
import okio.IOException
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Settings.System.getInt(context?.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)==0)notify(context, intent)
        else notify(context, intent)
    }

    private fun notify(context: Context?,  intent: Intent?) {
        val i = Intent(context, SplashScreenActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
        val extras = intent.extras


        try {
            val notificationManager = context?.let { NotificationManagerCompat.from(it) }


            val builder =
                "absensionline".let {
                    NotificationCompat.Builder(context!!, it)
                        .setSmallIcon(R.drawable.logors)
                        .setContentTitle(extras?.getString("title"))
                        .setContentText(extras?.getString("content"))
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.logors
                            )
                        )
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .setSummaryText("RSUD Brebes")
                                .setBigContentTitle(extras?.getString("title"))
                                .bigText(extras?.getString("content"))
                        )
                        .setSound(Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.doorbell}"))
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                }


            val c = Calendar.getInstance()
            Log.d("", "onReceive: masuk ${c.get(Calendar.MINUTE)} : ${c.get(Calendar.SECOND)}")
            extras?.getString("title")?.let { Log.d("", it) }
//            EPresensi.getApp().setNotif("running")
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
//            val mp: MediaPlayer = MediaPlayer.create(context, R.raw.doorbell)
//            mp.start()
            builder.let { notificationManager?.notify(1, it.build()) }


        } catch (e: IOException) {
            Log.d("Error", e.toString())
        }
    }


}