package cn.babyfs.android.clockin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("BootBroadcastReceiver","onReceive")
        context?.startService(Intent(context, ClockService::class.java))
    }

}