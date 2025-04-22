package com.example.networksignalapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.Text
import com.example.networksignalapp.ui.theme.LightGray

@Composable
fun SanFranciscoMap(modifier : Modifier = Modifier) {
    val imageUrl = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=American+University+of+Beirut,Lebanon" +
            "&zoom=17&size=600x300&maptype=roadmap" +
            "&markers=color:red%7Clabel:A%7CAmerican+University+of+Beirut,Lebanon" +
            "&key=YOUR_API_KEY"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Map of AUB",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "AUB - Beirut",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}