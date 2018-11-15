package cn.babyfs.android.clockin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class WebIseHandler {
	// 评测webapi接口地址
	 static final String WEBISE_URL = "https://api.xfyun.cn/v1/service/v1/ise";
	// 测试应用ID
	private static final String TEST_APPID = "5bebd17a";
	// 测试接口密钥
	private static final String TEST_API_KEY = "49c03e9e46f1b13688ad78551db63469";
	// 测试音频文件存放位置
	private static final String AUDIO_FILE_PATH = "resource//read_sentence1.pcm";

	/**
	 * 组装http请求头
	 * 
	 * @param aue
	 * @param resultLevel
	 * @param language
	 * @param category
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> constructHeader(String aue, String resultLevel, String language, String category) throws UnsupportedEncodingException {
		// 系统当前时间戳
		String X_CurTime = System.currentTimeMillis() / 1000L + "";
		// 业务参数
		String param = "{\"aue\":\"" + aue + "\",\"result_level\":\"" + resultLevel + "\",\"language\":\"" + language + "\",\"category\":\"" + category + "\"}";
		String X_Param = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
		// 接口密钥
		String apiKey = TEST_API_KEY;
		// 讯飞开放平台应用ID
		String X_Appid = TEST_APPID;
		// 生成令牌
		String X_CheckSum = new String(Hex.encodeHex(DigestUtils.md5(apiKey + X_CurTime + X_Param)));

		// 组装请求头
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("X-Param", X_Param);
		header.put("X-CurTime", X_CurTime);
		header.put("X-CheckSum", X_CheckSum);
		header.put("X-Appid", X_Appid);
		return header;
	}

	public static String getScore(String text,String audioPath) throws IOException {
		Map<String, String> header = constructHeader("raw", "entirety", "en_us", "read_word");
		// 读取音频文件，转二进制数组，然后Base64编码
		byte[] audioByteArray = FileUtil.read2ByteArray(audioPath);
		String audioBase64 = new String(Base64.encodeBase64(audioByteArray), "UTF-8");
		String bodyParam = "text=" + text + "&audio=" + audioBase64;
		String result = HttpUtil.doPost(WEBISE_URL, header, bodyParam);
		System.out.println(result);
		return result;
	}
}
