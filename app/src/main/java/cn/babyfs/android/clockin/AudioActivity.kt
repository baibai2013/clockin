package cn.babyfs.android.clockin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_audio.*
import org.apache.commons.codec.binary.Base64

class AudioActivity : AppCompatActivity() {


    var recoder = AIRecorder()
    val path = PathUtils.getAppPath()  +"audiotemp.pcm"

    var callback = object :AIRecorder.Callback{
        override fun onStarted() {

        }

        override fun onData(data: ByteArray?, size: Int) {

        }

        override fun onStopped() {
           val result =  WebIseHandler.getScore("[word]apple",PathUtils.getAppPath()+"read_sentence1.pcm")
            Log.e("AudioActivity",result)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        recoderBtn.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val start = recoder.start(path,callback)
            } else if (event.action == MotionEvent.ACTION_UP) {
                recoder.stop()
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                         arrayOf(Manifest.permission.RECORD_AUDIO),
                        1)
            }else {

            }
        }
    }




}
