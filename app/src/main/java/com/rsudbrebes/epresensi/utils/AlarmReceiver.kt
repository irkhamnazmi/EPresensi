package com.rsudbrebes.epresensi.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.auth.AuthActivity
import com.rsudbrebes.epresensi.ui.splashscreen.SplashScreenActivity
import okio.IOException
import java.lang.Integer.parseInt
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, SplashScreenActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0,i,0)
        val extras = intent.extras


        try {
            val notificationManager = context?.let { NotificationManagerCompat.from(it) }

                val builder =
                  "absensionline".let {
                        NotificationCompat.Builder(context!!, it)
                            .setSmallIcon(R.drawable.logors)
                            .setContentTitle(extras?.getString("title"))
                            .setSubText("RSUD Brebes")
                            .setContentText(extras?.getString("content"))
                            .setStyle(NotificationCompat.BigTextStyle()
                                .bigText(extras?.getString("content")))
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent)
                    }




            Log.d("id baru", "onReceive: masuk ")
            EPresensi.getApp().setNotif("running")
            builder?.let { notificationManager?.notify(1, it.build()) }
//            EPresensi.getApp().getNotifId()
//                ?.let { notificationManager?.deleteNotificationChannel(it) }
//            Log.d("id lama", "onReceive: dihapus ")




        }catch (e : IOException){
            Log.d("Error", e.toString())
        }



    }



}