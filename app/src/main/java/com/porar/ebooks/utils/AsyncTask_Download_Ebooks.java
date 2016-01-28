package com.porar.ebooks.utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;

import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.utils.AsyncTask_Download_Ebooks_Watcher.ErrorException;

public abstract class AsyncTask_Download_Ebooks extends AsyncTask<Model_Ebooks_Page, Integer, Integer> implements Runnable {
	//Constructor
	private LinkedList<Model_Ebooks_Page> ebooksPage = new LinkedList<Model_Ebooks_Page>();
	private Thread t;
	
	//Notify
	private Timer timerStart;
	private Timer timerEnd;
	private Timer timerRun = new Timer();
	
	//Flag
	private boolean preventStart = true;
	private boolean preventEnd = false;
	private boolean inDownload = false;
	private boolean running = false;

	@Override
	protected Integer doInBackground(Model_Ebooks_Page... params) {
		for(int i = 0;i<params.length;i++){
			if(!ebooksPage.contains(params[i])){
				ebooksPage.add(params[i]);
				preventEnd = true;
				
				if(timerStart==null){
					timerStart = new Timer();
					timerStart.schedule(new TimerTask(){
						@Override
						public void run() {
							//Log.e(AppMain.getTag(),"--- Wait Start ---");
							if(ebooksPage.size()>0 && preventStart){
								preventStart = false;
								preventEnd = true;

								runThread();
								
								timerEnd = new Timer();
								timerEnd.schedule(new TimerTask(){
									@Override
									public void run() {
										//Log.e(AppMain.getTag(),"--- Wait End ---");
										if(ebooksPage.size()==0 && preventEnd){
											preventStart = true;
											preventEnd = false;
											
											timerStart.cancel();
											timerEnd.cancel();
											ebooksPage = new LinkedList<Model_Ebooks_Page>();
											System.gc();
										}
									}
								}, new Date(), 100);
								
							}
						}
					}, new Date(), 100);
				}
			}
		}
		return null;
	}
	
	
	public void run(){
		new AsyncTask_Download_Ebooks_Watcher(ebooksPage.removeFirst()){
			@Override
			protected void onStart(int bookid) {
				// TODO Auto-generated method stub
			}

			@Override
			protected void onProgress(int bookid, int progressDownload) {
				// TODO Auto-generated method stub
			}

			@Override
			protected void onComplete(final int bookid, final int pageNumber) {
				if(ebooksPage.size()>=0){
					OnComplete(bookid,pageNumber);
				}
				inDownload = false;
				if(ebooksPage.size()==0){
					
				}
			}

			@Override
			protected void onError(int bid, ErrorException e,Model_Ebooks_Page model) {
				AsyncTask_Download_Ebooks.this.OnError(bid,e,model);
			}
		}.run();
	}
	
	private void runThread(){
		if(!running){
			running = true;
			timerRun.schedule(new TimerTask(){
				@Override
				public void run() {
					if(!inDownload){
						if(ebooksPage.size()>0){
							if(t==null ||t.getState()==Thread.State.TERMINATED){
								inDownload = true;
								t = new Thread(AsyncTask_Download_Ebooks.this);
								t.start();
							}
						}
					}
				}
			},new Date(),100);
		}
	}
	
	public boolean enableClose(){
		if(ebooksPage.size()>0){
			return false;
		}
		return true;
	}
	
	protected abstract void OnComplete(int bookid,int pageNumber);
	protected abstract void OnError(int bookid,ErrorException e,Model_Ebooks_Page model);
}