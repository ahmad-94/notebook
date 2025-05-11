package com.example.notebook.dateUtils

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog
import ir.huri.jcal.JalaliCalendar
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@Composable
fun ShowDatePicker(
    initialDate: LocalDateTime = LocalDateTime.now(),
    onDateSelected: (LocalDateTime) -> Unit,
) {
    val context = LocalContext.current
    val isPersian = Locale.getDefault().language == "fa"
    val openDialog = remember { mutableStateOf(false) }

    val currentDate = Calendar.getInstance().time

    Button(onClick = { openDialog.value = true }) {
        Text(text = if (isPersian) "انتخاب تاریخ" else "Select Date", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }

    if (openDialog.value) {
        if (isPersian) {
            JalaliDatePickerDialog(
                openDialog = openDialog,
                fontSize = 18.sp,
                disableBeforeDate = JalaliCalendar(1403, 10, 15),
                disableAfterDate = JalaliCalendar(1450, 12, 29),
                initialDate = JalaliCalendar(currentDate),
                onSelectDay = {
                    Log.d("JalaliDate", "Selected: ${it.year}-${it.month}-${it.day}")
                },
                onConfirm = { jalaliDate ->
                    openDialog.value = false
                    val gregorian = jalaliDate.toGregorian()
                    val dateTime = LocalDateTime.of(
                        gregorian.get(Calendar.YEAR),
                        gregorian.get(Calendar.MONTH) + 1,
                        gregorian.get(Calendar.DAY_OF_MONTH),
                        0, 0
                    )
                    onDateSelected(dateTime)
                },
                backgroundColor = Color.Transparent,
                textColor = Color.Black,
                cancelBtnColor = Color(0xFFFF5722),
                confirmBtnColor = Color(0xFF4CAF50),
                todayBtnColor = Color(0xFF125487),
                fontFamily = FontFamily.SansSerif,

                )
        } else {

            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    openDialog.value = false
                    val selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
                    onDateSelected(selectedDate)
                },
                initialDate.year,
                initialDate.monthValue - 1,
                initialDate.dayOfMonth
            ).show()
        }
    }
}


fun formattedDateTime(localDateTime: LocalDateTime): String {

    return if (Locale.getDefault().language == "fa") {
        val calendar = GregorianCalendar(
            localDateTime.year,
            localDateTime.monthValue - 1,
            localDateTime.dayOfMonth,
            localDateTime.hour,
            localDateTime.minute
        )
        val persianDate = PersianDate(calendar.time)
        val formatter = PersianDateFormat("l j F Y - H:i")
        formatter.format(persianDate)
    } else {
        localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}