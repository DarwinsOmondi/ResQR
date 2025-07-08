package com.example.resqr.presentation.screen.victim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resqr.di.AppModule
import com.example.resqr.presentation.components.both.ActionDisplayerCards
import com.example.resqr.presentation.components.both.BottomNavBar
import com.example.resqr.presentation.components.victim.QuickActionCardVictim
import com.example.resqr.presentation.components.victim.VoiceCommandsCard
import com.example.resqr.presentation.viewmodel.UserViewModel

@Composable
fun VictimHomeScreen(navController: NavController) {
    val userViewModel: UserViewModel = AppModule.userViewModel
    VictimHomeScreenContent(userViewModel, navController)
}

@Composable
fun VictimHomeScreenContent(userViewModel: UserViewModel, navController: NavController) {
    val userState by userViewModel.userState.collectAsState()
    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }
    val actions = remember {
        listOf(
            Triple(Icons.Default.Person, "Medical Profile", "Medical Profile"),
            Triple(Icons.Default.QrCode, "Share QR Code", "Share QR Code"),
            Triple(Icons.Default.Phone, "Call 911", "Call 911"),
            Triple(Icons.Default.LocationOn, "Update Location", "Update Location")
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionDisplayerCards(
                icon = Icons.Default.Favorite,
                contentDescription = "Person Icon",
                title = "Emergency Assistant",
                content = "Tap to speak for",
                iconColor = Color.White,
                cardColor = Color.Red,
                onCardClick = {}
            )
            ActionDisplayerCards(
                icon = Icons.Default.WarningAmber,
                contentDescription = "Emergency Icon",
                title = "Emergency",
                content = "Tap to initiate",
                iconColor = Color.White,
                cardColor = Color.Red,
                onCardClick = {}
            )
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(actions.size) { index ->
                    val action = actions[index]
                    QuickActionCardVictim(
                        icon = actions[index].first,
                        contentDescription = actions[index].second,
                        title = actions[index].third,
                        iconColor = Color(0xFF1976D2),
                        cardColor = Color.White,
                        onCardClick = {
                            when (action.third) {
                                "Medical Profile" -> navController.navigate(Routes.MEDICAL_PROFILE)
                                "Share QR Code" -> navController.navigate("share_qr")
                                "Call 911" -> {
                                    navController.navigate("call_911")
                                }

                                "Update Location" -> navController.navigate("update_location")
                            }
                        }
                    )
                }
            }
            VoiceCommandsCard(
                onVoiceCommandClick = {}
            )
        }
    }
}


object Routes {
    const val MEDICAL_PROFILE = "add_medical_data"
    const val SHARE_QR = "share_qr"
    const val CALL_911 = "call_911"
    const val UPDATE_LOCATION = "update_location"
}