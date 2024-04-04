@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.internalstorage

import android.content.Context
// import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.example.internalstorage.ui.theme.InternalStorageTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb

class MainActivity<Button> : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternalStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Task 1 -  Shared Preferences
                    favoriteColorScreen(context = applicationContext)

                    // Task 3 - Internal Storage
                    // NoteApp(context = applicationContext)

                    // Task 4 - External Storage
                    // CameraApp()
                }
            }
        }
    }
}

// Task 1 -  Shared Preferences
@Composable
fun favoriteColorScreen(context: Context) {
    val sharedPreferences =
        remember {
            context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        }

    val favoriteColor by remember {
        mutableStateOf(
            try {
                sharedPreferences.getInt("favoriteColor", Color.White.toArgb())
            } catch (e: ClassCastException) {
                // A String was stored under the "favoriteColor" key
                // Handle this case as you see fit, for example by using a default color
                Color.White.toArgb()
            },
        )
    }

    fun saveFavoriteColor(color: Int) {
        sharedPreferences.edit {
            putInt("favoriteColor", color)
            apply()
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Favorite Color",
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your favorite color is: ${Color(favoriteColor)}",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    ColorPicker(
        selectedColor = Color(favoriteColor),
    ) { color ->
        saveFavoriteColor(color.toArgb())
    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    val colors =
        listOf(
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
            Color.Gray,
            Color.Black,
            Color.White,
        )

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        colors.chunked(3).forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                rowColors.forEach { color ->
                    Box(
                        modifier =
                            Modifier
                                .size(50.dp)
                                .background(color)
                                .clickable { onColorSelected(color) },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (color == selectedColor) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}

/*                  Task 3 - Internal Storage
@Composable
fun NoteApp(context: Context) {
    var newNoteText by remember { mutableStateOf("") }
    var notesList by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input field for new note
        TextField(
            value = newNoteText,
            onValueChange = { newNoteText = it },
            label = { Text("Enter new note") },
            modifier = Modifier.padding(16.dp)
        )

        // Button to save new note
        Button(
            onClick = {
                if (newNoteText.isNotEmpty()) {
                    saveNoteToFile(context, newNoteText)
                    newNoteText = ""
                    notesList = loadNotesFromFiles(context)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Note")
        }

        // Display saved notes
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Saved Notes:", fontSize = 22.sp, color = Color.Green, modifier = Modifier.padding(start = 16.dp).background(color = MaterialTheme.colorScheme.secondary))
            notesList.forEach { note ->
                Text("- $note", modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}

fun saveNoteToFile(context: Context, note: String) {
    try {
        val outputStream: FileOutputStream = context.openFileOutput("notes.txt", Context.MODE_APPEND)
        outputStream.write("$note\n".toByteArray())
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadNotesFromFiles(context: Context): List<String> {
    val notes = mutableListOf<String>()
    try {
        val inputStream: FileInputStream = context.openFileInput("notes.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            notes.add(line!!)
        }
        inputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return notes
}
*/

// block comment

/*
// Task 4 - External Storage
@Composable
fun CameraApp() {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            // The photo was successfully taken, and it's saved to the location represented by 'photoUri'
            // You can now use 'photoUri' to access the photo
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            val photoFile: File? = try {
                createImageFile(context)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    it
                )
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                launcher.launch(intent)
            }
        } else {
            // Handle permission denied scenario
        }
    }

    Column {
        Button(onClick = {
            when {
                ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED -> {
                    permissionLauncher.launch(cameraPermission)
                }
                else -> {
                    permissionLauncher.launch(cameraPermission)
                }
            }
        }) {
            Text("Open Camera")
        }
    }
}
@Throws(IOException::class)
fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}
*/

// ///////////////////////////////////////
