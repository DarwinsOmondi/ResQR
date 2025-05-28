package com.example.resqr

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.resqr.QRCode.QRCodeScreen
import com.example.resqr.clienthome.ClientHomeScreen
import com.example.resqr.clientprofile.ProfileScreen
import com.example.resqr.permissionRequest.PermissionRequestScreen
import com.example.resqr.responderhome.ResponderHomeUi
import com.example.resqr.signin.SignInScreen
import com.example.resqr.signup.SignUpScreen
import com.example.resqr.ui.theme.ResQRTheme
import com.example.resqr.ui.theme.ResponderTheme
import com.example.resqr.utils.supabaseClient
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import io.github.jan.supabase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var qrScanLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Register here BEFORE setContent
        qrScanLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val intentResult =
                    IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                if (intentResult != null) {
                    if (intentResult.contents != null) {
                        Toast.makeText(
                            this,
                            "Scanned: ${intentResult.contents}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        enableEdgeToEdge()

        setContent {
            ResQRTheme {
                val startDestination =
                    if (supabaseClient.auth.currentUserOrNull() != null) "home" else "signin"
                ResQR(startDestination, onScanQrCode = {
                    val integrator = IntentIntegrator(this@MainActivity)
                    integrator.setCaptureActivity(CaptureActivity::class.java)
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    integrator.setPrompt("Scan a QR Code")
                    integrator.setCameraId(0)
                    integrator.setBeepEnabled(true)
                    integrator.setBarcodeImageEnabled(true)
                    qrScanLauncher.launch(integrator.createScanIntent())
                })
            }
        }
    }
}

class PortraitCaptureActivity : CaptureActivity()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResQR(startDestination: String, onScanQrCode: () -> Unit) {
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
            ResponderHomeUi(navController, onScanQrCode)
        }
    }
}
