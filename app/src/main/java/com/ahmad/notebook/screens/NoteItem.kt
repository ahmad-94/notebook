package com.ahmad.notebook.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmad.notebook.data.Note
import ir.huri.jcal.JalaliCalendar
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@Composable
fun DisplayNoteItem(note: Note, onNoteClick: (Note) -> Unit) {
    val isPersian = Locale.getDefault().language == "fa"

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(note.color)),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = note.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = if (isPersian) TextAlign.Right else TextAlign.Left,
                style = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.description,
                modifier = Modifier.fillMaxWidth(),
                textAlign = if (isPersian) TextAlign.Right else TextAlign.Left,
                style = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onNoteClick(note) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Note"
                    )
                }
                DisplayLocalDatetime(note.alertDateTime, note, onNoteClick)
            }
        }
    }
}


@Composable
fun DisplayLocalDatetime(alertDateTime: LocalDateTime?, note: Note, onBellClick: (Note) -> Unit) {

    val isPersian = Locale.getDefault().language == "fa"

    alertDateTime?.let {
        val formatted = if (isPersian) {
            val zoneId = ZoneId.systemDefault()
            val instant = it.atZone(zoneId).toInstant()
            val date = Date.from(instant)
            val jalali = JalaliCalendar(date)

            val hour = it.hour.toString().padStart(2, '0')
            val minute = it.minute.toString().padStart(2, '0')
            "${jalali.year}/${jalali.month}/${jalali.day} $hour:$minute"
        } else {
            it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Alert set",
                tint = Color.DarkGray,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onBellClick(note) }
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = formatted,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}
