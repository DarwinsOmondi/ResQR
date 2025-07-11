package com.example.resqr

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.resqr.QRCode.QRCodeScreen
import com.example.resqr.clienthome.ClientHomeScreen
import com.example.resqr.clientprofile.ProfileScreen
import com.example.resqr.permissionRequest.PermissionRequestScreen
import com.example.resqr.responderhome.QRScannerScreen
import com.example.resqr.responderhome.ResponderHomeUi
import com.example.resqr.signin.SignInScreen
import com.example.resqr.ui.theme.ResQRTheme
import com.google.zxing.integration.android.IntentResult
import androidx.compose.runtime.State
import com.example.resqr.presentation.screen.auth.LogIn
import com.example.resqr.presentation.screen.auth.SignUp
import com.example.resqr.presentation.screen.qr.QRScreen
import com.example.resqr.presentation.screen.responder.ResponderHomeScreen
import com.example.resqr.presentation.screen.shared.SplashScreen
import com.example.resqr.presentation.screen.victim.AddMedicalDataScreen
import com.example.resqr.presentation.screen.victim.VictimHomeScreen
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : ComponentActivity() {

    private lateinit var qrScanLauncher: ActivityResultLauncher<Intent>
    private val scannedResultState = mutableStateOf<String?>(null)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ResQR)
        super.onCreate(savedInstanceState)

        // Register the ActivityResultLauncher
        qrScanLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val intentResult: IntentResult? =
                    IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                if (intentResult != null) {
                    scannedResultState.value = intentResult.contents // Update the state
                    if (scannedResultState.value != null) {
                        Toast.makeText(
                            this,
                            "Scanned: ${scannedResultState.value}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                    }
                }
            }

        enableEdgeToEdge()

        setContent {
            ResQRTheme {
                ResQR(qrScanLauncher, scannedResultState)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResQR(
    qrScanLauncher: ActivityResultLauncher<Intent>,
    scannedResult: State<String?>
) {
    val navController = rememberNavController()
//    LaunchedEffect(Unit) {
//        when (supabaseClient.auth.currentUserOrNull()?.userMetadata?.get(
//            "role"
//        )?.toString()?.replace("\"", "") ?: "null"
//        ) {
//            "Victim" -> {
//                navController.navigate("home")
//            }
//
//            "Responder" -> {
//                navController.navigate("responder_home")
//            }
//
//            else -> {
//                navController.navigate("signin")
//            }
//        }
//    }
    NavHost(
        navController = navController,
        startDestination = "splashscreen"
    ) {
        composable("signup") {
            SignUp(navController)
        }
        composable("signin") {
            SignInScreen(navController)
        }
        composable("home") {
            ClientHomeScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("qrcode") {
            QRCodeScreen(navController)
        }
        composable("permission_request") {
            PermissionRequestScreen(navController)
        }
        composable("responder_home") {
            ResponderHomeUi(navController, qrScanLauncher, scannedResult)
        }
        composable("qr_scanner_screen") {
            QRScannerScreen()
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
            AddMedicalDataScreen()
        }
        composable("show_qr") {
            QRScreen()
        }
    }
}
