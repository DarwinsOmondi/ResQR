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
import com.example.resqr.signup.SignUpScreen
import com.example.resqr.ui.theme.ResQRTheme
import com.example.resqr.utils.supabaseClient
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import io.github.jan.supabase.auth.auth
import androidx.compose.runtime.State


class MainActivity : ComponentActivity() {

    private lateinit var qrScanLauncher: ActivityResultLauncher<Intent>
    private val scannedResultState = mutableStateOf<String?>(null)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the ActivityResultLauncher
        qrScanLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val intentResult: IntentResult? =
                    IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                if (intentResult != null) {
                    scannedResultState.value = intentResult.contents // Update the state
                    if (scannedResultState.value != null) {
                        Toast.makeText(this, "Scanned: ${scannedResultState.value}", Toast.LENGTH_SHORT).show()
                    } else {

                    }
                }
            }

        enableEdgeToEdge()

        setContent {
            ResQRTheme {
                val startDestination =
                    if (supabaseClient.auth.currentUserOrNull() != null) "home" else "signin"
                ResQR(startDestination, qrScanLauncher, scannedResultState) // Pass the state
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResQR(
    startDestination: String,
    qrScanLauncher: ActivityResultLauncher<Intent>,
    scannedResult: State<String?>
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("signup") {
            SignUpScreen(navController)
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
    }
}
