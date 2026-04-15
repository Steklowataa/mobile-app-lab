package pl.oleksandra.pam.lab06 // Upewnij się, że paczka jest poprawna

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import pl.oleksandra.pam.lab8.channelID
import pl.oleksandra.pam.lab8.messageExtra
import pl.oleksandra.pam.lab8.notificationID
import pl.oleksandra.pam.lab8.titleExtra

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Możesz zmienić na własną ikonę
            .setContentTitle(intent?.getStringExtra(titleExtra) ?: "Brak tytułu")
            .setContentText(intent?.getStringExtra(messageExtra) ?: "Brak treści")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Ważne dla nowszych Androidów
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}