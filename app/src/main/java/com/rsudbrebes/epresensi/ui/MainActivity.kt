package com.rsudbrebes.epresensi.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.work.*
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.ActivityMainBinding
import com.rsudbrebes.epresensi.utils.WorkerService
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding




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

    @RequiresApi(Build.VERSION_CODES.M)
     fun workerService() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        val  mRequest = PeriodicWorkRequestBuilder<WorkerService>(15, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
            .build()
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("sendLogs", ExistingPeriodicWorkPolicy.REPLACE,mRequest)
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