package com.porar.ebooks.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class TaskImageInfo_forCache extends AsyncTask<Map<File, Bitmap>, Void, Void> {

	public static void writeFile(File f, Bitmap bmp) {
		try {
//			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f), bmp.getByteCount());
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f), getSizeInBytes(bmp));
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void writeDateFile(String textfile, File file_read) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file_read), textfile.getBytes().length);
			bufferedWriter.write(textfile);
			bufferedWriter.flush();
			bufferedWriter.close();

			Log.v("writeDate success", "" + file_read);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected Void doInBackground(Map<File, Bitmap>... params) {

		for (Map<File, Bitmap> map : params) {
			for (Entry<File, Bitmap> each : map.entrySet()) {
				writeFile(each.getKey(), each.getValue());

				Log.v("writeFile success", "" + each.getKey());
				map.remove(each.getKey());

			}
		}
		return null;
	}
	
	private static  int getSizeInBytes(Bitmap bitmap) {
	    return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
