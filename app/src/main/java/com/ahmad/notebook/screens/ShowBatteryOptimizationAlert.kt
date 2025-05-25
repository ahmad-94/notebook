package com.ahmad.notebook.screens


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale


@Composable
fun ShowBatteryOptimizationAlert(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onGoToSettings: () -> Unit
) {

    val isPersian = Locale.getDefault().language == "fa"
    val message = if (isPersian) {
        "برای عملکرد صحیح آلارم‌ها، لطفاً برنامه را از بهینه‌سازی باتری خارج کنید."
    } else {
        "To ensure alarms work properly, please exclude this app from battery optimization."
    }

    val buttonText = if (isPersian) "رفتن به تنظیمات" else "Go to Settings"

    CompositionLocalProvider(
        LocalLayoutDirection provides if (isPersian) LayoutDirection.Rtl else LayoutDirection.Ltr
    )  {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = if (isPersian) "دفترچه یادداشت" else "Notebook")
                },
                text = {
                    Text(text = message)
                },
                confirmButton = {
                    TextButton(onClick = {
                        onGoToSettings()
                    }) {
                        Text(text = buttonText)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                }
            )
        }
    }
}

