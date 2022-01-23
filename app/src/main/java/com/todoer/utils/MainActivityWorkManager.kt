package com.todoer.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MainActivityWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val CHANNEL_ID = "MainActivity"

    val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_media_play)
        .setContentTitle("Hello World")
        .setContentText("This is your test notification")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val titleInput =
            inputData.getString("title") ?: return Result.failure()
        val detailInput =
            inputData.getString("detail") ?: return Result.failure()

        notificationBuilder.setContentTitle(titleInput)
            .setContentText(detailInput)

        createNotificationChannel()

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, notificationBuilder.build())
        }

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Test Notification",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "This is your MainActivity's test channel"
        }
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}