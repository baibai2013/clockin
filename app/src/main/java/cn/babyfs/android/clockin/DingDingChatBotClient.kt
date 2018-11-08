package cn.babyfs.android.clockin

import com.alibaba.fastjson.JSONObject
import com.dingtalk.chatbot.SendResult
import com.dingtalk.chatbot.message.Message
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DingDingChatBotClient {

    fun send(webhook: String, message: Message): SendResult {
        val url = URL(webhook)
        val http = url.openConnection() as HttpURLConnection
        http.connectTimeout = 5000
        http.doInput = true
        http.doOutput = true
        http.requestMethod = "POST"
        http.useCaches = false
        http.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        val out = http.outputStream
        out.write(message.toJsonString().toByteArray())
        var sendResult = SendResult()
        val response = http.responseCode
        if (response == HttpURLConnection.HTTP_OK) {
            val input = http.inputStream
            val result = dealResponseResult(input)

            val obj = JSONObject.parseObject(result)
            val errcode = obj.getInteger("errcode")

            sendResult.errorCode = errcode
            sendResult.errorMsg = obj.getString("errmsg")
            sendResult.setIsSuccess(errcode == 0)

        }
        return sendResult

    }

    fun dealResponseResult(inputStream: InputStream): String {
        var resultData: String? = null      //存储处理结果
        val byteArrayOutputStream = ByteArrayOutputStream()
        val data = ByteArray(1024)
        var len = 0
        try {
            while (inputStream.read(data).apply { len = this } != -1) {
                byteArrayOutputStream.write(data, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        resultData = String(byteArrayOutputStream.toByteArray())
        return resultData
    }
}