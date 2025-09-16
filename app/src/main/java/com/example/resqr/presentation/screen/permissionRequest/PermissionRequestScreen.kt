package com.example.resqr.presentation.screen.permissionRequest

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionRequestScreen(navController: NavHostController) {
    val context = LocalContext.current
    var permissionStatus by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var isRequesting by remember { mutableStateOf(false) }
    val userRole =
        supabaseClient.auth.currentSessionOrNull()?.user?.appMetadata?.get("role").toString()

    // List of permissions to request
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ).apply {
    }.toTypedArray()

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionStatus = result
        isRequesting = false

        val allGranted = result.all { it.value }

        if (allGranted) {
            if (userRole == "Victim") {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            } else {
                navController.navigate("responder_home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }

        } else {
            showSettingsDialog = true
        }
    }

    // Launch permission request on screen load
    LaunchedEffect(Unit) {
        delay(500)
        isRequesting = true
        permissionLauncher.launch(permissions)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRequesting) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Requesting permissions...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text("Permissions Required") },
                text = {
                    Text(
                        "This app needs location, notifications, camera,microphone ,audio recording, and storage permissions to function properly. Please grant them in Settings.",
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            setData(Uri.fromParts("package", context.packageName, null))
                        })
                        showSettingsDialog = false
                    }) {
                        Text("Open Settings")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showSettingsDialog = false
                        // Allow navigation even if permissions are denied
                        if (userRole == "Victim") {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate("responder_home") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }) {
                        Text("Skip")
                    }
                }
            )
        }
    }
}
