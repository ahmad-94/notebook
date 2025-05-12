package com.ahmad.notebook.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ahmad.notebook.R
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {
    val isPersian = Locale.getDefault().language == "fa"

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Reminder"
        var description = intent.getStringExtra("description") ?: "It's time!"

        val channelId = "reminder_channel"

        // Create notification channel
        val channel = NotificationChannel(
            channelId,
            "Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = if (isPersian)  "هشدار!" else "Alarm Reminder!"
            enableLights(true)
            enableVibration(true)
            setSound(
                null,
                null
            )
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        // Start MediaPlayer to play custom sound in a loop
        try {
            val afd = context.resources.openRawResourceFd(R.raw.broken_android_alarm) ?: return

            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

                isLooping = true
                prepare()
                start()
            }

            afd.close()

        } catch (e: Exception) {
            Log.e("AlarmReceiver", "Failed to start MediaPlayer: ${e.message}")
        }



        // Intent to stop the notification
        val stopIntent = Intent(context, NotificationDismissReceiver::class.java).apply {
            action = "STOP_ALARM"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // replace with your icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setFullScreenIntent(null, true)
            .addAction(R.drawable.baseline_stop_circle_24, if (isPersian) "توقف" else "Stop", stopPendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notification)
    }
}
