package com.example.lab2textinputapp
import androidx.compose.ui.res.stringResource

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.lab2textinputapp.ui.theme.Lab2TextInputAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2TextInputAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen(navController)
        }
        composable("inputScreen") {
            InputScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val image: Painter = painterResource(id = R.drawable.leonid_livshits_rocket)
        Image(
            painter = image,
            contentDescription = stringResource(id = R.string.image_description),
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    navController.navigate("inputScreen")
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.student_info),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Нажмите на фото, чтобы попасть на страницу с вводом текста")
    }
}

@Composable
fun InputScreen(navController: NavHostController) {
    var textState by remember { mutableStateOf("") }
    var displayText by remember { mutableStateOf("Ваше сообщение") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = displayText, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text("Введите текст") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (textState.isNotBlank()) {
                        displayText = textState
                        textState = ""
                    } else {
                        Toast.makeText(context, "Поле ввода пустое!", Toast.LENGTH_SHORT).show()
                    }
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (textState.isNotBlank()) {
                    displayText = textState
                    textState = ""
                } else {
                    Toast.makeText(context, "Поле ввода пустое!", Toast.LENGTH_SHORT).show()
                }
                focusManager.clearFocus()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Отправить")
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab2TextInputAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
