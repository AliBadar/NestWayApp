package com.example.hackatonprjoect.core.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.data.repositories.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var repository: NotificationRepository

    override fun onCreate() {
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            try {
                repository.saveFcmToken(applicationContext, token)

            } catch (e: Exception) {
                Log.e("NotificationService", "Token handling failed", e)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e("remoteMessage", "remoteMessage: ${remoteMessage.data}")

        when {

            // Notification message (foreground)
            remoteMessage.notification != null -> {
                remoteMessage.notification?.let {
//                    handleNotificationMessage(it)
                    serviceScope.launch {
                        try {
                            //saveNotificationData(remoteMessage.data, repository)
                            handleDataMessage(remoteMessage.data)
                        } catch (e: Exception) {
                            Log.e("remoteMessage", "exception: ${e.message}")
                        }
                    }
                }
            }
            // Data message (foreground/background)
            remoteMessage.data.isNotEmpty() -> {
                serviceScope.launch {
                    try {
                        //saveNotificationData(remoteMessage.data, repository)
                        handleDataMessage(remoteMessage.data)
                    } catch (e: Exception) {
                        Log.e("remoteMessage", "exception: ${e.message}")
                    }
                }

            }

        }
    }

//    suspend fun saveNotificationData(
//        data: Map<String, String>,
//        repository: NotificationRepository
//    ) {
//        val notificationVO = NotificationVO(
//            pTitle = data["title"] ?: getString(R.string.app_name_hia),
//            messageBody = data["message"] ?: "New notification",
//            pushType = data["type"] ?: "default",
//            pushUrl = data["push_url"] ?: "",
//            nID = data["uid"] ?: "",
//            flightUID = data["uid"] ?: "",
//        )
//        repository.saveNotificationData(notificationVO)
//    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"] ?: getString(R.string.app_name)
        val body = data["message"] ?: "New notification"
        val channelId = data["channel_id"] ?: DEFAULT_CHANNEL_ID


    }



//    private fun showNotification(
//        title: String,
//        body: String,
//        channelId: String,
//        data: Map<String, String> = emptyMap()
//    ) {
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        createNotificationChannel(notificationManager, channelId)
//
//        // Create intent with flight details
//        val intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP
//            putExtra("fromNotification", true)
//            // Add all data payload as extras
////            val types = listOf("content", "Flights")
//            if (data.isNotEmpty()) {
//                data.forEach { (key, value) ->
//                    putExtra(key, value)
//                }
////                putExtra("type", "Flights")
//            } else {
//                putExtra("uid", "303107499")
//                putExtra("type", "Flights")
//            }
//        }
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            System.currentTimeMillis().toInt(),
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Handle click action
//        //val pendingIntent = NotificationRouter.getPendingIntent(this, data)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.hia_default)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
//    }
//
//    private fun createNotificationChannel(
//        notificationManager: NotificationManager,
//        channelId: String
//    ) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "General Notifications",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                description = "General notifications"
//            }
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    companion object {
        const val DEFAULT_CHANNEL_ID = "default_channel"
    }
}