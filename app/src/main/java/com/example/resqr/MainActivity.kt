package com.example.resqr

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.resqr.presentation.screen.permissionRequest.PermissionRequestScreen
import com.example.resqr.ui.theme.ResQRTheme
import com.google.zxing.integration.android.IntentResult
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.resqr.data.local.database.PasswordDatabase
import com.example.resqr.presentation.screen.auth.LogIn
import com.example.resqr.presentation.screen.auth.SignUp
import com.example.resqr.presentation.screen.password.SetPasswordScreen
import com.example.resqr.presentation.screen.qr.QRScreen
import com.example.resqr.presentation.screen.responder.ResponderHomeScreen
import com.example.resqr.presentation.screen.sharedScreens.SplashScreen
import com.example.resqr.presentation.screen.victim.AddMedicalDataScreen
import com.example.resqr.presentation.screen.victim.QrLockScreen
import com.example.resqr.presentation.screen.victim.SettingsScreen
import com.example.resqr.presentation.screen.victim.VictimHomeScreen
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : ComponentActivity() {

    private lateinit var qrScanLauncher: ActivityResultLauncher<Intent>
    private val scannedResultState = mutableStateOf<String?>(null)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ResQR)
        super.onCreate(savedInstanceState)
        qrScanLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val intentResult: IntentResult? =
                    IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                if (intentResult != null) {
                    scannedResultState.value = intentResult.contents
                    if (scannedResultState.value != null) {
                        Toast.makeText(
                            this,
                            "Scanned: ${scannedResultState.value}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        PasswordDatabase.getDatabase(this)
        enableEdgeToEdge()
        setContent {
            ResQRTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    val window = (view.context as Activity).window
                    window.statusBarColor = Color.Transparent.toArgb()
                    val useDarkIcons = !isSystemInDarkTheme()
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                        useDarkIcons
                    ResQR()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ResQR() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "splashscreen"
        ) {
            composable("signup") {
                SignUp(navController)
            }
            composable("permission_request") {
                PermissionRequestScreen(navController)
            }
            composable("splashscreen") {
                SplashScreen(navController)
            }
            composable("login") {
                LogIn(navController)
            }
            composable("victim_home_screen") {
                VictimHomeScreen(navController)
            }
            composable("responder_home_screen") {
                ResponderHomeScreen(navController)
            }
            composable("add_medical_data") {
                AddMedicalDataScreen(navController)
            }
            composable("show_qr") {
                QRScreen(navController)
            }
            composable("settings_screen") {
                SettingsScreen(navController)
            }
            composable("set_password_screen") {
                SetPasswordScreen(navController)
            }
            composable("medical_data_lock_screen") {
                QrLockScreen(navController)
            }
        }
    }
}
