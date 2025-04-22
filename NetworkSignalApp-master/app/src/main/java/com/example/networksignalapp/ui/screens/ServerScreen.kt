package com.example.networksignalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.networksignalapp.R
import com.example.networksignalapp.ui.components.BottomNavigationBar

@Composable
fun ServerScreen(
    onNavigateToOverview: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    val devices = listOf(
        DeviceInfo(1, "Device 1", "192.168.1.100", "00:1A:2B:3C:4D:5E", R.drawable.ic_smartphone),
        DeviceInfo(2, "Device 2", "192.168.1.101", "00:1A:2B:3C:4D:5F", R.drawable.ic_laptop),
        DeviceInfo(3, "Device 3", "192.168.1.102", "00:1A:2B:3C:4D:60", R.drawable.ic_desktop),
        DeviceInfo(4, "Device 4", "192.168.1.103", "00:1A:2B:3C:4D:61", R.drawable.ic_wifi),
        DeviceInfo(5, "Device 5", "192.168.1.104", "00:1A:2B:3C:4D:62", R.drawable.ic_wifi),
        DeviceInfo(6, "Device 6", "192.168.1.105", "00:1A:2B:3C:4D:63", R.drawable.ic_speaker)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "server",
                onOverviewSelected = onNavigateToOverview,
                onServerSelected = {},
                onStatisticsSelected = onNavigateToStatistics
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Server",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Mobile Network Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Mobile Network", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text("Number of connected mobile devices", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    Text("4", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)

                    Button(
                        onClick = { /* Handle */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("See detailed device statistics")
                    }
                }
            }

            // Connected Devices
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Connected Devices", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)

                    devices.forEach { device -> DeviceItem(device) }

                    Button(
                        onClick = { /* Add device */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Add Device")
                    }
                }
            }
        }
    }
}

data class DeviceInfo(
    val id: Int,
    val name: String,
    val ip: String,
    val mac: String,
    val iconRes: Int
)

@Composable
fun DeviceItem(device: DeviceInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = device.iconRes),
                    contentDescription = device.name,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column {
                Text(device.name, color = MaterialTheme.colorScheme.onSurface)
                Text("IP: ${device.ip} MAC: ${device.mac}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}