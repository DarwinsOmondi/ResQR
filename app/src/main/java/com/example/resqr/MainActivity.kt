package com.example.resqr

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.resqr.signin.SignInScreen
import com.example.resqr.signup.SignUpScreen
import com.example.resqr.ui.theme.ResQRTheme
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResQRTheme {
                ResQR()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResQR() {
    val startDestination =
        if (supabaseClient.auth.currentSessionOrNull() != null) "home" else "signin"
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
    }
}
