package cn.babyfs.android.clockin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, ClockService::class.java))


        clockIn.setOnClickListener {
            var intent = Intent(this, ClockInService::class.java)
            intent.action = ClockInService.ACTION_SEND_DINGDING
            this?.startService(intent)
        }
    }


}
