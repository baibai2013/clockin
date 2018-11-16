package cn.babyfs.android.clockin

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP
import android.os.PowerManager.SCREEN_DIM_WAKE_LOCK
import android.util.Log
import cn.babyfs.android.clockin.Global.PACKAGE_NAME_BS
import com.dingtalk.chatbot.DingtalkChatbotClient
import com.dingtalk.chatbot.message.MarkdownMessage
import com.dingtalk.chatbot.message.TextMessage
import java.io.*
import java.util.*




class ClockInService : IntentService("ClockInService") {

    companion object {
        const val TAG = "ClockInService"
        const val ACTION_CLOCK_IN = "action.clock_in"
        const val ACTION_SCREEN_SHOT = "action.screen_sort"
        const val ACTION_OPEN_APP = "action.open_app"
        const val ACTION_CLOSE_APP = "action.close_app"
        const val ACTION_WAKE_UP = "action.wake_up"
        const val ACTION_UN_LOCK = "action.un_lock"
        const val ACTION_SEND_DINGDING = "action.send_dingding"

        @SuppressLint("InvalidWakeLockTag")
        fun wakeUpAndUnlock(context: Context) {
            // 获取电源管理器对象
            var pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            var screenOn = pm.isScreenOn()
            if (!screenOn) {
                // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
                var wl: PowerManager.WakeLock = pm.newWakeLock(ACQUIRE_CAUSES_WAKEUP or SCREEN_DIM_WAKE_LOCK, "bright")
                wl.acquire(10000)
                wl.release() // 释放
            }
            // 屏幕解锁
            var keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            var keyguardLock = keyguardManager.newKeyguardLock("unLock")
            keyguardLock.reenableKeyguard()
            keyguardLock.disableKeyguard() // 解锁
        }

    }


    override fun onCreate() {
        super.onCreate()

    }


    override fun onHandleIntent(intent: Intent?) {

        when (intent?.action) {
            ACTION_CLOCK_IN -> {

//                val x = intent.getIntExtra("x",0)
//                val y = intent.getIntExtra("y",0)
                val x = 351
                val y = 1016
//                val order = arrayListOf("input", "tap", "" + x, "" + y)
//                ProcessBuilder(order).start()

                var process = Runtime.getRuntime().exec("su")
                var os = DataOutputStream(process.getOutputStream())
                var cmd = "/system/bin/input tap $x $y\n"
                os.writeBytes(cmd)
                os.writeBytes("exit\n")
                os.flush()
                os.close()
                process.waitFor()

                Log.d(TAG, ACTION_CLOCK_IN)
            }
            ACTION_OPEN_APP -> {
                wakeUpAndUnlock(this)
                var intent = Intent()
                intent.setClassName(PACKAGE_NAME_BS, "com.beisen.titamobile.ui.sign.italent.ItalentSignActivity")
                startActivity(intent)
                Log.d(TAG, ACTION_OPEN_APP)
            }
            ACTION_CLOSE_APP->{
                var process = Runtime.getRuntime().exec("su")
                var os = DataOutputStream(process.getOutputStream())
                var cmd = "/system/bin/input keyevent 4\n"
                os.writeBytes(cmd)
                os.writeBytes("exit\n")
                os.flush()
                os.close()
                process.waitFor()

                Log.d(TAG,"$ACTION_CLOSE_APP")
            }
            ACTION_WAKE_UP->{
                var process = Runtime.getRuntime().exec("su")
                var os = DataOutputStream(process.getOutputStream())
                var cmd = "/system/bin/input keyevent 26\n"
                os.writeBytes(cmd)
                os.writeBytes("exit\n")
                os.flush()
                os.close()
                process.waitFor()

                Log.d(TAG,"$ACTION_WAKE_UP")
            }

            ACTION_UN_LOCK->{
                var process = Runtime.getRuntime().exec("su")
                var os = DataOutputStream(process.getOutputStream())
                var cmd = "/system/bin/input swipe 351 1016 351 500\n"
                os.writeBytes(cmd)
                os.writeBytes("exit\n")
                os.flush()
                os.close()
                process.waitFor()

                Log.d(TAG,"$ACTION_UN_LOCK")
            }

            ACTION_SEND_DINGDING->{
               sendDingDing()
                Log.d(TAG,"${ACTION_SEND_DINGDING} ")
            }

        }


    }

    private fun sendDingDing() {
        val hookweb = "https://oapi.dingtalk.com/robot/send?access_token=a96ea0f7a8428d9ac8f9de4db5e8839a65d6bcdedcb0c4d544cbc542b54803ee"
        val currentTime = ClockService.CeshiFmt.format(Date())
        val nextClockInTime =  ClockService.CeshiFmt.format(ClockService.getClockInTime(this)?.let { Date(it) })

        val imgarr = arrayListOf("http://t2.hddhhn.com/uploads/tu/201601/210/wi3xipqk20z.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/210/e2h0nh4j4x0.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/210/yvbtnjctm1x.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/210/31fn3g2rmya.jpg",
                "http://t2.hddhhn.com/uploads/tu/201612/562/4hp4d1fcocu.jpg",
                "http://t2.hddhhn.com/uploads/tu/201612/562/emlvtcyx3n4.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/4m1pxuxmaxy.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/ngrnhmhixcn.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/4nabbtc2hrx.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/qsmpmfel0tw.jpg",
                "http://t2.hddhhn.com/uploads/tu/201602/20/gvgmhdz1si3.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/tcz2dmex2sx.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/kn0nbpvdvgv.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/ulx0ugwm5s0.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/encfc1hylhv.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/hoyon1z3lgp.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/u0x0kjludzo.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/xb0h3jnbwf5.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/lwyj3edv4pv.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/jdy2ruhcfm4.jpg",
                "http://t2.hddhhn.com/uploads/tu/201601/320/ylfwzzewptb.jpg")

        val image = imgarr[Random().nextInt(imgarr.size)]

        val phone = ClockService.getNumber(this)
        var content = "@$phone: \n" +
                "#### 打卡时间: \n" +
                "$currentTime\n" +
                "#### 下次打卡时间: \n" +
                "$nextClockInTime\n" +
                "![screenshot]($image)\n"
        var msg = MarkdownMessage()
        msg.title = "今日打卡"
        msg.add(content)
        var client = DingDingChatBotClient()
        var result = client.send(hookweb,msg)
        Log.d(TAG,"${ACTION_SEND_DINGDING} $result")
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}