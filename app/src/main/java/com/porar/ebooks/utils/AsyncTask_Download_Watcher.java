package com.porar.ebooks.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;

import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.EbooksHashFile;
import com.porar.ebooks.stou.EbooksShelfHeaderFile;
import com.porar.ebooks.stou.Shared_Object;

public abstract class AsyncTask_Download_Watcher extends TimerTask {
	// Constructor
	private final Context context;
	private final String currentURLString;
	private final Model_EBook_Shelf_List ebooks;
	private final Model_Customer_Detail customer;

	// Download
	private URL currentURL;
	private URLConnection connection;
	private Integer fileSize = 0;
	private String filesave;
	private boolean threadDownload = true;
	private Integer readByte = 0;
	private float downloadPercent;

	public AsyncTask_Download_Watcher(Context context, Model_EBook_Shelf_List item, Model_Customer_Detail modelCustomer) {
		this.ebooks = item;
		this.currentURLString = AppMain.DOWNLOAD_WATCHER_URL + item.getFile();
		this.context = context;
		this.customer = modelCustomer;
	}

	@Override
	public void run() {
		try {
			Log.d(AppMain.getTag(), "download watcher : " + currentURLString);
			currentURL = new URL(currentURLString);
			connection = currentURL.openConnection();
			connection.setConnectTimeout(5000);

			fileSize = connection.getContentLength();

			EbooksHashFile saveHash = new EbooksHashFile();
			saveHash.setBID(this.ebooks.getBID());
			saveHash.setCID(Shared_Object.getCustomerDetail.getCID());
			saveHash.setExtension(this.ebooks.getExtension());
			saveHash.setFile(this.ebooks.getFile());
			saveHash.setHashBytes(fileSize);
			saveHash.setSaveDate(new Date());
			saveHash.setUsername(Shared_Object.getCustomerDetail.getEmail());
			Class_Manage.SaveEbooksObject(this.context, saveHash, "ebooks_" + ebooks.getBID() + ".hash");

			filesave = "ebooks_" + ebooks.getBID() + ebooks.getExtension();

			onStart(ebooks);

			InputStream in = connection.getInputStream();

			FileOutputStream out = new FileOutputStream(Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + filesave);
			byte[] buffer = new byte[512 * 1024]; // Experiment with this value
			int bytesRead;
			int countPrint = 0;
			while ((bytesRead = in.read(buffer)) != -1) {
				if (threadDownload == false) {
					break;
				} else {
					countPrint++;
					readByte += bytesRead;
					if (countPrint == 50) {
						countPrint = 0;
						downloadPercent = ((float) readByte / fileSize) * 100;
						onProgress(ebooks, Math.round(downloadPercent));
					}
					out.write(buffer, 0, bytesRead);// Write to file
				}
			}

			if (threadDownload) {
				String filehead = "ebooks_" + ebooks.getBID() + ".porar";
				if (Class_Manage.SaveEbooksObject(this.context, new EbooksShelfHeaderFile(this.ebooks, this.customer), filehead)) {
					threadDownload = false;
					onComplete(this.ebooks);
				} else {
					readByte = 0;
					threadDownload = false;
				}

			} else {
				File file = new File(this.context.getFilesDir() + "/" + filesave);
				file.delete();
				onError(ebooks);
			}
		} catch (MalformedURLException e) {
			onError(ebooks);
		} catch (IOException e) {
			onError(ebooks);
		} catch (NullPointerException e) {
			onError(ebooks);
		}
	}

	protected abstract void onStart(Model_EBook_Shelf_List model);

	protected abstract void onError(Model_EBook_Shelf_List model);

	protected abstract void onProgress(Model_EBook_Shelf_List model, int progressDownload);

	protected abstract void onComplete(Model_EBook_Shelf_List model);

}