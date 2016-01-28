package com.porar.ebook.control;

import plist.xml.PList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;

import com.porar.ebooks.model.Model_Quote;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

public class Quote_API {

	private final Activity context;
	private AsyncTask_FetchAPI asyncTask_FetchAPI = null;
	private final Handler handler = new Handler();
	private ProgressDialog progressDialog;
	Model_Quote model_Quote;
	Quote quote;
	private PList plist;

	public interface Quote {
		void onQuote(PList plList, ProgressDialog dialog);
	}

	public void setQuoteListener(Quote quote) {
		this.quote = quote;
	}

	public Quote_API(Activity context) {
		this.context = context;

	}

	public void RefreshQuote_API() {
		loadQuote_API();
	}

	public void loadQuote_API() {

		asyncTask_FetchAPI = new AsyncTask_FetchAPI();
		asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

			@Override
			public void onTimeOut(String apiURL, int currentIndex) {

			}

			@Override
			public void onFetchStart(String apiURL, int currentIndex) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (progressDialog == null) {
							progressDialog = ProgressDialog.show(context, "", "Loading..", true, true);
						}
					}
				});
			}

			@Override
			public void onFetchError(String apiURL, int currentIndex, Exception e) {

			}

			@Override
			public void onFetchComplete(final String apiURL, int currentIndex, final PList result) {

				plist = result;
			}

			@Override
			public void onAllTaskDone() {
				if (quote != null) {
					quote.onQuote(plist, progressDialog);
				}
			}
		});

		asyncTask_FetchAPI.execute("http://api.ebooks.in.th/ebooks_quote.php");

	}

	public boolean CancelTask() {
		if (asyncTask_FetchAPI != null) {
			asyncTask_FetchAPI.cancel(true);
			return true;
		}
		return false;

	}

}
