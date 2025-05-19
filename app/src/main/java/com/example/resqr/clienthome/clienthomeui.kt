package com.example.resqr.clienthome

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.R
import com.example.resqr.utils.supabaseClient
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navHostController: NavHostController) {
    val supabaseClient = supabaseClient
    val clientRepository = ClientRepository(supabaseClient)
    val clientViewModel: ClientViewmodel = viewModel(factory = ClientHomeFactory(clientRepository))
    var isSheetOpen by remember { mutableStateOf(false) }
    var username = "Darwins"
    var bloodgroup by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Hello! $username") },
                colors = TopAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    scrolledContainerColor = Color.Red
                ),
                actions = {
                    Box(
                        content = {
                            Icon(
                                imageVector = Icons.Default.Person2,
                                contentDescription = "",
                                modifier = Modifier
                            )
                        },
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .clickable(
                                true,
                                onClick = {}
                            )
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isSheetOpen = true
                },
                content = {
                    Icon(
                        Icons.Default.MedicalInformation,
                        contentDescription = "Update user medical data",
                        tint = Color.White
                    )
                },
                shape = CircleShape,
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = Color.Red
            )
        },
        content = { innerPadding ->
            Box(Modifier.fillMaxSize(), content = {
                Column(
                    Modifier
                        .padding(innerPadding)
                        .background(Color.Red),
                    content = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clickable(
                                    enabled = true,
                                    onClick = {}
                                )
                                .weight(.5f)
                                .background(Color.Red)
                                .align(Alignment.CenterHorizontally),
                            content = {
                                Text(
                                    "SEND EMERGENCY ALERT",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 32.sp,
                                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                                    )
                                )
                            },
                            contentAlignment = Alignment.Center
                        )
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                                .background(Color.White)
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center,
                            content = {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center), content = {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(.5f),
                                            content = {
                                                AndroidView(
                                                    factory = { context ->
                                                        Configuration.getInstance().userAgentValue =
                                                            context.packageName
                                                        MapView(context).apply {
                                                            setTileSource(TileSourceFactory.MAPNIK)
                                                            controller.setZoom(22.0)
                                                            setMultiTouchControls(true)

                                                            val locationOverlay =
                                                                MyLocationNewOverlay(this).apply { enableMyLocation() }
                                                            overlays.add(locationOverlay)
                                                        }
                                                    }
                                                )
                                            },
                                            shape = RoundedCornerShape(
                                                topStart = 25.dp,
                                                topEnd = 25.dp,
                                                bottomStart = 25.dp,
                                                bottomEnd = 25.dp
                                            )
                                        )
                                        OutlinedButton(
                                            onClick = {},
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(32.dp)
                                                .height(60.dp),
                                            shape = RoundedCornerShape(5.dp),
                                            content = {
                                                Text(
                                                    "My QR Code",
                                                    style = TextStyle(color = Color.Black)
                                                )
                                            }
                                        )
                                    },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                )
                            }
                        )
                    }
                )
            })
            if (isSheetOpen) {
                ModalBottomSheet(
                    modifier = Modifier.padding(innerPadding),
                    onDismissRequest = { isSheetOpen = false },
                    sheetState = rememberModalBottomSheetState(),
                    containerColor = Color.White,
                    content = {
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        OutlinedTextField(
                            value = bloodgroup,
                            onValueChange = { bloodgroup = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    },
                )
            }
        }
    )
}