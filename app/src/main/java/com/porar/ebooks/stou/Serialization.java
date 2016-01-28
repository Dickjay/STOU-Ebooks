package com.porar.ebooks.stou;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.widget.Toast;

import com.porar.ebooks.image2ebooks.EbooksPageFile;
import com.porar.ebooks.utils.StaticUtils;

public class Serialization {
	public static int bufferPattern = (8326);
	private static String TAG = "Ebooks.in.th";

	public static boolean serializeEbooksAndSave2(EbooksPageFile obj, String savePath) {
		return writeEbooksPattern(obj, savePath);
	}

	private static boolean writeEbooksPattern(EbooksPageFile obj, String savePath) {
		int sampleSize = 1;
		Log.e(TAG, "Raw Byte : " + obj.getRawBitmapLength());
		if (obj.getRawBitmapLength() > (1024 * 1024)) {
			sampleSize = 2;
		}

		boolean enableSave = false;
		boolean indexOver = false;
		Bitmap newBitmap = null;
		int rawByteLength = obj.getRawBitmap().length;
		Options opts = new Options();
		opts.inTempStorage = new byte[16 * 1024];
		opts.inJustDecodeBounds = true;

		while (enableSave == false) {
			// ---------------------------------------------------------------
			if (sampleSize > 16) {
				indexOver = true;
				// Log.e(TAG,"Index Over");
				// Log.d(TAG, "Reduce Ratio");
				break;
			}
			// ---------------------------------------------------------------
			if (indexOver) {
				try {
					// Reduce by Ratio
				} catch (OutOfMemoryError e) {
					Log.e(TAG, "Request Size : " + sampleSize);
					continue;
				}
			} else {
				try {
					opts.inSampleSize = sampleSize;
					// Log.d(TAG,"Sample Size : " + sampleSize);
					newBitmap = BitmapFactory.decodeByteArray(obj.getRawBitmap(), 0, rawByteLength, opts);
				} catch (OutOfMemoryError e) {
					Log.e(TAG, "OutOfMemory : Can't Serialize File");
					if (sampleSize == 1) {
						sampleSize += 1;
					} else {
						sampleSize += 2;
					}
					Log.e(TAG, "Request Size : " + sampleSize);
					continue;
				}
			}
			// ---------------------------------------------------------------
			ByteArrayOutputStream bos = null;
			try {
				bos = new ByteArrayOutputStream();
				newBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
				obj.setRawBitmap(bos.toByteArray());
				obj.setRawBitmapLength(bos.toByteArray().length);
				bos = null;
				newBitmap = null;
			} catch (OutOfMemoryError e) {
				Log.e(TAG, "Compress : OutOfMemoryError");
				if (sampleSize == 1) {
					sampleSize += 1;
				} else {
					sampleSize += 2;
				}
				Log.e(TAG, "Request Size : " + sampleSize);
				continue;
			} catch (NullPointerException e) {
				Log.e(TAG, "Compress : NullPointerException");
				if (sampleSize == 1) {
					sampleSize += 1;
				} else {
					sampleSize += 2;
				}
				Log.e(TAG, "Request Size : " + sampleSize);
				continue;
			}
			// ---------------------------------------------------------------
			try {
				bos = new ByteArrayOutputStream();
				ObjectOutputStream objOut = new ObjectOutputStream(bos);
				objOut.writeObject(obj);
				objOut.close();
				Log.e(TAG, "Enable to Save File");
			} catch (IOException e) {
				Log.e(TAG, "WriteObject : IOException[" + e.getMessage() + "]");
				e.printStackTrace();
				continue;
			} catch (OutOfMemoryError e) {
				Log.e(TAG, "WriteObject : OutOfMemoryError");
				if (sampleSize == 1) {
					sampleSize += 1;
				} else {
					sampleSize += 2;
				}
				Log.e(TAG, "Request Size : " + sampleSize);
				continue;
			}
			// ---------------------------------------------------------------
			try {
				byte[] bytes = bos.toByteArray();
				FileOutputStream out = new FileOutputStream(savePath);
				int bytesLength = bytes.length;
				int diffLength = 0;
				while (bytesLength > 0) {
					diffLength = bytesLength - bufferPattern;
					if (diffLength < bufferPattern) {
						out.write(bytes, diffLength, bufferPattern);//
						out.write(bytes, 0, diffLength);
						break;
					} else {
						out.write(bytes, diffLength, bufferPattern);
					}
					bytesLength -= bufferPattern;
					if (bytesLength < 0) {
						break;
					}
				}
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				Log.e(TAG, "FileOutputStream : FileNotFoundException[" + e.getMessage() + "]");
				e.printStackTrace();
				break;
			} catch (IOException e) {
				Log.e(TAG, "FileOutputStream : IOException[" + e.getMessage() + "]");
				e.printStackTrace();
				break;
			} catch (OutOfMemoryError e) {
				Log.e(TAG, "FileOutputStream : OutOfMemoryError");
				if (sampleSize == 1) {
					sampleSize += 1;
				} else {
					sampleSize += 2;
				}
				Log.e(TAG, "Request Size : " + sampleSize);
				continue;
			}
			// ---------------------------------------------------------------
			enableSave = true;
		}
		return true;
	}
	
	public static EbooksPageFile deserializeEbooksAndStore(final Context context, String savePath) {
	
		try {

			byte[] restore = readEbooksPattern(savePath);
			// ByteArrayInputStream bis = new ByteArrayInputStream(restore);
			BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(restore), new byte[4 * 1024].length);
			ObjectInputStream ois = new ObjectInputStream(bis);
			EbooksPageFile objTemp = (EbooksPageFile) ois.readObject();
			ois.close();
			bis.close();
			return objTemp;
		} catch (StreamCorruptedException e) {
			Log.e(TAG, "deserializeEbooksAndStore - StreamCorruptedException[" + e.getMessage() + "]");
			File file = new File(savePath);
			if (file.exists()) {
				file.delete();
				file = null;
				try {

					StaticUtils.handler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(context, "File is Corrupted", Toast.LENGTH_LONG).show();
						}
					});

				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		} catch (OptionalDataException e) {
			Log.e(TAG, "deserializeEbooksAndStore - OptionalDataException[" + e.getMessage() + "]");
		} catch (IOException e) {
			Log.e(TAG, "deserializeEbooksAndStore - IOException[" + e.getMessage() + "]");
			try {
				StaticUtils.handler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "File is Corrupted", Toast.LENGTH_LONG).show();
					}
				});

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "deserializeEbooksAndStore - ClassNotFoundException[" + e.getMessage() + "]");
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "deserializeEbooksAndStore - OutOfMemoryError[" + e.getMessage() + "]");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.e(TAG, "deserializeEbooksAndStore - NullPointerException[" + e.getMessage() + "]");
			e.printStackTrace();
		}
		return null;
	}

	public static EbooksPageFile deserializeEmbedEbooksAndStore(Context context, String savePath) {
		ByteArrayOutputStream loopBuffer;
		ArrayList<byte[]> byteListPattern;
		try {
			int byteRead = 0;
			byte[] buffer = new byte[bufferPattern];

			int loopBufferIndex = 0;
			int leftPad = 0;

			byteListPattern = new ArrayList<byte[]>();
			loopBuffer = new ByteArrayOutputStream();

			AssetManager mgr = context.getResources().getAssets();
			InputStream fis = mgr.open(savePath, AssetManager.ACCESS_BUFFER);
			while ((byteRead = fis.read(buffer)) != -1) {
				if ((loopBufferIndex + byteRead) >= bufferPattern) {
					loopBuffer.write(buffer, loopBufferIndex, (bufferPattern - loopBufferIndex));
					byteListPattern.add(loopBuffer.toByteArray());
					loopBuffer = new ByteArrayOutputStream();
					leftPad = (loopBufferIndex + byteRead) - bufferPattern;
					loopBuffer.write(buffer, bufferPattern, leftPad);
					loopBufferIndex = leftPad;
				} else {
					loopBuffer.write(buffer, loopBufferIndex, (loopBufferIndex + byteRead));
					loopBufferIndex += byteRead;
					if (fis.read() == -1) {
						byteListPattern.add(loopBuffer.toByteArray());
					}
				}
			}
			loopBuffer.close();
			fis.close();

			buffer = null;
			loopBuffer = null;

			loopBuffer = new ByteArrayOutputStream();
			for (int i = (byteListPattern.size() - 1); i >= 0; i--) {
				loopBuffer.write(byteListPattern.get(i));
				byteListPattern.remove(i);
			}

			byteListPattern = null;

			loopBuffer.flush();
			loopBuffer.close();
			byte[] restore = loopBuffer.toByteArray();

			ByteArrayInputStream bis = new ByteArrayInputStream(restore);

			restore = null;

			ObjectInputStream ois = new ObjectInputStream(bis);
			EbooksPageFile objTemp = (EbooksPageFile) ois.readObject();
			ois.close();
			bis.close();
			return objTemp;
		} catch (IOException e) {
			// Log.e(AppMain.getTag(), "Class_Manage - LoadEbooksShelfHeaderFile() - IOException[" + e.getMessage() + "]");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Log.e(AppMain.getTag(), "Class_Manage - LoadEbooksShelfHeaderFile() - ClassCastException[" + e.getMessage() + "]");
		} catch (OutOfMemoryError e) {
			// Log.e(AppMain.getTag(), "Class_Manage - LoadEbooksShelfHeaderFile() - OutOfMemoryError[" + e.getMessage() + "]");
		}
		return null;
	}

	private static byte[] readEbooksPattern(String filePath) {
		ByteArrayOutputStream loopBuffer;
		ArrayList<byte[]> byteListPattern;
		try {
			int byteRead = 0;
			byte[] buffer = new byte[bufferPattern];

			int loopBufferIndex = 0;
			int leftPad = 0;

			byteListPattern = new ArrayList<byte[]>();
			loopBuffer = new ByteArrayOutputStream();

			FileInputStream fis = new FileInputStream(filePath);
			while ((byteRead = fis.read(buffer)) != -1) {
				if ((loopBufferIndex + byteRead) >= bufferPattern) {
					loopBuffer.write(buffer, loopBufferIndex, (bufferPattern - loopBufferIndex));
					byteListPattern.add(loopBuffer.toByteArray());
					loopBuffer = new ByteArrayOutputStream();
					leftPad = (loopBufferIndex + byteRead) - bufferPattern;
					loopBuffer.write(buffer, bufferPattern, leftPad);
					loopBufferIndex = leftPad;
				} else {
					loopBuffer.write(buffer, loopBufferIndex, (loopBufferIndex + byteRead));
					loopBufferIndex += byteRead;
					if (fis.read() == -1) {
						byteListPattern.add(loopBuffer.toByteArray());
					}
				}
			}
			loopBuffer.close();
			fis.close();

			buffer = null;
			loopBuffer = null;

			loopBuffer = new ByteArrayOutputStream();
			for (int i = (byteListPattern.size() - 1); i >= 0; i--) {
				loopBuffer.write(byteListPattern.get(i));
				byteListPattern.remove(i);
			}
			loopBuffer.flush();
			loopBuffer.close();

			byteListPattern = null;

			return loopBuffer.toByteArray();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Serialization : readEbooksPattern[" + e.getMessage() + "]");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.e(TAG, "Serialization : readEbooksPattern[" + e.getMessage() + "]");
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "Serialization : readEbooksPattern[" + e.getMessage() + "]");
			e.printStackTrace();
			return null;
		}
	}
}