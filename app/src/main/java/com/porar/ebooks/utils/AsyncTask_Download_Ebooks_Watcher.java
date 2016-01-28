package com.porar.ebooks.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

import android.util.Log;

import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;

public abstract class AsyncTask_Download_Ebooks_Watcher extends TimerTask {
	// Constructor
	private String currentURLString;
	private final Model_Ebooks_Page ebooks;

	// Download
	private URL currentURL;
	private URLConnection connection;
	private Integer fileSize = 0;
	private String filesave;
	private final boolean threadDownload = true;
	private Integer readByte = 0;
	private float downloadPercent;

	private String filename;

	public enum ErrorException {
		MalformedURLException, IOException, NullPointerException, ConnectException, OutOfMemoryError
	}

	public AsyncTask_Download_Ebooks_Watcher(Model_Ebooks_Page item) {
		this.ebooks = item;
		this.currentURLString = AppMain.DOWNLOAD_EBOOK_WATCHER_URL;
		currentURLString += "?cid=" + item.getCustomerId() + "&bid=" + item.getBID() + "&p=" + item.getCurrentPagesNumber();
		Log.e("AsyncTask_Download_Ebooks_Watcher", currentURLString);
	}

	@Override
	public void run() {
		FileOutputStream out = null;
		try {
			currentURL = new URL(currentURLString);
			connection = currentURL.openConnection();
			connection.setConnectTimeout(5000);

			fileSize = connection.getContentLength();

			filesave = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + ebooks.getBID();
			filename = "ebooks_" + ebooks.getBID() + "_page_" + ebooks.getCurrentPagesNumber() + ".ebooks";

			onStart(ebooks.getCurrentPagesNumber());

			InputStream in = connection.getInputStream();

			//out = new BufferedOutputStream(new FileOutputStream(filesave + "/" + filename), new byte[1024].length);
			out = new FileOutputStream(filesave + "/" + filename);
			byte[] buffer = new byte[512 * 1024]; // Experiment with this value
			int bytesRead;
			int countPrint = 0;
			while ((bytesRead = in.read(buffer)) != -1) {

				countPrint++;
				readByte += bytesRead;
				if (countPrint == 50) {
					countPrint = 0;
					downloadPercent = ((float) readByte / fileSize) * 100;
					onProgress(ebooks.getCurrentPagesNumber(), Math.round(downloadPercent));
				}
				out.write(buffer, 0, bytesRead);// Write to file
			}

			// FileOutputStream out = new FileOutputStream(filesave + "/" + filename);

			// Log.i(AppMain.getTag(), "Save : " + filename);
			onComplete(this.ebooks.getBID(), ebooks.getCurrentPagesNumber());
		} catch (MalformedURLException e) {
			// Log.e(AppMain.getTag(), "AsyncTask_Download_Ebooks_Watcher  - MalformedURLException");
			onError(ebooks.getBID(), ErrorException.MalformedURLException, ebooks);
		} catch (ConnectException e) {
			e.printStackTrace();
			// Log.e(AppMain.getTag(), "AsyncTask_Download_Ebooks_Watcher  - ConnectException");
			onError(ebooks.getBID(), ErrorException.ConnectException, ebooks);
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e(AppMain.getTag(), "AsyncTask_Download_Ebooks_Watcher  - IOException[1]");
			onError(ebooks.getBID(), ErrorException.IOException, ebooks);
		} catch (NullPointerException e) {
			e.printStackTrace();
			// Log.e(AppMain.getTag(), "AsyncTask_Download_Ebooks_Watcher  - NullPointerException");
			onError(ebooks.getBID(), ErrorException.NullPointerException, ebooks);
		} finally {
			if (out != null) {
				out = null;
			}
		}
	}

	protected abstract void onStart(int bid);

	protected abstract void onError(int bid, ErrorException e, Model_Ebooks_Page model);

	protected abstract void onProgress(int bid, int progressDownload);

	protected abstract void onComplete(int bid, int pageNumber);

}
