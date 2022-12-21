package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0



fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, selectedRepository : String, downloadStatus : String) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(applicationContext.getString(R.string.RepositoryKey),selectedRepository)
    contentIntent.putExtra(applicationContext.getString(R.string.StatusKey),downloadStatus)

        val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
        val downloadImg = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.download_finished
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImg)
        .bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channel_id)
    )

        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        .setStyle(bigPicStyle)
        .setLargeIcon(downloadImg)

        .addAction(
            R.drawable.download_finished,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        notify(NOTIFICATION_ID,builder.build())
}


/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}