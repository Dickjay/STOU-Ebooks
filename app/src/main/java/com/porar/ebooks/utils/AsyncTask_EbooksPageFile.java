package com.porar.ebooks.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.utils.AsyncTask_Download_Ebooks_Watcher.ErrorException;

public abstract class AsyncTask_EbooksPageFile extends AsyncTask<Model_Ebooks_Page, Integer, Bitmap> {

	private AsyncTask_Download_Ebooks download;
	private String fileName = "";
	private File currentFile;
	private Model_Ebooks_Page currentModel;

	@Override
	protected Bitmap doInBackground(Model_Ebooks_Page... each) {
		try {
			for (final Model_Ebooks_Page item : each) {
				currentModel = item;
				fileName = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + item.getBID() + "/ebooks_" + item.getBID() + "_page_" + item.getCurrentPagesNumber() + ".ebooks";
				currentFile = new File(fileName);
				if (currentFile.exists()) {
					if (currentFile.length() > 0) {
						OnComplete(item);
					}else{
						if (download == null) {
							download = new AsyncTask_Download_Ebooks() {
								@Override
								protected void OnComplete(int bookid, int pageNumber) {
									AsyncTask_EbooksPageFile.this.OnComplete(item);
								}

								@Override
								protected void OnError(int bookid, ErrorException e, Model_Ebooks_Page model) {
									AsyncTask_EbooksPageFile.this.OnError(e, model);
								}
							};
						}
						download.doInBackground(each);
					}
					
				} else {
					if (download == null) {
						download = new AsyncTask_Download_Ebooks() {
							@Override
							protected void OnComplete(int bookid, int pageNumber) {
								AsyncTask_EbooksPageFile.this.OnComplete(item);
							}

							@Override
							protected void OnError(int bookid, ErrorException e, Model_Ebooks_Page model) {
								AsyncTask_EbooksPageFile.this.OnError(e, model);
							}
						};
					}
					download.doInBackground(each);
				}
			}
		} catch (OutOfMemoryError e) {
			//Log.e(AppMain.getTag(), "AsyncTask_EbooksPageFile : OutOfMemoryError[" + e.getMessage() + "]");
			e.printStackTrace();
			OnError(ErrorException.OutOfMemoryError, currentModel);
		}
		return null;
	}

	protected abstract void OnComplete(Model_Ebooks_Page model);

	protected abstract void OnError(ErrorException e, Model_Ebooks_Page model);
}