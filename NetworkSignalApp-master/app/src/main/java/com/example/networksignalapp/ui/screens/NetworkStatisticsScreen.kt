package com.example.networksignalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.networksignalapp.ui.components.BarChartView
import com.example.networksignalapp.ui.components.BottomNavigationBar
import com.example.networksignalapp.ui.components.CalendarView
import androidx.compose.ui.draw.clip



@Composable
fun NetworkStatisticsScreen(
    onNavigateToOverview: () -> Unit,
    onNavigateToServer: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("connectivity") }
    var showCalendar by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "statistics",
                onOverviewSelected = onNavigateToOverview,
                onServerSelected = onNavigateToServer,
                onStatisticsSelected = {}
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
            Text("Network Statistics", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)

            if (showCalendar) {
                CalendarView(onApply = { showCalendar = false })
            } else {
                Button(
                    onClick = { showCalendar = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text("Filter by date range")
                }

                TabRow(
                    selectedTabIndex = when (selectedTab) {
                        "connectivity" -> 0
                        "signal" -> 1
                        else -> 2
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(selected = selectedTab == "connectivity", onClick = { selectedTab = "connectivity" }, text = { Text("Connectivity") })
                    Tab(selected = selectedTab == "signal", onClick = { selectedTab = "signal" }, text = { Text("Signal Power") })
                    Tab(selected = selectedTab == "snr", onClick = { selectedTab = "snr" }, text = { Text("SNR/SINR") })
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Clipped Box Example", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}