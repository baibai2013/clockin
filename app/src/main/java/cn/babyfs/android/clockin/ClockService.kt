package cn.babyfs.android.clockin

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Heart Beat service
 *
 * @author zhaozhe
 * Created on 2018/8/17.
 */
class ClockService : Service() {


    companion object {
        const val TAG = "ClockInService"
        const val HEART_BEAT_INTERVAL = 60L
        val CeshiFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEE", Locale.CHINA)
        val CeshiFmt2 = SimpleDateFormat("yyyy-MM-dd=HH:mm")


        val fineName = "Time"

        fun saveClockInTime(context: Context?, time: Long) {
            var editer = context?.getSharedPreferences(fineName, Context.MODE_PRIVATE)?.edit()
            editer = editer?.putLong("time", time)
            editer?.apply()
        }

        fun getClockInTime(context: Context?): Long? {
            var sp = context?.getSharedPreferences(fineName, Context.MODE_PRIVATE)
            return sp?.getLong("time", 0)
        }
    }


    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        if (!serviceRunning("cn.babyfs.android.clockin.ClockService")) {
            improvePriority()
            doWork()
        }

        return START_STICKY
    }

    var notification: Notification? = null
    //提高服务优先级
    fun improvePriority() {

        if (notification != null) return

        var contentIntent = PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), 0)

        var smallIcon = R.mipmap.ic_launcher

        var contentText = "打卡精灵"

        var builder = NotificationCompat.Builder(this, "channel_clock")
        builder.setContentText(contentText)

        notification = builder
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setColor(Color.parseColor("#28cfd4"))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build()
        notification?.contentIntent = contentIntent
        startForeground(1, notification)
    }


    private fun doWork() {
        Log.d(TAG, "doWork")
        var timer = Timer()
        var timerTask = object : TimerTask() {
            override fun run() {
                dida()
            }
        }
        timer.schedule(timerTask, 0, 20 * 1000)
    }

    private fun dida() {
        val calender = Calendar.getInstance()
        var time = CeshiFmt.format(calender.time)
        Log.d(TAG, "当前时间：$time--星期${calender.get(Calendar.DAY_OF_WEEK) - 1}")
        if (calender.get(Calendar.DAY_OF_WEEK) in arrayListOf(2, 3, 4, 5, 6)) {
            var time = getClockInTime(this)
            if (time == 0L) {
                time = calender.timeInMillis + 1000 * 10
                saveClockInTime(this, time)
            }

            var clockInTimeFormated = CeshiFmt2.format(time?.let { Date(it) })
            var currentTimeFormated = CeshiFmt2.format(calender.time)

            Log.d(TAG, "打卡时间：$clockInTimeFormated")
            Log.d(TAG, "isScreenOn----${isScreenOn()}")
            Log.d(TAG, "isScreentLook----${isScreentLook()}")
            if (currentTimeFormated == clockInTimeFormated) {

                Log.d(TAG, "clockin----")
                time = randonNextTime()
                saveClockInTime(this, time)

                var timer = Timer()


                if (!isScreenOn()) {//屏幕是不亮，点亮
                    var timerTask6 = object : TimerTask() {
                        override fun run() {
                            wekeUp()
                            cancel()
                        }
                    }
                    timer.schedule(timerTask6, 0, 1000)


                }

                if (isScreentLook()) { //屏幕锁住，解锁
                    var timerTask5 = object : TimerTask() {
                        override fun run() {
                            unlock()
                            cancel()
                        }
                    }
                    timer.schedule(timerTask5, 200, 1000)
                }

                var timerTask = object : TimerTask() {
                    override fun run() {
                        open()
                        cancel()
                    }
                }
                timer.schedule(timerTask, 1000, 1000)

                var timerTask1 = object : TimerTask() {
                    override fun run() {
                        clockIn()
                        cancel()
                    }
                }

                timer.schedule(timerTask1, 5 * 1000, 10 * 1000)

                var timerTask2 = object : TimerTask() {
                    override fun run() {
                        close()
                        cancel()
                    }
                }
                timer.schedule(timerTask2, 7 * 1000, 7 * 1000)


                var timerTask3 = object : TimerTask() {
                    override fun run() {
                        close()
                        cancel()
                    }
                }
                timer.schedule(timerTask3, 8 * 1000, 8 * 1000)


                var timerTask4 = object : TimerTask() {
                    override fun run() {
                        close()
                        cancel()
                    }
                }

                timer.schedule(timerTask4, 9 * 1000, 9 * 1000)

                var timerTask7 = object : TimerTask() {
                    override fun run() {
                        senddingding()
                        cancel()
                    }
                }

                timer.schedule(timerTask7, 10 * 1000, 10 * 1000)
            }

        }

    }

    fun open() {
        var intent = Intent(this, ClockInService::class.java)
        intent.action = ClockInService.ACTION_OPEN_APP
        this?.startService(intent)
    }

    fun clockIn() {
        var intent = Intent(this, ClockInService::class.java)
        intent.action = ClockInService.ACTION_CLOCK_IN
        this?.startService(intent)
    }

    fun wekeUp() {
        var intent = Intent(this, ClockInService::class.java)
        intent.action = ClockInService.ACTION_WAKE_UP
        this?.startService(intent)
    }

    fun unlock() {
        var intent = Intent(this, ClockInService::class.java)
        intent.action = ClockInService.ACTION_UN_LOCK
        this?.startService(intent)
    }

    fun close() {
        var intent = Intent(this, ClockInService::class.java)
        intent.action = ClockInService.ACTION_CLOSE_APP
        this?.startService(intent)
    }

    fun senddingding() {
        var intent = Intent(this, ClockInService::class.java)
        intent.action = ClockInService.ACTION_SEND_DINGDING
        this?.startService(intent)
    }


    fun Context.serviceRunning(name: String): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(50)
        runningServices.forEach {
            if (name == it.service.className) {
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    /**
     * 下一次执行的时间
     * 上午随机下午的时间
     * 下午随机明天上午时间
     */
    fun randonNextTime(): Long {
        val calender = Calendar.getInstance()
        var startAM = Calendar.getInstance()
        startAM.set(Calendar.HOUR_OF_DAY, 9)
        startAM.set(Calendar.MINUTE, 0)

        var endAM = Calendar.getInstance()
        endAM.set(Calendar.HOUR_OF_DAY, 9)
        endAM.set(Calendar.MINUTE, 40)

        var startPM = Calendar.getInstance()
        startPM.set(Calendar.HOUR_OF_DAY, 19)
        startPM.set(Calendar.MINUTE, 30)

        var endPM = Calendar.getInstance()
        endPM.set(Calendar.HOUR_OF_DAY, 21)
        endPM.set(Calendar.MINUTE, 30)

        var time24 = 1000 * 60 * 60 * 24

        var hour = calender.get(Calendar.HOUR_OF_DAY)
        if (hour >= 12) {
            //下午
            if (calender.timeInMillis < startPM.timeInMillis) {
                return getRandomNumberRange(startPM.timeInMillis, endPM.timeInMillis)
            }
            //周5 跳过3天
            if (calender.get(Calendar.DAY_OF_WEEK) == 6) {
                time24 *= 3
            }
            return getRandomNumberRange(startAM.timeInMillis + time24, endAM.timeInMillis + time24)
        } else {
            //上午
            if (calender.timeInMillis < startAM.timeInMillis) {
                return getRandomNumberRange(startAM.timeInMillis, endAM.timeInMillis)
            }

            return getRandomNumberRange(startPM.timeInMillis, endPM.timeInMillis)
        }
        return 0
    }


    /**
     * 随机范围
     * @return
     */
    fun getRandomNumberRange(begin: Long, end: Long): Long {
        var rtn = begin + (Math.random() * (end - begin)).toLong()
        if (rtn == begin || rtn == end) {
            getRandomNumberRange(begin, end)
        }
        return rtn
    }

    fun isScreentLook(): Boolean {
        var mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        if (mKeyguardManager.isKeyguardLocked) {
            // keyguard on
            return true
        }
        return false
    }

    fun isScreenOn(): Boolean {
        var powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isInteractive
    }

}