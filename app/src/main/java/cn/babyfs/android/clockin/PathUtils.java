package cn.babyfs.android.clockin;

import android.os.Environment;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class PathUtils {

	public static String getSDcardDir() {
		return Environment.getExternalStorageDirectory().getPath() + "/";
	}

	public static String checkAndMkdirs(String dir) {
		File file = new File(dir);
		if (file.exists() == false) {
			file.mkdirs();
		}
		return dir;
	}

	public static String getAppPath(){
		String dir = getSDcardDir() + "TestAudio/";
		return checkAndMkdirs(dir);
	}

}
