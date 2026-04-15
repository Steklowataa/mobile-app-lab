package pl.oleksandra.pam.lab8

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import pl.oleksandra.pam.lab06.Lab06Theme
import pl.oleksandra.pam.lab06.MainScreen
import pl.oleksandra.pam.lab06.NotificationBroadcastReceiver
import pl.oleksandra.pam.lab06.TodoApplication
import pl.oleksandra.pam.lab06.data.AppContainer

// Przenieś te stałe tutaj, żeby były globalnie dostępne
const val notificationID = 121
const val channelID = "Lab06 channel"
const val titleExtra = "title"
const val messageExtra = "message"

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun scheduleAlarm(delayMillis: Long) {
        // POPRAWKA: Używamy context przekazanego do klasy, a nie null!
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra(titleExtra, "Deadline")
            putExtra(messageExtra, "Zbliża się termin zakończenia zadania")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + delayMillis

        // Ustawiamy alarm
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, channelID)
            .setContentTitle("Proste powiadomienie")
            .setContentText("Zadanie zostało pomyślnie przetworzone!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationID, notification)
    }
}

class Lab8Activity : ComponentActivity() {

    companion object {
        lateinit var container: AppContainer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        container = (this.application as TodoApplication).container

        // TEST: Wywołujemy alarm za 2 sekundy po starcie
        // Musimy utworzyć obiekt NotificationHandler i wywołać metodę
        val handler = NotificationHandler(this)
        handler.scheduleAlarm(2000)

        setContent {
            Lab06Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lab06 channel"
            val descriptionText = "Lab06 is channel for notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH // Zmień na HIGH, żeby wyskoczyło na górze

            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}