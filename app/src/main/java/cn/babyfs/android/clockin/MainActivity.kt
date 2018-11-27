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


        settingBtn.setOnClickListener {
//            var intent = Intent(this, ClockInService::class.java)
//            intent.action = ClockInService.ACTION_SEND_DINGDING
//            this?.startService(intent)
            setting()
        }



        initData()
    }


    fun initData(){
        var amTime = ClockService.getAMTme(this)
        var pmTime = ClockService.getPMTme(this)
        var phone = ClockService.getNumber(this)

        var amTimeArr = amTime?.split("-")
        amStartTime.text = amTimeArr?.get(0)
        amEndTime.text = amTimeArr?.get(1)

        var pmTimeArr = pmTime?.split("-")
        pmStartTime.text = pmTimeArr?.get(0)
        pmEndTime.text = pmTimeArr?.get(1)

        phoneNumber.text = phone

        clockInTime.text = ClockService.CeshiFmt.format(ClockService.getClockInTime(this))
    }

    private fun setting(){

        var amTime = "${amStartTime.text}-${amEndTime.text}"
        var pmTime = "${pmStartTime.text}-${pmEndTime.text}"
        var phone = "${phoneNumber.text}"
        ClockService.saveAMTme(this,amTime)
        ClockService.savePMTme(this,pmTime)
        ClockService.saveNumber(this,phone)
    }

}
