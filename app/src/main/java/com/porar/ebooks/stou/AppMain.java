package com.porar.ebooks.stou;

import java.util.Hashtable;

import plist.xml.PList;
import android.app.Application;
import android.net.Uri;

public class AppMain extends Application {

	public static boolean isphone = false;
	public static String MD5 = "0";
	private static String TAG = "STOU";
	public static int CID = 0;

	public static enum ScreenSize {
		Large, XLarge, Normal, Small
	};

	public static boolean isGotoshelf = true;
	public static ScreenSize screenSize = ScreenSize.Normal;
	public static Uri uri_imageCrop = null;
	public static PList pList_default_ebookshelf = null;
	public static PList pList_default_ebook = null;
	public static PList pList_default_ebook_copy = null;
	

	public static PList pList_default_ebook_master = null;
	public static PList pList_default_ebook_master0 = null;
	public static PList pList_default_ebook_master1 = null;

	public static PList pList_default_publisher = null;
	public static PList pList_default_publisher_ebook = null;
	public static PList pList_default_top = null;
	public static PList pList_default_top50 = null;
	public static PList pList_categories = null;
	public static PList pList_Subcategories = null;
	public static PList pList_Subcategories_ebook = null;
	public static PList pList_news = null;
	public static PList pList_news2 = null;
	// stou
	public static PList pList_categories2 = null;
	public static PList pList_categories3 = null;
	public static PList pList_news2_master = null;
	
	public static boolean isShelf = false;
	// end

	private static Hashtable<String, String> API;
	private static String PARAMS[] = { "keyword", "page", "count", "type",
			"catid", "page", "short", "pcid", "cid", "did", "email",
			"password", "chcode", "bid", "version", "file", "pointer",
			"comment", "name", "rating", "add", "scatid", "includeafter",
			"ucc", "retina", "trueonly", "package", "provider", "size", "srid",
			"period", "student_id", "student_name", "student_surname",
			"student_birthdate", "student_email", "student_password",
			"master_degree", "cate","mcat","bcode","mode" };

	// stou
	public static final String DOWNLOAD_COVER_URL = "http://203.150.225.223/stoubook/covers/";
	public static final String NEW_LIST_URL = "http://203.150.225.223/stoubookapi/images/api/";
	public static final String NEW_LIST_URL2 = "http://203.150.225.223/stoubookapi/images/api/";
	public static final String NEW_LIST_URL2_MASTERs = "http://203.150.225.223/stoubookapi/images/api/";
	public static final String REGISTER_URL = "http://203.150.225.223/stoubookapi/api/register.ashx?";
	public static final String BUY_BOOK_URL = "http://203.150.225.223/stoubookapi/payment-android.aspx";
	// public static final String ADD_TO_SHELF_URL =
	// "http://203.150.225.223/stoubookapi/api/add-api-cart.ashx";
	public static final String ADD_TO_SHELF_URL = "http://203.150.225.223/stoubookapi/api/addbookshelf.ashx";

	public static final String TRASH_SHELF_URL = "http://203.150.225.223/stoubookapi/android/_/android-disable-list.php?cid=";
	// public static final String TRASH_SHELF_URL =
	// "http://ebooks.in.th/android/_/android-disable-list.php?cid=";
	// public static final String TRASH_SHELF_URL =
	// "http://203.150.225.223/stoubookapi/api/android-disable-list.php?cid=";

	public static final String TEST_LOGIN_URL = "http://203.150.225.223/stoubookapi/api/cusinfo.php";
	public static final String LOGIN_URL = "http://203.150.225.223/stoubookapi/api/checklogin.ashx?email=";

	public static final String LOGIN_FACEBOOK_URL = "http://203.150.225.223/stoubookapi/api/facebook-login.ashx?fbid=";
	public static final String GET_PASSWORD_FACEBOOK_URL = "http://203.150.225.223/stoubookapi/api/facebook-password.ashx?fbid=";
	public static final String POST_COMMENT_URL = "http://203.150.225.223/stoubookapi/api/post-comment.ashx?";
	public static final String DOWNLOAD_WATCHER_URL = "http://stoubook.com/admin/download.ashx?file=";
	public static final String DOWNLOAD_EBOOK_WATCHER_URL = "http://203.150.225.223/stoubook/api/android-get-page.php";
	public static final String DELETE_BOOK_SHELF_URL = "http://203.150.225.223/stoubookapi/api/deletebookshelf.ashx?";
	public static final String UPDATE_BOOK_SHELF_POINTER_URL = "http://203.150.225.223/stoubookapi/api/update-bookshelf-pointer.ashx?";
	public static final String FAVORITE_BOOK_SHELF_URL = "http://203.150.225.223/stoubookapi/api/addfavorite.ashx?";
	public static final String ENABLE_BOOK_SHELF_URL = "http://203.150.225.223/stoubookapi/api/enable-bookshelf.ashx?";
	public static final String COVER_URL = "http://203.150.225.223/stoubook/covers/";
	public static final String NEW_API = "http://203.150.225.223/stoubookapi/api/news-api2.aspx?nid=";
	// public static final String COVER_URL = "http://www.ebooks.in.th/covers/";
	public static final String DEFAULT_USER_PIC_URL = "http://www.ebooks.in.th/images/user48px.png";

	public static final String LOGOUT_URL_STOU = "http://203.150.225.223/stoubookapi/api/logout.php?cid=";
	public static final String LOGIN_URL_STOU = "http://203.150.225.223/stoubookapi/api/checklogin.php?email=";
	public static final String CHECK_STUDENT_AVAIABLE = "http://203.150.225.223/stoubookapi/api/check-register.php?apikey=f4666c85ab34b5397519ef4b4e471b43";

	public static void setScreen(ScreenSize size) {

		screenSize = size;
	}

	// TEST LOGIN
	// ebook.bu@bumail.net pass ebook@2014 or ebook@stoubook.com pass ebook@2014
	// jirayu.sin@bu.ac.th pass jirayu@2014 or sarawut.ras@bu.ac.th pass
	// sarawut@2014

	private static void setAPIRefKey() {
		AppMain.API = new Hashtable<String, String>();

		// image comment default :
		// http://203.150.225.223/stoubookapi/api/images/no_avt.jpg
		AppMain.API
		.put("getMasterBookDetail",
				"http://203.150.225.223/stoubookapi/api/getMasterBookDetail.php?mcat={{{mcat}}}&bcode={{{bcode}}}");
		AppMain.API
				.put("getMasterBook",
						"http://203.150.225.223/stoubookapi/api/getMasterBook.php?cate={{{cate}}}&type=android");
		AppMain.API
				.put("news-list2",
						"http://203.150.225.223/stoubookapi/api/news-list2.ashx?retina={{{retina}}}&master_degree={{{master_degree}}}");
		AppMain.API
				.put("news-api2",
						"http://203.150.225.223/stoubookapi/api/news-api2.aspx?nid{{{nid}}}&type=android");
		// digi-new
		AppMain.API
				.put("digi-news-list",
						"http://203.150.225.223/stoubookapi/api/digi-news-list.ashx?retina={{{retina}}}");

		AppMain.API
				.put("categories",
						"http://203.150.225.223/stoubookapi/api/categories.ashx?catid={{{catid}}}");
		AppMain.API
				.put("back-issues",
						"http://203.150.225.223/stoubookapi/api/back-issues.ashx?bid={{{bid}}}&count={{{count}}}&includeafter={{{includeafter}}}&retina={{{retina}}}");
		AppMain.API
				.put("search-publisher",
						"http://203.150.225.223/stoubookapi/api/search-publisher.ashx?keyword={{{keyword}}}&page={{{page}}}&count={{{count}}}");
		AppMain.API
				.put("search",
						"http://203.150.225.223/stoubookapi/api/search.ashx?keyword={{{keyword}}}&page={{{page}}}&count={{{count}}}");
		AppMain.API
				.put("get_ebook_list",
						"http://203.150.225.223/stoubookapi/api/get_ebook_list.php?type={{{type}}}&page={{{page}}}&cid={{{cid}}}&bcode={{{bcode}}}&mode={{{mode}}}&scatid={{{scatid}}}");
		AppMain.API
				.put("ebooks-list",
						"http://203.150.225.223/stoubookapi/api/ebooks-list.ashx?type={{{type}}}&count={{{count}}}&retina={{{retina}}}&catid={{{catid}}}&cid={{{cid}}}");
		AppMain.API
				.put("categories-list",
						"http://203.150.225.223/stoubookapi/api/categories-list.ashx?scatid={{{scatid}}}&page={{{page}}}&cid={{{cid}}}");

		AppMain.API
				.put("publishers-list",
						"http://203.150.225.223/stoubookapi/api/publishers-list.ashx?type={{{type}}}&short={{{short}}}&page={{{page}}}&count={{{count}}}");

		AppMain.API
				.put("publisher-detail",
						"http://203.150.225.223/stoubookapi/api/publisher-detail.ashx?pcid={{{pcid}}}&cid={{{cid}}}&did={{{did}}}&provider={{{provider}}}");
		AppMain.API
				.put("publisher-ebooks",
						"http://203.150.225.223/stoubookapi/api/publisher-ebooks.ashx?cid={{{cid}}}&count={{{count}}}");
		AppMain.API
				.put("addfan",
						"http://203.150.225.223/stoubookapi/api/addfan.ashx?pcid={{{pcid}}}&cid={{{cid}}}&did={{{did}}}");
		AppMain.API
				.put("top50",
						"http://203.150.225.223/stoubookapi/api/top50.ashx?type={{{type}}}");
		AppMain.API
				.put("top100",
						"http://203.150.225.223/stoubookapi/api/top100.ashx?type={{{type}}}&period={{{period}}}&catid={{{catid}}}&retina={{{retina}}}");
		//
		AppMain.API
				.put("ebooks-detail",
						"http://203.150.225.223/stoubookapi/api/ebooks-detail.ashx?chcode={{{chcode}}}&bid={{{bid}}}&cid={{{cid}}}&did={{{did}}}&version=1.5");
		AppMain.API
				.put("ebooks-detail-short",
						"http://203.150.225.223/stoubookapi/api/ebooks-detail-short.ashx?bid={{{bid}}}");

		AppMain.API
				.put("customer-detail",
						"http://203.150.225.223/stoubookapi/api/customer-detail.ashx?cid={{{cid}}}");

		AppMain.API
				.put("comments-list",
						"http://203.150.225.223/stoubookapi/api/comments-list.ashx?bid={{{bid}}}&page={{{page}}}");
		AppMain.API
				.put("download",
						"http://stoubook.com/admin/download.ashx?file={{{file}}}&cid={{{cid}}}&did={{{did}}}");
		AppMain.API
				.put("update-bookshelf",
						"http://203.150.225.223/stoubookapi/api/update-bookshelf.ashx?cid={{{cid}}}&did={{{did}}}");
		AppMain.API
				.put("fans-update",
						"http://203.150.225.223/stoubookapi/api/fans-update.ashx?cid={{{cid}}}");

		AppMain.API
				.put("addfavorite",
						"http://203.150.225.223/stoubookapi/api/addfavorite.ashx?bid={{{bid}}}&cid={{{cid}}}&add={{{add}}}");
		AppMain.API
				.put("deletebookshelf",
						"http://203.150.225.223/stoubookapi/api/deletebookshelf.ashx?bid={{{bid}}}&cid={{{cid}}}");
		AppMain.API
				.put("update-bookshelf-pointer",
						"http://203.150.225.223/stoubookapi/api/update-bookshelf-pointer.ashx?bid={{{bid}}}&cid={{{cid}}}&pointer={{{pointer}}}");
		AppMain.API
				.put("bookshelf-list",
						"http://203.150.225.223/stoubookapi/api/bookshelf-list.ashx?email={{{email}}}&password={{{password}}}&type={{{type}}}&retina={{{retina}}}");
		AppMain.API
				.put("check_student_stou",
						"http://203.150.225.223/stoubookapi/api/check-register.php?apikey=f4666c85ab34b5397519ef4b4e471b43&student_id={{{student_id}}}&student_name={{{student_name}}}&student_surname={{{student_surname}}}&student_birthdate={{{student_birthdate}}}&student_email={{{student_email}}}&student_password={{{student_password}}}");
		AppMain.API
				.put("comments-list",
						"http://203.150.225.223/stoubookapi/api/comments-list.ashx?bid={{{bid}}}&page={{{page}}}");
		AppMain.API
				.put("post-comment",
						"http://203.150.225.223/stoubookapi/api/post-comment.ashx?cid={{{cid}}}&bid={{{bid}}}&comment={{{comment}}}&name={{{name}}}&rating={{{rating}}}");

		AppMain.API.put("news-cover",
				"http://203.150.225.223/stoubookapi/images/api/{{{file}}}");
		AppMain.API
				.put("book-cover",
						"http://203.150.225.223/stoubookapi/covers/{{{id}}}/{{{file}}}");
		AppMain.API
				.put("publishers-cover",
						"http://203.150.225.223/stoubookapi/publishers/{{{cid}}}/{{{file}}}");

		// STOU
		// http://203.150.225.223/stoubookapi/api/
		// http://www.ebooks.in.th/
		// http://api.ebooks.in.th/

	}

	public static String getAPIbyRefKey(String key, String queryString) {
		queryString = queryString.replace("?", "");
		String query[] = queryString.split("&");
		if (query.length > 0) {
			Hashtable<String, Object> params = new Hashtable<String, Object>();
			for (String each : query) {
				if (each.contains("=")) {
					String item[] = each.split("=");
					if (item.length == 2) {
						params.put(item[0], item[1]);
					} else {
						params.put(item[0], "");
					}
				}
			}
			return getAPIbyRefKey(key, params);
		}
		return getAPIbyRefKey(key, new Hashtable<String, Object>());
	}

	public static String getAPIbyRefKey(String key,
			Hashtable<String, Object> parameter) {
		if (AppMain.API == null) {
			AppMain.setAPIRefKey();
		}
		if (AppMain.API.containsKey(key)) {
			String newUrl = AppMain.API.get(key);
			if (parameter != null) {
				boolean notFound = false;
				for (String params : AppMain.PARAMS) {
					if (parameter.containsKey(params)) {
						newUrl = newUrl.replace("{{{" + params + "}}}",
								parameter.get(params).toString());
					} else {
						notFound = true;
					}
				}
				if (notFound) {
					// throw new
					// Exception("At least one key's parameter find not found");
				}
			}
			if (newUrl.indexOf("{{{") > -1) {
				// throw new
				// Exception("Has a least one parameter is empty in : " +
				// newUrl);
			}
			return newUrl;
		} else {
			// throw new Exception("API Key's not found");
		}
		return null;
	}

	public static String getAPIRefKeyByURL(String url) {
		String key = url.substring(url.lastIndexOf("/") + 1);
		String queryString[] = key.split("\\?");
		queryString[0] = queryString[0].substring(0,
				queryString[0].lastIndexOf("."));
		return queryString[0];
	}

	public static String getTag() {
		return TAG;
	}

	public static void setCID(String ConfigValueUID) {
		CID = java.lang.Integer.parseInt(ConfigValueUID);
	}

	public static int getCID() {
		return CID;
	}

}