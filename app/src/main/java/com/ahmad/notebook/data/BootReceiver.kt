package com.ahmad.notebook.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ahmad.notebook.repository.NotesRepository.AlarmScheduler


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Thread {
                AlarmScheduler.rescheduleAllAlarms(context)
            }.start()
        }
    }
}
