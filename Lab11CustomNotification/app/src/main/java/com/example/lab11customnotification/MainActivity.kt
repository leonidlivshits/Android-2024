package com.example.lab11customnotification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val channelId = "custom_notification_channel"
    private val notificationId = 101
    private val requestNotificationPermission = 1
    private val tag = "LibraryLoader"

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNativeLibraries()

        checkNotificationPermission()
        createNotificationChannel()

        val notifyButton: Button = findViewById(R.id.notify_button)
        notifyButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                showNotificationWithSound()
            }
        }
    }

    private fun loadNativeLibraries() {
        try {
            Log.d(tag, "Attempting to load libcolorx-loader.so")
            System.loadLibrary("colorx-loader")
            Log.d(tag, "Successfully loaded libcolorx-loader.so")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(tag, "Failed to load libcolorx-loader.so", e)
        }

        try {
            Log.d(tag, "Attempting to load libhwuiextimpl.so")
            System.loadLibrary("hwuiextimpl")
            Log.d(tag, "Successfully loaded libhwuiextimpl.so")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(tag, "Failed to load libhwuiextimpl.so", e)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestNotificationPermission
                )
            }
        } else {
            Log.d(tag, "Notification permission is not required for API level below 33")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Custom Channel"
            val descriptionText = "Channel for custom notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private suspend fun showNotificationWithSound() {
        withContext(Dispatchers.Main) {
            mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.notification_sound)
            mediaPlayer?.start()

            val stopMusicIntent = Intent(this@MainActivity, StopMusicReceiver::class.java)
            val stopMusicPendingIntent = PendingIntent.getBroadcast(
                this@MainActivity,
                0,
                stopMusicIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Custom Notification")
                .setContentText("This is a custom notification with song Метаморфоза v2 от Loqiemean.")
                .addAction(R.drawable.ic_stop, "Stop Music", stopMusicPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager = NotificationManagerCompat.from(this@MainActivity)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(notificationId, builder.build())
                    Log.d(tag, "Notification sent successfully")
                } else {
                    Log.e(tag, "Notification permission not granted")
                }
            } else {
                notificationManager.notify(notificationId, builder.build())
                Log.d(tag, "Notification sent successfully for API level below 33")
            }

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
        }
    }

    class StopMusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            Log.d("StopMusicReceiver", "Stop Music action received")
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}
