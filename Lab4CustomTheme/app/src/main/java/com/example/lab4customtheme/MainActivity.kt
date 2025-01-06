package com.example.lab4customtheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab4customtheme.ui.theme.Lab4CustomThemeTheme
import kotlin.random.Random
import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4CustomThemeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var squareColor by remember { mutableStateOf(Color.Red) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var backgroundColor by remember { mutableStateOf(Color.White) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(squareColor, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        squareColor = randomColor()
                        textColor = randomColor()
                        backgroundColor = randomColor()
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Изменить цвет",
                color = textColor,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.leonid_livshits_rocket),
                contentDescription = "Мое фото",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Леонид Лившиц, БПИ 235",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
        }
    }
}

fun randomColor(): Color {
    return Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab4CustomThemeTheme {
        MainScreen()
    }
}