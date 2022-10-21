package notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent:Intent) {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val r:Ringtone = RingtoneManager.getRingtone(context,notification)
        r.play()

    }

}