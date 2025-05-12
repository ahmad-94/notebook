package com.ahmad.notebook.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "STOP_ALARM") {
            AlarmReceiver.mediaPlayer?.stop()
            AlarmReceiver.mediaPlayer?.release()
            AlarmReceiver.mediaPlayer = null

            NotificationManagerCompat.from(context).cancel(1001)
        }
    }
}
