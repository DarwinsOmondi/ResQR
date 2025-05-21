package com.example.resqr.clientprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.model.User
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    var supabaseClient = supabaseClient
    val repo = ClientProfileRepository(supabaseClient)
    val viewModel: ClientProfileViewModel = viewModel(factory = ClientProfileFactory(repo))
    val userId = supabaseClient.auth.currentUserOrNull()?.id

    LaunchedEffect(Unit) {
        userId?.let { viewModel.fetchClientMedicalData(it) }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.signOut()
                        navController.navigate("signin") { popUpTo(0) }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingShimmerEffect()
            }

            uiState.fetchSuccess != null -> {
                ProfileContent(
                    user = uiState.fetchSuccess!!,
                    onEditClick = { navController.navigate("edit_profile") },
                    modifier = Modifier.padding(padding)
                )
            }

            uiState.error != null -> {
                ErrorState(
                    errorMessage = uiState.error!!,
                    onRetry = { userId?.let { viewModel.fetchClientMedicalData(it) } },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun LoadingShimmerEffect() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading profile...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
fun ProfileContent(user: User, onEditClick: () -> Unit, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardPadding = (screenWidth * 0.04f).coerceAtLeast(16.dp)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentPadding = PaddingValues(horizontal = cardPadding, vertical = 16.dp)
    ) {
        item {
            // Profile Header with Avatar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Image(
//                    painter = rememberAsyncImagePainter(
//                        user.profilePictureUrl ?: R.drawable.placeholder_avatar
//                    ),
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.surfaceVariant)
//                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = onEditClick, shape = RoundedCornerShape(10.dp), content = {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Profile")
                })
            }
        }
        item {
            UserInfoCard(user)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Medical Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { contentDescription = "Medical Information Section" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            MedicalInfoCard(user)
        }
    }
}

@Composable
fun UserInfoCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "User Information Card" },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    "Name: ${user.fullname}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Email: ${user.email}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Phone: ${user.phone_number}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Role: ${user.role}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun MedicalInfoCard(user: User) {
    val data = user.medicalData

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Medical Information Card" },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.MedicalServices,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    "Blood Type: ${data.blood_type}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Gender: ${data.gender}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Conditions: ${data.conditions}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Allergies",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (data.allergies.isNotEmpty()) {
                    data.allergies.forEach {
                        Text(
                            "- ${it.substance} → ${it.reaction}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Text("None", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Medications",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (data.medications.isNotEmpty()) {
                    data.medications.forEach {
                        Text(
                            "- ${it.name} (${it.dosage}, ${it.frequency}, ${it.duration})",
                            style = MaterialTheme
                                .typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Text("None", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Emergency Contacts",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (data.emergency_contact.isNotEmpty()) {
                    data.emergency_contact.forEach {
                        Text(
                            "- ${it.name}: ${it.phone_number}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Text("None", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}