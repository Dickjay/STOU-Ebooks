package com.porar.ebooks.stou;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import com.porar.ebooks.image2ebooks.EbooksPageFile;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.model.Model_Setting;

import com.porar.ebooks.model.Model_SettingShelf;


@SuppressLint({ "NewApi", "DefaultLocale" })
public class Class_Manage {

	private static final String fileConfig = "Config.porar";
	private static final String fileModel = "model_detail";

	public static String CURRENT_EBOOKS_FILE_PATH = Environment
			.getExternalStorageDirectory() + "/ebooks.in.th";

	public static Hashtable<Integer, String> getEbooksFileList(
			Context mContext, int bookingId) {
		Hashtable<Integer, String> fileList = new Hashtable<Integer, String>();
		try {
			String folderPath = CURRENT_EBOOKS_FILE_PATH + "/" + bookingId;
			File directory = new File(folderPath);
			for (File files : directory.listFiles()) {
				if (files.isFile()) {
					String fileName = files.getName();
					fileName = fileName.replaceAll("ebooks_" + bookingId
							+ "_page_", "");
					fileName = fileName.replace(".ebooks", "");
					int pageNum = Integer.parseInt(fileName);
					fileList.put(pageNum, files.getAbsolutePath());
				}
			}
			Hashtable<Integer, String> List = new Hashtable<Integer, String>();
			Object[] keys = fileList.keySet().toArray();
			Arrays.sort(keys);
			for (Object key : keys) {
				// Log.i(AppMain.getTag(),key + " - " + fileList.get(key));
				List.put((Integer) key, fileList.remove(key));
			}
			return List;
		} catch (NullPointerException e) {
			// Log.e(AppMain.getTag(), "Manage : NullPointerException[" +
			// e.getMessage() + "]");
			return null;
		}
	}

	public static Hashtable<Integer, Model_Ebooks_Page> getEbooksFileList(
			Context mContext, int customerId, int bookingId, String bookName,
			int countPage) {
		Hashtable<Integer, Model_Ebooks_Page> fileList = new Hashtable<Integer, Model_Ebooks_Page>();
		try {
			String eachFile = "";
			// String folderPath =
			// mContext.getExternalCacheDir().getAbsolutePath() +
			// "/ebooks.in.th/" + bookingId;
			String folderPath = Environment.getExternalStorageDirectory()
					+ "/ebooks.in.th/" + bookingId;
			File directory = new File(folderPath);
			if (directory.exists()) {
				directory.mkdirs();
			}
			for (int i = 1; i <= countPage; i++) {
				eachFile = folderPath + "/ebooks_" + bookingId + "_page_" + i
						+ ".ebooks";
				Model_Ebooks_Page obj = new Model_Ebooks_Page(customerId,
						bookingId, bookName, i, eachFile);
				fileList.put(i, obj);

			}
			return fileList;
		} catch (NullPointerException e) {
			// Log.e(AppMain.getTag(), "Manage : NullPointerException[" +
			// e.getMessage() + "]");
			return null;
		}
	}

	public static boolean checkCoverCache(Context context, String urlFile) {
		File file = new File(context.getFilesDir(), urlToCoverName(urlFile));
		if (file.exists()) {
			file = null;
			return true;
		}
		file = null;
		return false;
	}

	public static boolean checkPublisherCache(Context context, String urlFile) {
		File file = new File(context.getFilesDir(), urlToPublisherName(urlFile));
		if (file.exists()) {
			file = null;
			return true;
		}
		file = null;
		return false;
	}

	public static boolean checkFileCache(Context context, String urlFile) {
		File file = new File(context.getFilesDir(), urlToFileName(urlFile));
		// Log.i(AppMain.getTag(),"Check File[" + file.getPath() + "]");
		if (file.exists()) {
			file = null;
			return true;
		}
		file = null;
		return false;
	}

	public static boolean checkEbooksCache(Context context, String FileName) {
		// Log.i(AppMain.getTag(),"Class_Manage - checkEbooksCache["+FileName+"]");
		File file = new File(context.getFilesDir(), FileName);
		if (file.exists()) {
			file = null;
			return true;
		}
		file = null;
		return false;
	}

	public static boolean deleteEbooksCache(Context context, int bookid) {
		// Log.i(AppMain.getTag(), "Class_Manage - deleteEbooksCache[" + bookid
		// + "]");

		File file = new File(context.getFilesDir(), "ebooks_" + bookid
				+ ".porar");
		File file2 = new File(context.getFilesDir(), "ebooks_" + bookid
				+ ".pdf");
		File file3 = new File(Environment.getExternalStorageDirectory(),
				"ebooks.in.th/ebooks_" + bookid + ".pdf");
		File file4 = new File(context.getFilesDir(), "ebooks_" + bookid
				+ ".hash");
		File file5 = new File(Environment.getExternalStorageDirectory(),
				"ebooks.in.th/" + bookid);

		try {
			if (file.delete()) {
				// Log.i(AppMain.getTag(), "Class_Manage - deleteEbooksCache[" +
				// file.getName() + "]");
				if (file2.exists()) {
					if (file2.delete()) {
						// Log.i(AppMain.getTag(),
						// "Class_Manage - deleteEbooksCache[" + file2.getName()
						// + "]");
					}
				}
				if (file3.exists()) {
					if (file3.delete()) {
						// Log.i(AppMain.getTag(),
						// "Class_Manage - deleteEbooksCache[" + file3.getName()
						// + "]");
					}
				}
				if (file4.exists()) {
					if (file4.delete()) {
						// Log.i(AppMain.getTag(),
						// "Class_Manage - deleteEbooksCache[" + file4.getName()
						// + "]");
					}
				}
				if (file5.exists()) {
					deleteNon_EmptyDir(file5);
				}
				return true;
			}
		} catch (Exception e) {
			// None
		}

		file = null;
		file2 = null;
		file3 = null;
		file4 = null;

		return false;
	}

	public static boolean deleteNon_EmptyDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (String each : children) {
				boolean success = deleteNon_EmptyDir(new File(dir, each));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static ArrayList<String> LoadEbooksEmbedList(Context context) {
		AssetManager assetManager = context.getAssets();
		ArrayList<String> list = new ArrayList<String>();
		try {
			String[] files = assetManager.list("embed_book");
			for (String filename : files) {
				if (filename.toLowerCase().endsWith(".porar")) {
					EbooksShelfHeaderFile header = Class_Manage
							.LoadEbooksShelfHeaderFile_of_Embed(context,
									"embed_book/" + filename);
					if (header != null) {
						Model_Customer_Detail customer = header
								.getModelCustomer();
						if (customer.getEmail().equals("support@ebooks.in.th")) {
							Model_EBook_Shelf_List shelf = header
									.getModelShelf();
							list.add(shelf.getBID() + "");
						}

					}
				}
			}
		} catch (Exception e) {

		}
		return list;
	}

	// ebooks
	public static ArrayList<Model_EBook_Shelf_List> getEbooksShelfInOffLineMode(
			Context context) {
		ArrayList<Model_EBook_Shelf_List> list = new ArrayList<Model_EBook_Shelf_List>();
		File obj = new File(context.getFilesDir() + "/");

		ArrayList<String> listembed = new ArrayList<String>();
		AssetManager assetManager = context.getAssets();
		String[] file_dummy = null;
		try {
			file_dummy = assetManager.list("embed_book");
			for (String filename : file_dummy) {
				if (filename.toLowerCase().endsWith(".porar")) {
					listembed.add(filename);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		for (File file : obj.listFiles(new FileFilter_EbooksShelf())) {
			if (!file.getName().equals(fileConfig)
					&& !file.getName().equals("customer_detail.porar")) {
				try {
					EbooksShelfHeaderFile ebooks = LoadEbooksShelfHeaderFile(
							context, file);
					if (ebooks.getModelCustomer().getEmail()
							.equals(Shared_Object.getCustomerDetail.getEmail())) {
						if (listembed.toString().contains(
								"ebooks_" + ebooks.getModelShelf().getBID()
										+ ".porar")) {
							// not create
						} else {
							list.add(ebooks.getModelShelf());
						}
					}
				} catch (NullPointerException e) {
					file.delete();
					// Log.e(AppMain.getTag(), "Error Header File : " +
					// file.getName());
				}
			}
		}

		// Edit
		// get Asset Embed File
		// embed
		// try {
		// File sdDir = android.os.Environment.getExternalStorageDirectory();
		// File cacheDir = new File(sdDir, "data/hbook/embed/");
		//
		// for (File fname : cacheDir.listFiles()) {
		// if (fname.getName().endsWith(".porar")) {
		// EbooksShelfHeaderFile header =
		// Class_Manage.LoadEbooksShelfHeaderFile(context, fname);
		// if (header != null) {
		// list.add(header.getModelShelf());
		// }
		// }
		// }
		// } catch (NullPointerException e) {
		// // TODO: handle exception
		// Log.e("", "NullPointerException dot worry " + e.toString());
		// }

		// try {
		// String[] files = assetManager.list("embed_book");
		// for (String filename : files) {
		// if (filename.toLowerCase().endsWith(".porar")) {
		// EbooksShelfHeaderFile header =
		// Class_Manage.LoadEbooksShelfHeaderFile_of_Embed(context,
		// "embed_book/" + filename);
		// if (header != null) {
		// Model_Customer_Detail customer = header.getModelCustomer();
		// if (customer.getEmail().equals("support@ebooks.in.th")) {
		// list.add(header.getModelShelf());
		// }
		// }
		// }
		// }
		// } catch (Exception e) {
		//
		// }
		return list;
	}

	public static void ListFileInApp(Context context) {
		// Log.i(AppMain.getTag(), "======== List Files in App ========");
		try {
			File obj = new File(context.getFilesDir() + "/");
			for (File file : obj.listFiles()) {
				if (!file.getName().startsWith("cover_")) {
					Log.i(AppMain.getTag(), file.getName());
					if (file.getName().startsWith(fileModel)) {
						// if (file.delete()) {
						// Log.i(AppMain.getTag(), "delete " + file.getName());
						// }
					}
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// Log.i(AppMain.getTag(), "===================================");
	}

	public static void syncCoverseByShelf(Context context,
			LinkedList<String> arrBID) {
		File obj = new File(context.getFilesDir() + "/");
		for (File file : obj.listFiles(new FileFilter_Cover())) {
			String BID = file.getName().split("\\.")[0];
			BID = BID.split("_")[1];
			if (!arrBID.contains(BID)) {
				// Log.d(AppMain.getTag(), "Delete Cover : " + file.getName());
				file.delete();
			}
		}
	}

	// ebooks vesion 1.04
	public static Model_Customer_Detail LoadCustomerHeaderFile(Context context) {
		byte[] bytes = null;
		File file = new File(context.getFilesDir() + "/"
				+ "customer_detail.porar");
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				// long free = Debug.getNativeHeapFreeSize() - 1024;
				byte[] buf = new byte[512 * 1024];
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
				bytes = bos.toByteArray();
				return (Model_Customer_Detail) deserializeObject(bytes);
			} catch (IOException e) {
				// Log.e(AppMain.getTag(),
				// "Class_Manage - LoadCustomerHeaderFile() - IOException[" +
				// e.getMessage() + "]");
			} catch (NullPointerException e) {
				// Log.e(AppMain.getTag(),
				// "Class_Manage - LoadCustomerHeaderFile() - NullPointerException["
				// + e.getMessage() + "]");
			} catch (ClassNotFoundException e) {
				file.delete();
				// Log.e(AppMain.getTag(),
				// "Class_Manage - LoadCustomerHeaderFile() - ClassNotFoundException["
				// + e.getMessage() + "]");
			}
		}
		return new Model_Customer_Detail(null);
	}

	public static EbooksShelfHeaderFile LoadEbooksShelfHeaderFile_of_Embed(
			Context context, String filename) {
		EbooksShelfHeaderFile obj = null;
		byte[] bytes = null;
		try {
			AssetManager mgr = context.getResources().getAssets();
			InputStream fileInputStream = mgr.open(filename,
					AssetManager.ACCESS_BUFFER);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			bytes = bos.toByteArray();
			obj = (EbooksShelfHeaderFile) deserializeObject(bytes);
		} catch (IOException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - IOException[" +
			// e.getMessage() + "]");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - ClassCastException["
			// + e.getMessage() + "]");
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - OutOfMemoryError["
			// + e.getMessage() + "]");
		}
		return obj;
	}

	public static InputStream LoadHowToZipStream(Context context) {
		try {
			AssetManager mgr = context.getResources().getAssets();
			InputStream fileInputStream = mgr.open("how_to.zip",
					AssetManager.ACCESS_BUFFER);
			return fileInputStream;
		} catch (IOException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - IOException[" +
			// e.getMessage() + "]");
		} catch (ClassCastException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - ClassCastException["
			// + e.getMessage() + "]");
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - OutOfMemoryError["
			// + e.getMessage() + "]");
		}
		return null;
	}

	public static EbooksPageFile readEbookPageEmbedFile(Context context,
			int BID, String filename) {
		EbooksPageFile obj = null;
		byte[] bytes = null;
		try {
			AssetManager mgr = context.getResources().getAssets();
			InputStream fileInputStream = mgr.open(BID + "/" + filename,
					AssetManager.ACCESS_BUFFER);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			bytes = bos.toByteArray();
			obj = (EbooksPageFile) deserializeObject(bytes);
		} catch (IOException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - IOException[" +
			// e.getMessage() + "]");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - ClassCastException["
			// + e.getMessage() + "]");
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - OutOfMemoryError["
			// + e.getMessage() + "]");
		}
		return obj;
	}

	public static Drawable getEmbedCoverByBID(Context context, int bid) {
		try {
			String filename = "embed_book/" + bid + "/cover.jpg";
			AssetManager mgr = context.getResources().getAssets();
			InputStream fileInputStream = mgr.open(filename,
					AssetManager.ACCESS_BUFFER);
			return Drawable.createFromStream(fileInputStream, "cover.jpg");
		} catch (IOException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - getEmbedCoverByBID() - IOException[" +
			// e.getMessage() + "]");
		}
		return null;
	}

	public static EbooksShelfHeaderFile LoadEbooksShelfHeaderFile(
			Context context, File file) {
		EbooksShelfHeaderFile obj = null;
		byte[] bytes = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			bytes = bos.toByteArray();
			obj = (EbooksShelfHeaderFile) deserializeObject(bytes);
		} catch (IOException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - IOException[" +
			// e.getMessage() + "]");
		} catch (NullPointerException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - NullPointerException["
			// + e.getMessage() + "]");
		} catch (ClassNotFoundException e) {
			file.delete();
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - ClassNotFoundException["
			// + e.getMessage() + "]");
		} catch (ClassCastException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - ClassCastException["
			// + e.getMessage() + "]");
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksShelfHeaderFile() - OutOfMemoryError["
			// + e.getMessage() + "]");
		}
		return obj;
	}

	public static String base64ToEbooksFile(String base64FileName) {
		return "ebooks_" + base64FileName + ".porar";
	}

	public static String urlToCoverName(String url) {
		Integer bid_Cover = null;
		String fileType_Cover = "";
		try {
			String cover = "";
			cover = url.substring(url.indexOf("covers"));
			cover = cover.split("/")[1];
			bid_Cover = Integer.parseInt(cover);
			cover = url.substring(url.lastIndexOf("."));
			fileType_Cover = cover;
		} catch (StringIndexOutOfBoundsException e) {
			fileType_Cover = "";
		} finally {
			fileType_Cover = "cover_" + bid_Cover + fileType_Cover;
		}
		return fileType_Cover.toLowerCase();
	}

	public static String urlToPublisherName(String url) {
		try {
			if (url.matches("(.*)publishers/[0-9]+(.*)")) {
				String publisher = url.substring(url.indexOf("publishers"));
				publisher = publisher.split("/")[1];
				Integer bid_Publish = Integer.parseInt(publisher);
				publisher = url.substring(url.lastIndexOf("."));
				String fileType_Publish = "publishers_" + bid_Publish
						+ publisher;
				// Log.i(AppMain.getTag(),"1--------------["+
				// fileType_Publish+"]");
				return fileType_Publish.toLowerCase();
			} else if (url.matches("(.*)images/(.*)")) {
				String temp = urlToFileName(url);
				// Log.i(AppMain.getTag(),"2--------------["+temp+"]");
				return temp;
			}
		} catch (StringIndexOutOfBoundsException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - StringIndexOutOfBoundsException[" + url + "]");
			return "";
		}
		return "";
	}

	public static String urlToFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1).toLowerCase();
	}

	public static String urlEncodeName(String url) {
		String urlpath = url.substring(0, url.lastIndexOf("/"));
		String urlname = url.substring(url.lastIndexOf("/") + 1);
		urlpath = urlpath + "/" + URLEncoder.encode(urlname);
		urlpath = urlpath.replace("+", "%20");// Fix
		return urlpath;
	}

	public static String urlFileType(String url) {
		return url.substring(url.lastIndexOf(".") + 1).toLowerCase();
	}

	public static Bitmap getFBPictureByFBID(Context context, String FBFileName) {
		Bitmap imgData = null;
		String filePath = context.getFilesDir() + "/" + FBFileName;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inTempStorage = new byte[16 * 1024];
		options.inPurgeable = true;
		imgData = Bitmap.createScaledBitmap(
				BitmapFactory.decodeFile(filePath, options), 48, 48, true);
		return imgData;
	}

	public static Bitmap getBitmapCache(Context context, String URL,
			String cacheType) {
		Bitmap imgData = null;
		try {
			String filePath = null;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			if (cacheType.equals("C")) {
				filePath = context.getFilesDir() + "/" + urlToCoverName(URL);
			} else if (cacheType.equals("P")) {
				filePath = context.getFilesDir() + "/"
						+ urlToPublisherName(URL);
			} else if (cacheType.equals("F")) {
				filePath = context.getFilesDir() + "/" + urlToFileName(URL);
			}
			opts.inTempStorage = new byte[32 * 1024];
			opts.inPurgeable = true;
			return BitmapFactory.decodeFile(filePath, opts);
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(), e.getMessage());
		} catch (NullPointerException e) {
			// Log.e(AppMain.getTag(), "Class_Manage - getBitmapCache(" +
			// cacheType + ") - NullPointerException");
		}
		return imgData;
	}

	public static boolean SaveConfig(Context context, Object obj) {
		try {
			FileOutputStream out = new FileOutputStream(context.getFilesDir()
					+ "/" + fileConfig);
			out.write(serializeObject(obj));
			out.flush();
			out.close();
			// Log.i(AppMain.getTag(), "Class_Manage - Save Config Complete");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	public static boolean SaveModel_Detail(Context context, Object obj, int bid) {
		try {
			File file = new File(getCacheDirectory(context), fileModel + bid
					+ ".porar");
			FileOutputStream out = new FileOutputStream(file);
			byte[] data = Class_Manage.serializeObject(obj);
			out.write(data);
			out.flush();
			out.close();

			Log.v(AppMain.getTag(), "Class_Manage - Save Model_Detail Complete"
					+ file.getPath());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean SaveSetting(Context context, Object obj) {
		try {
			File file = new File(getCacheDirectory(context), "setting.porar");
			FileOutputStream out = new FileOutputStream(file);
			byte[] data = Class_Manage.serializeObject(obj);
			out.write(data);
			out.flush();
			out.close();

			Log.v(AppMain.getTag(), "Class_Manage - Save Model_Detail Complete"+ file.getPath());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean SaveSettingShelf(Context context, Object obj) {
		try {
			File file = new File(getCacheDirectory(context),
					"settingshelf.porar");
			FileOutputStream out = new FileOutputStream(file);
			byte[] data = Class_Manage.serializeObject(obj);
			out.write(data);
			out.flush();
			out.close();

			Log.v(AppMain.getTag(), "Class_Manage - Save Model_Detail Complete"
					+ file.getPath());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static Model_Setting getModel_Setting(Context context) {
		Model_Setting model_Setting = null;
		byte[] bytes = null;
		File file = null;
		try {
			file = new File(getCacheDirectory(context), "setting.porar");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {
				return model_Setting;
			}
			bytes = bos.toByteArray();
		} catch (FileNotFoundException e) {
			//
			return model_Setting;
		} finally {
			try {

				model_Setting = (Model_Setting) deserializeObject(bytes);

			} catch (NullPointerException e) {
				return model_Setting;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return model_Setting;
	}

	public static Model_SettingShelf getModel_SettingShelf(Context context) {
		Model_SettingShelf model_Setting = null;
		byte[] bytes = null;
		File file = null;
		try {
			file = new File(getCacheDirectory(context), "settingshelf.porar");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {
				return model_Setting;
			}
			bytes = bos.toByteArray();
		} catch (FileNotFoundException e) {
			//
			return model_Setting;
		} finally {
			try {

				model_Setting = (Model_SettingShelf) deserializeObject(bytes);

			} catch (NullPointerException e) {
				return model_Setting;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return model_Setting;
	}

	@SuppressLint("SimpleDateFormat")
	public static Model_Ebooks_Detail getModel_Detail(Context context, int bid) {
		Model_Ebooks_Detail ebooks_Detail = null;
		byte[] bytes = null;
		File file = null;
		try {
			file = new File(getCacheDirectory(context), fileModel + bid
					+ ".porar");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {
				return ebooks_Detail;
			}
			bytes = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ebooks_Detail;
		} finally {
			try {

				ebooks_Detail = (Model_Ebooks_Detail) deserializeObject(bytes);
				SimpleDateFormat formatter = new SimpleDateFormat();
				try {
					Date date2 = formatter.parse(ebooks_Detail.getDateTime());
					Date epoch = date2;

					Date today = new Date();
					String dt = formatter.format(today);
					Date date3 = formatter.parse(dt.toString());

					long diff = date3.getTime() - epoch.getTime();
					long distance = (diff / (1000 * 60 * 60 * 24));

					if (distance >= 7) {
						if (file.exists()) {
							file.delete();
						}
						ebooks_Detail = null;
						Log.d(AppMain.getTag(), "file cache over 7 day");
					} else {
						Log.d(AppMain.getTag(), "file cache day" + distance);
					}
				} catch (Exception e) {
					return ebooks_Detail;
				}

			} catch (NullPointerException e) {
				return ebooks_Detail;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ebooks_Detail;
	}

	@SuppressLint("SimpleDateFormat")
	public static Model_Ebooks_Detail getModel_DetailByNever(Context context,
			int bid) {
		Model_Ebooks_Detail ebooks_Detail = null;
		byte[] bytes = null;
		File file = null;
		try {
			file = new File(getCacheDirectory(context), fileModel + bid
					+ ".porar");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {
				return ebooks_Detail;
			}
			bytes = bos.toByteArray();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ebooks_Detail;
		} finally {
			try {

				ebooks_Detail = (Model_Ebooks_Detail) deserializeObject(bytes);
				// SimpleDateFormat formatter = new SimpleDateFormat();
				// try {
				// Date date2 = formatter.parse(ebooks_Detail.getDateTime());
				// Date epoch = date2;
				//
				// Date today = new Date();
				// String dt = formatter.format(today);
				// Date date3 = formatter.parse(dt.toString());
				//
				// long diff = date3.getTime() - epoch.getTime();
				// long distance = (diff / (1000 * 60 * 60 * 24));
				//
				// if (distance >= 7) {
				// if (file.exists()) {
				// file.delete();
				// }
				// ebooks_Detail = null;
				// Log.d(AppMain.getTag(), "file cache over 7 day");
				// } else {
				// Log.d(AppMain.getTag(), "file cache day" + distance);
				// }
				// } catch (Exception e) {
				// return ebooks_Detail;
				// }

				return ebooks_Detail;

			} catch (NullPointerException e) {
				return ebooks_Detail;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ebooks_Detail;

	}

	private static File getCacheDirectory(Context context) {
		String sdState = android.os.Environment.getExternalStorageState();
		File cacheDir;

		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();
			cacheDir = new File(sdDir, "data/ebooks.in.th/detail/"
					+ Shared_Object.getCustomerDetail.getFirstName());
		} else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir;
	}

	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();
			byte[] buf = bos.toByteArray();
			return buf;
		} catch (IOException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - serializeObject() - IOException[" +
			// e.getMessage() + "]");
			return null;
		} catch (Exception e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - serializeObject() - Other Exception");
			return null;
		}
	}

	public static Boolean DeleteAllImageCropTemp() {
		boolean delete = false;
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					"data/ebooks.in.th/image_crop/temp/"
							+ Shared_Object.getCustomerDetail.getFirstName()
							+ "/");
			for (File f : file.listFiles()) {
				if (f.delete()) {
					Log.v("", "" + f.getPath());
				}
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

		return delete;
	}

	public static File getCacheDirectoryForCrop(Context context) {
		String sdState = android.os.Environment.getExternalStorageState();
		File cacheDir;

		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();
			cacheDir = new File(sdDir, "data/ebooks.in.th/image_crop/temp/"
					+ Shared_Object.getCustomerDetail.getFirstName());
		} else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir;
	}

	public static File getCacheDirectoryForSave(Context context) {
		String sdState = android.os.Environment.getExternalStorageState();
		File cacheDir;

		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();
			cacheDir = new File(sdDir, "data/ebooks.in.th/image_crop/save/"
					+ Shared_Object.getCustomerDetail.getFirstName());
		} else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir;
	}

	public static Config_Data LoadConfig(Context context) {
		Config_Data obj = new Config_Data();
		byte[] bytes = null;
		try {
			File file = new File(context.getFilesDir() + "/" + fileConfig);
			FileInputStream fis = new FileInputStream(file);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {
				return obj;
			}
			bytes = bos.toByteArray();
		} catch (FileNotFoundException e) {
			// Log.e(AppMain.getTag(), "Class_Manage - Config[None]");
			return obj;
		} finally {
			try {
				obj = (Config_Data) deserializeObject(bytes);
			} catch (NullPointerException e) {
				return obj;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}

	public static Object deserializeObject(byte[] b) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream(b));
		Object object = in.readObject();
		in.close();
		return object;
	}

	public static String getFaceBookGraphURL(String URL) {
		if (URL.startsWith("https://graph.facebook.com") && URL.endsWith("picture")) {
			String txtResult = "";
			try {
				HttpURLConnection con = (HttpURLConnection) (new URL(URL).openConnection());
				con.setInstanceFollowRedirects(false);
				con.connect();
				txtResult = con.getHeaderField("Location");
				// Log.i(AppMain.getTag(), "Facebook Picture - " + txtResult);
				return txtResult;

			} catch (MalformedURLException e) {
				return URL;
			} catch (IOException e) {
				return URL;
			} catch (RuntimeException e) {
				return URL;
			}
		}
		return URL;
	}

	public static boolean checkFBGraphURL(String url) 
	
	{
		if (url.startsWith("https://graph.facebook.com")
				&& url.endsWith("picture")) 
		{
			return true;
		}
		return false;
	}

	public static String getFBIDByGraphURL(String url, Context context) {
		String find = "graph.facebook.com";
		String fid = url.substring(url.indexOf(find) + find.length() + 1);
		fid = fid.substring(0, fid.indexOf("/"));
		return fid;
	}

	public static String checkFileByFBID(String FBID, Context context) {
		File[] files = new File(context.getFilesDir().getPath()).listFiles();
		for (File file : files) {
			if (file.getName().matches("(.*)" + FBID + "(.*)")) {
				// i(AppMain.getTag(),"Match File : " + file.getName());
				return file.getName();
			}
		}
		// Log.i(AppMain.getTag(),"Match File : NULL");
		return "";
	}

	public static boolean SaveEbooksObject(Context context, Object obj,String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(context.getFilesDir()
					+ "/" + fileName);
			byte[] data = Class_Manage.serializeObject(obj);
			int length = 512 * 1024;// 512K
			int offset = 0;
			while (data[offset] != -1) {
				if ((offset + length) > data.length) {
					length = data.length - offset;
					out.write(data, offset, length);
					break;
				} else {
					out.write(data, offset, length);
					offset += length;
				}
				Thread.sleep(1);
			}
			out.flush();
			out.close();

			// Log.d(AppMain.getTag(), "Save : " + fileName);

			return true;
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(), "Class_Manage  - OutOfMemoryError");
		} catch (IOException e) {
			// Log.e(AppMain.getTag(), "Class_Manage -  IOException[2]");
		} catch (InterruptedException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - InterruptedException[2]");
		}
		return false;
	}

	public static boolean SaveEbooksObjectEmbed(Context context, Object obj,String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			byte[] data = Class_Manage.serializeObject(obj);
			int length = 512 * 1024;// 512K
			int offset = 0;
			while (data[offset] != -1) {
				if ((offset + length) > data.length) {
					length = data.length - offset;
					out.write(data, offset, length);
					break;
				} else {
					out.write(data, offset, length);
					offset += length;
				}
				Thread.sleep(1);
			}
			out.flush();
			out.close();

			// Log.d(AppMain.getTag(), "Save : " + fileName);

			return true;
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(), "Class_Manage  - OutOfMemoryError");
		} catch (IOException e) {
			// Log.e(AppMain.getTag(), "Class_Manage -  IOException[2]");
		} catch (InterruptedException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - InterruptedException[2]");
		}
		return false;
	}

	public static EbooksHashFile LoadEbooksHashFile(Context context, File file) {
		EbooksHashFile obj = null;
		byte[] bytes = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			long free = Debug.getNativeHeapFreeSize() - 1024;
			byte[] buf = new byte[(int) free];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			bytes = bos.toByteArray();
			obj = (EbooksHashFile) deserializeObject(bytes);
		} catch (IOException e) {
			// Log.e("Ebooks.in.th",
			// "Class_Manage - LoadEbooksHashFile() - IOException[" +
			// e.getMessage() + "]");
		} catch (NullPointerException e) {
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksHashFile() - NullPointerException[" +
			// e.getMessage() + "]");
		} catch (ClassNotFoundException e) {
			file.delete();
			// Log.e(AppMain.getTag(),
			// "Class_Manage - LoadEbooksHashFile() - ClassNotFoundException[" +
			// e.getMessage() + "]");
		}
		return obj;
	}
}