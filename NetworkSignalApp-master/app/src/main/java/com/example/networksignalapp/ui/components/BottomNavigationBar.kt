package com.example.networksignalapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.networksignalapp.R

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onOverviewSelected: () -> Unit,
    onServerSelected: () -> Unit,
    onStatisticsSelected: () -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == "overview",
            onClick = onOverviewSelected,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Overview",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Overview") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        NavigationBarItem(
            selected = selectedTab == "server",
            onClick = onServerSelected,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_server),
                    contentDescription = "Server",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Server") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        NavigationBarItem(
            selected = selectedTab == "statistics",
            onClick = onStatisticsSelected,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = "Statistics",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Statistics") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}