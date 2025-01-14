package com.example.lab6animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedScreen() {
    var isClicked by remember { mutableStateOf(false) }
    var isTextVisible by remember { mutableStateOf(true) }

    val scale by animateFloatAsState(
        targetValue = if (isClicked) 1.5f else 1f,
        animationSpec = tween(durationMillis = 500), label = "Scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isClicked) 360f else 0f,
        animationSpec = tween(durationMillis = 800), label = "Rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.rocket),
            contentDescription = "Rocket Image",
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
                .rotate(rotation)
                .clickable {
                    isClicked = !isClicked
                    isTextVisible = !isTextVisible
                }
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = isTextVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Лившиц Леонид",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = "БПИ 235",
                    fontSize = 22.sp,
                    color = Color.Gray
                )
            }
        }
    }
}