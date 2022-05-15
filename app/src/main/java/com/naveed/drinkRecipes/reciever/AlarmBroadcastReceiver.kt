package com.naveed.drinkRecipes.reciever

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.naveed.drinkRecipes.MainActivity
import com.naveed.drinkRecipes.R
import java.io.File


class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.e("AlarmBroadcastReceiver", "onReceive: Called" )

        val name = intent?.getStringExtra("name")
        val instruction = intent?.getStringExtra("instruction")
        val path = intent?.getStringExtra("path")

        RunNotification(context!! , name , instruction , path)

    }
    private var contentView: RemoteViews? = null
    private var notification: Notification? = null
    private var notificationManager: NotificationManager? = null
    private val NotificationID = 1005
    private var mBuilder: NotificationCompat.Builder? = null

    @SuppressLint("RemoteViewLayout", "LaunchActivityFromNotification")
    private fun RunNotification(
        context: Context,
        name: String?,
        instruction: String?,
        path: String?
    ) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        mBuilder = NotificationCompat.Builder(
            context,
            "notify_001"
        )
        contentView = RemoteViews(context.packageName, R.layout.custom_notification_layout)
        contentView!!.setImageViewResource(R.id.profile_image, R.mipmap.ic_launcher)
        var pendingIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(
                context, 3, Intent(context, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK),
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        contentView?.setTextViewText(R.id.titleText , name)
        contentView?.setTextViewText(R.id.instructionTxt , instruction)

        if (path != "") {
            var mSaveBit = File(path)
            val filePath = mSaveBit.path
            val bitmap = BitmapFactory.decodeFile(filePath)
            contentView?.setImageViewBitmap(R.id.profile_image , bitmap)
        }

        mBuilder?.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder?.priority = Notification.PRIORITY_HIGH
        mBuilder?.setContent(contentView)
        mBuilder?.setContentIntent(pendingIntent)
        mBuilder?.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notify_001"
            val channel =
                NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager!!.createNotificationChannel(channel)
            mBuilder!!.setChannelId(channelId)
        }
        notification = mBuilder!!.build()
        notificationManager!!.notify(NotificationID, notification)
    }
}