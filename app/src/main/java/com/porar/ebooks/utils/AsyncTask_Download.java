package com.porar.ebooks.utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
//import android.app.Notification;
//import android.app.NotificationManager;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;

public abstract class AsyncTask_Download extends AsyncTask<Model_EBook_Shelf_List, Integer, Integer> implements Runnable {
	// Constructor
	private LinkedList<Model_EBook_Shelf_List> urlString = new LinkedList<Model_EBook_Shelf_List>();
	private Thread t;
	private Context mContext;
	private final int NotifyID = R.drawable.ico_logo_notification;
	private final Handler handle = new Handler();

	// Notify
	// private NotificationManager mManager;
	// private Notification notification;
	private PendingIntent StartIntent = null;
	private String NotificationTitle;
	private Timer timerStart;
	private Timer timerEnd;
	private final Timer timerRun = new Timer();
	private String label = "";
	private AlertDialog alertDialog;
	// Flag
	private boolean preventStart = true;
	private boolean preventEnd = false;
	private boolean inDownload = false;
	private boolean running = false;

	private Model_Customer_Detail customerDetail;

	public AsyncTask_Download(Context context, Model_Customer_Detail customerDetail) {
		try {
			this.mContext = context;
			this.customerDetail = customerDetail;
			// this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// Intent intent = new Intent(this.mContext,context.getClass());
			int version_code = android.os.Build.VERSION.SDK_INT;
			if (version_code == Build.VERSION_CODES.GINGERBREAD || version_code == Build.VERSION_CODES.GINGERBREAD_MR1) {
				this.StartIntent = PendingIntent.getActivity(context, 0, null, PendingIntent.FLAG_CANCEL_CURRENT);
			}

			handle.post(new Runnable() {

				@Override
				public void run() {
					alertDialog = new AlertDialog.Builder(mContext).create();
					alertDialog.setTitle(AppMain.getTag());
					alertDialog.setCancelable(false);
					alertDialog.setMessage("Start  download _%");
					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							OnError(null);
							dialog.dismiss();
							if (mContext instanceof Activity) {
								((Activity) mContext).finish();
							}
						}
					});
					alertDialog.show();
				}
			});

		} catch (IllegalArgumentException e) {
			// Log.e(AppMain.getTag(),"AsyncTask_Download - AsyncTask_Download() - IllegalArgumentException");
		}
	}

	@Override
	protected Integer doInBackground(Model_EBook_Shelf_List... params) {
		for (int i = 0; i < params.length; i++) {
			if (!urlString.contains(params[i])) {
				urlString.add(params[i]);
				preventEnd = true;

				if (timerStart == null) {
					timerStart = new Timer();
					timerStart.schedule(new TimerTask() {
						@Override
						public void run() {
							// Log.e(AppMain.getTag(),"--- Wait Start ---");
							if (urlString.size() > 0 && preventStart) {
								preventStart = false;
								preventEnd = true;

								NotificationTitle = "Ebooks.in.th - Prepare Download Task";
								// notification = new Notification(R.drawable.ico_logo_notification, NotificationTitle, System.currentTimeMillis());
								// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, "Manage File in Queue", StartIntent);
								// notification.defaults = Notification.DEFAULT_SOUND;
								// mManager.notify(NotifyID, notification);
								handle.postDelayed(new Runnable() {
									@Override
									public void run() {
										runThread();
									}
								}, 2000);

								timerEnd = new Timer();
								timerEnd.schedule(new TimerTask() {
									@Override
									public void run() {
										// Log.e(AppMain.getTag(),"--- Wait End ---");
										if (urlString.size() == 0 && preventEnd) {
											preventStart = true;
											preventEnd = false;

											timerStart.cancel();
											timerEnd.cancel();
											urlString = new LinkedList<Model_EBook_Shelf_List>();
											System.gc();

											// Update Information
											NotificationTitle = "Ebooks.in.th - Download Manager";
											// notification.when = System.currentTimeMillis();
											// notification.defaults = Notification.FLAG_NO_CLEAR;
											if (urlString.size() == 0) {
												label = "In Process";
											} else {
												label = (urlString.size() - 1) + " Task in Queue";
											}
											// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, label, StartIntent);
											// mManager.notify(NotifyID, notification);
										}
									}
								}, new Date(), 1000);

							}
						}
					}, new Date(), 1000);
				}
			}
		}
		return null;
	}

	@Override
	public void run() {
		new AsyncTask_Download_Watcher(AsyncTask_Download.this.mContext, urlString.removeFirst(), this.customerDetail) {

			@Override
			protected void onStart(Model_EBook_Shelf_List model) {
				NotificationTitle = "[" + model.getName() + "] Downloading ...";
				// notification = new Notification(R.drawable.ico_logo_notification, NotificationTitle, System.currentTimeMillis());
				// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, "In Progress", StartIntent);
				// notification.defaults = Notification.FLAG_NO_CLEAR;
				// mManager.notify(model.getBID(), notification);

			}

			@Override
			protected void onError(Model_EBook_Shelf_List model) {
				// mManager.cancel(model.getBID());
				OnError(model);
			}

			@Override
			protected void onProgress(final Model_EBook_Shelf_List model, final int progressDownload) {
				NotificationTitle = "[" + model.getName() + "] Downloading ...";
				// notification.when = System.currentTimeMillis();
				// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, "In Progress " + progressDownload + " %", StartIntent);
				// notification.defaults = Notification.FLAG_NO_CLEAR;
				// mManager.notify(model.getBID(), notification);

				handle.post(new Runnable() {

					@Override
					public void run() {
						if (alertDialog != null) {
							if (alertDialog.isShowing()) {
								alertDialog.setMessage("In Progress " + "[" + model.getName() + "] Downloading ..." + progressDownload + " %");
							}
						}
					}
				});

			}

			@Override
			protected void onComplete(final Model_EBook_Shelf_List model) {
				if (urlString.size() >= 0) {
					NotificationTitle = "[" + model.getName() + "] Downloading ...";
					// notification.when = System.currentTimeMillis();
					// notification.defaults = Notification.FLAG_NO_CLEAR;
					// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, "In Progress - Complete", StartIntent);
					// mManager.notify(model.getBID(), notification);
					handle.postDelayed(new Runnable() {
						@Override
						public void run() {
							OnComplete(model);
							// mManager.cancel(model.getBID());

							if (alertDialog != null) {
								if (alertDialog.isShowing()) {
									alertDialog.setMessage("In Progress " + "[" + model.getName() + "] Downloading ... Complete");
									alertDialog.dismiss();
									if (mContext instanceof Activity) {
										((Activity) mContext).finish();
									}
								}
							}
						}
					}, 1000);
				}

				NotificationTitle = "[" + model.getName() + "] Complete";
				// notification = new Notification(R.drawable.ico_logo_notification, NotificationTitle, System.currentTimeMillis());
				// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, "", StartIntent);
				// notification.defaults = Notification.FLAG_NO_CLEAR;
				// mManager.notify(model.getBID(), notification);


				handle.postDelayed(new Runnable() {

					@Override
					public void run() {
						inDownload = false;

						if (urlString.size() == 0) {
							NotificationTitle = "Ebooks.in.th - End Download Task";
							// notification = new Notification(R.drawable.ico_logo_notification, NotificationTitle, System.currentTimeMillis());
							// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, "", StartIntent);
							// notification.defaults = Notification.DEFAULT_SOUND;
							// mManager.notify(NotifyID, notification);
							handle.postDelayed(new Runnable() {

								@Override
								public void run() {
									// mManager.cancel(NotifyID);
								}
							}, 3000);
						}

					}
				}, 2000);
			}
		}.run();
	}

	private void runThread() {
		if (!running) {
			running = true;
			timerRun.schedule(new TimerTask() {
				@Override
				public void run() {
					if (!inDownload) {
						if (urlString.size() > 0) {
							if (t == null || t.getState() == Thread.State.TERMINATED) {
								inDownload = true;

								NotificationTitle = "Ebooks.in.th - Download Manager";
								// notification.when = System.currentTimeMillis();
								// notification.defaults = Notification.FLAG_NO_CLEAR;
								if (urlString.size() == 0) {
									label = "In Process";
								} else {
									label = (urlString.size() - 1) + " Task in Queue";
								}
								// notification.setLatestEventInfo(AsyncTask_Download.this.mContext, NotificationTitle, label, StartIntent);
								// mManager.notify(NotifyID, notification);

								t = new Thread(AsyncTask_Download.this);
								t.start();
							}
						}
					}
				}
			}, new Date(), 100);
		}
	}

	public boolean enableClose() {
		if (urlString.size() > 0) {
			return false;
		}
		return true;
	}

	protected abstract void OnComplete(Model_EBook_Shelf_List model);

	protected abstract void OnError(Model_EBook_Shelf_List model);
}