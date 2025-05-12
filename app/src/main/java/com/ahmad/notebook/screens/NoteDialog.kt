package com.ahmad.notebook.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmad.notebook.data.AlarmReceiver
import com.ahmad.notebook.data.Note
import com.ahmad.notebook.dateUtils.ShowDatePicker
import com.ahmad.notebook.dateUtils.formattedDateTime
import com.ahmad.notebook.viewmodel.NoteViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisplayDialog(
    modifier: Modifier,
    noteViewModel: NoteViewModel,
    showDialog: Boolean,
    noteToEdit: Note? = null,
    localDateTime: LocalDateTime?,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current



    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var isReminderOn by remember { mutableStateOf(false) }
    var alertDateTime by remember { mutableStateOf(localDateTime ?: LocalDateTime.now()) }
    val isPersian = Locale.getDefault().language == "fa"
    var isDescriptionEnabled by remember { mutableStateOf(false) }


    val focusManager = LocalFocusManager.current

    val isOldAndroid = Build.VERSION.SDK_INT < Build.VERSION_CODES.S

    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = if (isOldAndroid) Color.Black else MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = if (isOldAndroid) Color.Black else MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    )





    LaunchedEffect (showDialog) {
        title = noteToEdit?.title ?: ""
        description = noteToEdit?.description ?: ""
        selectedColor = Color(noteToEdit?.color ?: Color.LightGray.toArgb())
        alertDateTime = noteToEdit?.alertDateTime ?: LocalDateTime.now()
        isReminderOn = noteToEdit?.alertDateTime != null
        isDescriptionEnabled = !noteToEdit?.description.isNullOrEmpty()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = if (isPersian) {
                        if (noteToEdit == null) "یادداشت جدید" else "ویرایش یادداشت"
                    } else {
                        if (noteToEdit == null) "Enter Note" else "Edit Note"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = if (isPersian) TextAlign.Center else TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            ,
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = LocalTextStyle.current.copy(
                            textDirection = TextDirection.ContentOrRtl,
                            textAlign = if (isPersian)TextAlign.Right else TextAlign.Left,
                            fontSize = 18.sp,
                        lineHeight = 24.sp
                        ),
                        label = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (isPersian) Alignment.CenterEnd else Alignment.CenterStart
                            ) {
                                Text(
                                    text = if (isPersian) "عنوان" else "Note Title",
                                    textAlign = if (isPersian) TextAlign.Right else TextAlign.Left
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,         // Show "Done" on keyboard
                            keyboardType = KeyboardType.Text    // Or Email, Number, etc.
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        colors = textFieldColors


                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CompositionLocalProvider(
                        LocalLayoutDirection provides if (isPersian) LayoutDirection.Rtl else LayoutDirection.Ltr
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isPersian) "افزودن توضیحات" else "Add Description",
                                modifier = Modifier.weight(1f),
                                textAlign = if (isPersian) TextAlign.Right else TextAlign.Left,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Switch(
                                checked = isDescriptionEnabled,
                                onCheckedChange = { isDescriptionEnabled = it }
                            )
                        }
                    }

                    if (isDescriptionEnabled) {
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            textStyle = LocalTextStyle.current.copy(
                                textDirection = TextDirection.ContentOrRtl,
                                textAlign = if (isPersian)TextAlign.Right else TextAlign.Left,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                            ),
                            label = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = if (isPersian) Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    Text(
                                        text = if (isPersian) "توضیحات" else "Note Description"
                                    )
                                }
                            },


                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,         // Show "Done" on keyboard
                                keyboardType = KeyboardType.Text    // Or Email, Number, etc.
                            ),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = textFieldColors

                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    CompositionLocalProvider(
                        LocalLayoutDirection provides if (isPersian) LayoutDirection.Rtl else LayoutDirection.Ltr
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isPersian) "تنظیم یادآور" else "Set Reminder",
                                modifier = Modifier.weight(1f),
                                textAlign = if (isPersian) TextAlign.Right else TextAlign.Left,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Switch(
                                checked = isReminderOn,
                                onCheckedChange = {
                                    isReminderOn = it
                                    if (it) {
                                        alertDateTime = LocalDateTime.now()
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    MyColorPicker(
                        selectedColor = selectedColor,
                        onSelectedColor = { selectedColor = it }
                    )


                    if (isReminderOn) {
                        Spacer(modifier = Modifier.height(8.dp))

                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.SpaceBetween
                       ) {

                               ShowDatePicker(
                                   initialDate = alertDateTime,
                                   onDateSelected = { selectedDate ->
                                       alertDateTime = alertDateTime
                                           ?.withYear(selectedDate.year)
                                           ?.withMonth(selectedDate.monthValue)
                                           ?.withDayOfMonth(selectedDate.dayOfMonth)
                                   },
                               )



                           Spacer(modifier = Modifier.width(8.dp))

                           // Time Picker Button
                           Button(onClick = {
                               val timePicker = TimePickerDialog(
                                   context,
                                   { _, hour, minute ->
                                       alertDateTime = alertDateTime
                                           ?.withHour(hour)
                                           ?.withMinute(minute)
                                   },
                                   alertDateTime?.hour ?: 0,
                                   alertDateTime?.minute ?: 0,
                                   true
                               )
                               timePicker.show()
                           }) {
                               Text(
                                   text = if (Locale.getDefault().language == "fa") "انتخاب زمان"
                                   else "Select Time",
                                   fontWeight = FontWeight.Bold,
                                   fontSize = 14.sp
                               )
                           }

                       }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Display selected date/time
                        alertDateTime?.let {
                            Text(text = formattedDateTime(it), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    }

                }
            },
            confirmButton = {
                Button(onClick = {
                    val note = Note(
                        id = noteToEdit?.id ?: 0,
                        title = title,
                        description = description,
                        color = selectedColor.toArgb(),
                        alertDateTime = if (isReminderOn) alertDateTime else null
                    )

                    if (noteToEdit == null) {
                        noteViewModel.insertNote(note) { insertedId ->
                            if (isReminderOn) {
                                scheduleAlarm(
                                    context = context,
                                    noteId = insertedId,
                                    title = title,
                                    description = description,
                                    dateTime = alertDateTime
                                )
                            }
                        }
                    } else {
                        noteViewModel.updateNote(note)
                        if (isReminderOn) {
                            scheduleAlarm(
                                context = context,
                                noteId = note.id,
                                title = title,
                                description = description,
                                dateTime = alertDateTime
                            )
                        }
                    }

                    onDismiss()
                },
                       enabled = title.isNotBlank(),
                       colors = ButtonColors(contentColor = Color.White, containerColor = Color(0xFF4CAF50), disabledContainerColor = Color.Gray, disabledContentColor = Color.White)
                ) {
                    Text(
                        text = if (isPersian) "ذخیره یادداشت" else "Save Note",
                        fontSize = 16.sp

                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonColors(contentColor = Color.White,
                                          containerColor = Color(0xFFFF5722),
                                          disabledContainerColor = Color.Gray,
                                          disabledContentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isPersian) "لغو" else "Cancel",
                        fontSize = 16.sp

                    )
                }
            }
        )
    }
}



fun scheduleAlarm(
    context: Context,
    noteId: Int,
    title: String,
    description: String,
    dateTime: LocalDateTime
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("title", title)
        putExtra("description", description)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        noteId, // Must be unique
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val triggerAtMillis = dateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        triggerAtMillis,
        pendingIntent
    )
}

