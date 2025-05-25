package com.ahmad.notebook.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@SuppressLint("BatteryLife")
@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showBatteryDialog by remember { mutableStateOf(false) }
    var triggerCheckBattery by remember { mutableStateOf(false) }

    // Launcher to request battery optimization exclusion
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // Trigger delayed battery status check
        triggerCheckBattery = true
    }

    // Run initial check on first launch
    LaunchedEffect(Unit) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
        showBatteryDialog = !isIgnoring
    }

    // Delayed battery optimization check after returning from settings
    LaunchedEffect(triggerCheckBattery) {
        if (triggerCheckBattery) {
            kotlinx.coroutines.delay(1500)
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
            showBatteryDialog = !isIgnoring
            triggerCheckBattery = false
        }
    }

    // Show the alert dialog if needed
    ShowBatteryOptimizationAlert(
        showDialog = showBatteryDialog,
        onDismiss = { showBatteryDialog = false },
        onGoToSettings = {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = ("package:" + context.packageName).toUri()
            }
            launcher.launch(intent)
        }
    )
}
