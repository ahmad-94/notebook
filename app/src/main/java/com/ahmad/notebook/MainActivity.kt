package com.ahmad.notebook

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ahmad.notebook.data.Note
import com.ahmad.notebook.data.NoteViewModelFactory
import com.ahmad.notebook.repository.NotesRepository
import com.ahmad.notebook.roomdb.NotesDB
import com.ahmad.notebook.screens.DisplayDialog
import com.ahmad.notebook.screens.DisplayNotesList
import com.ahmad.notebook.screens.MainScreen
import com.ahmad.notebook.screens.MyNotesTopAppBar
import com.ahmad.notebook.screens.ShowBatteryOptimizationAlert
import com.ahmad.notebook.ui.theme.RoomNoteBookTheme
import com.ahmad.notebook.viewmodel.NoteViewModel
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }



        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR


        val database = NotesDB.getInstance(applicationContext)
        val repository = NotesRepository(database.noteDao)
        // ViewModelFactory
        val viewModelFactory = NoteViewModelFactory(repository)
        val noteViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NoteViewModel::class.java]


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {

                var showDialog by remember { mutableStateOf(false) }
                var noteToEdit by remember { mutableStateOf<Note?>(null) }
                var localTimeDate by remember { mutableStateOf<LocalDateTime?>(null) }

            RoomNoteBookTheme {

                MainScreen()

                Scaffold (
                    floatingActionButton = {
                        MyFloatBtn(
                        modifier = Modifier.padding(32.dp),
                        noteViewModel,
                        showDialog = showDialog,
                        noteToEdit = noteToEdit,
                        localDateTime = localTimeDate,
                        onDismiss = {
                            noteToEdit = null
                            showDialog = false
                        },
                        onFabClick = {
                            showDialog = true
                            noteToEdit = null
                            localTimeDate = LocalDateTime.now()
                        },
                    )

                                           },
                    topBar = {
                        MyNotesTopAppBar()
                    },
                ) {
                    val notes = noteViewModel.getAllNotes.observeAsState(emptyList())
                        DisplayNotesList(
                            contentPadding = it,
                            notes = notes.value,
                            onNoteClick = {
                                noteToEdit = it
                                showDialog = true
                            },
                            onNoteDelete = {
                                noteViewModel.deleteNote(it)
                            },
                        )
                }
            }


        }
    }
}

@Composable
fun MyFloatBtn(
    modifier: Modifier,
    noteViewModel: NoteViewModel,
    showDialog: Boolean,
    noteToEdit: Note?,
    localDateTime: LocalDateTime?,
    onDismiss: () -> Unit,
    onFabClick: () -> Unit,
) {


    DisplayDialog (
        modifier,
        noteViewModel,
        showDialog,
        noteToEdit,
        localDateTime,
        onDismiss
    )
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = Color(0xFFF44336),
        contentColor = Color.White,
        modifier = Modifier.offset(y = (-12).dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note"
        )
    }
}