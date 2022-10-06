package notification

import activitiy.DashBoardActivity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.interviewreminderapp.R
import viewmodel.NOTIFICATION_REPLY

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent:Intent?) {
        val i = Intent(context,DashBoardActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,i,0)
        val remoteInput = RemoteInput.Builder(NOTIFICATION_REPLY).setLabel("Send Reminder").build()
        val action = NotificationCompat.Action.Builder(R.drawable.notificationbell,"Set Reminder",pendingIntent).addRemoteInput(remoteInput).build()
        val builder = NotificationCompat.Builder(context!!,"InterviewReminder")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Interview Reminder")
            .setContentText("Interview Reminder Received")
            .setAutoCancel(true)
            .addAction(action)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationsManager = NotificationManagerCompat.from(context)
        notificationsManager.notify(123,builder.build())

    }
}