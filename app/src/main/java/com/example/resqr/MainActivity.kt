package com.example.resqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.resqr.clienthome.ClientHomeScreen
import com.example.resqr.signin.SignInScreen
import com.example.resqr.signup.SignUpScreen
import com.example.resqr.ui.theme.ResQRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResQRTheme {
                val navController = rememberNavController()
                 ClientHomeScreen(navController)
                //ResQR()
            }
        }
    }
}

@Composable
fun ResQR() {
    val startDestination = "signup"
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
    }
}
