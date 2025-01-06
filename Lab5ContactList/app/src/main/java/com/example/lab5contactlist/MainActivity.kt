package com.example.lab5contactlist

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lab5contactlist.ui.theme.Lab5ContactListTheme
import androidx.activity.result.contract.ActivityResultContracts
import android.provider.ContactsContract



@Composable
fun ContactListScreen() {
    val context = LocalContext.current
    val contacts by remember { mutableStateOf(getContacts(context.contentResolver)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserProfile()
        Spacer(modifier = Modifier.height(16.dp))
        ContactList(contacts)
    }
}

@Composable
fun UserProfile() {
    val image: Painter = painterResource(id = R.drawable.leonid_livshits_rocket) // ваше фото

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = image,
            contentDescription = "Мое фото с ракетой",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "Леонид Лившиц", fontSize = 22.sp, color = Color.Black)
            Text(text = "БПИ235", fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ContactList(contacts: List<String>) {
    LazyColumn {
        items(contacts) { contact ->
            Text(
                text = contact,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

fun getContacts(contentResolver: ContentResolver): List<String> {
    val contactsList = mutableListOf<String>()
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, null, null, null
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val idSet = mutableSetOf<String>()
        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            if (name != null && !idSet.contains(name)) {
                contactsList.add(name)
                idSet.add(name)
            }
        }
    }
    return contactsList
}
