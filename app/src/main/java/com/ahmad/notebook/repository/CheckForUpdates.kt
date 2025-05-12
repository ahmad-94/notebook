package com.ahmad.notebook.repository

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.net.toUri
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL





fun checkForUpdates(context: Context, currentVersionCode: Int) {
    val url = URL("https://ahmad-94.github.io/notebook/latest_version.json")
    Thread {
        try {
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream = connection.inputStream.bufferedReader().use { it.readText() }

            val jsonObject = JSONObject(inputStream)

            val updateInfo = UpdateInfo(
                versionCode = jsonObject.getInt("version_code"),
                versionName = jsonObject.getString("version_name"),
                updateMessage = jsonObject.getString("update_message"),
                downloadUrl = jsonObject.getString("download_url")
            )

            if (updateInfo.versionCode > currentVersionCode) {
                Handler(Looper.getMainLooper()).post {
                    AlertDialog.Builder(context)
                        .setTitle("Update Available")
                        .setMessage(updateInfo.updateMessage)
                        .setPositiveButton("Update") { _, _ ->
                            val intent = Intent(Intent.ACTION_VIEW, updateInfo.downloadUrl.toUri())
                            context.startActivity(intent)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}

