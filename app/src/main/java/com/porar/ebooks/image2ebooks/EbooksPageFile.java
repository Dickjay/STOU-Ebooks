package com.porar.ebooks.image2ebooks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.porar.ebooks.event.OnCompleteListener;
import com.porar.ebooks.stou.Serialization;
import com.porar.ebooks.stou.objectCompleteListener;
import com.porar.ebooks.utils.AsyncTask_EbooksPageResult;

public class EbooksPageFile implements Serializable{
	
	//Don't make it to abstract Because not support to serializable.
	
	private static final long serialVersionUID = 1L;
	private transient static String TAG = "Ebooks.in.th";
	private transient static int bufferPattern = (Serialization.bufferPattern);
	
	private int bookId = 0;
	private String bookName = "";
	private byte[] raw;
	private int rawLength = 0;
	private int ownerId = 0;
	private String ownerEmail = "";
	private int currentPage = 0;
	private final Date createDate;
	private String deviceId = "";
	
	private transient InputStream is;
	private transient int readSize = 0;
    private transient int readedSize = 0;
    private transient int bufferSize = (8*1024);
    private transient byte[] buffer = new byte[bufferSize];
    private transient List<byte[]> arraysList = new ArrayList<byte[]>();
    private transient String savePath = "";
    private transient String thumbPath = "";
    private transient static List<OnCompleteListener> _listeners = new ArrayList<OnCompleteListener>();
    private transient AsyncTask_EbooksPageResult asyncResult;
    
    public synchronized void addEventListener(OnCompleteListener listener){
      _listeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnCompleteListener listener){
      _listeners.remove(listener);
    }
    
    public synchronized void throwComplete(){
    	//OnCompleteListener event = new OnCompleteListener(this);
    	Iterator<OnCompleteListener> i = _listeners.iterator();
    	while(i.hasNext())  {
    		i.next().handleCompleteEvent(new objectCompleteListener(this.asyncResult));
    	}
    }
    
	public EbooksPageFile(InputStream inputStream,String bookName,int bookId,int page,String savePath,String thumbnailPath){
		this.bookId = bookId;
		this.bookName = bookName;
		this.ownerId = 1;
		this.ownerEmail = "narongsak@porar.com";
		this.currentPage = page;
		this.createDate = new Date();
		this.deviceId = "0000000000";
		this.is = inputStream;
		this.savePath = savePath;
		this.thumbPath = thumbnailPath;
		thumbnailPath = this.thumbPath;//Remove Waring Unuse
		this.asyncResult = new AsyncTask_EbooksPageResult(bookId,bookName,page,savePath);
		
		File checkFile = new File(savePath);
		if(!checkFile.exists()){
			this.run();
		}
	}
	
	private void run(){
		try {
			while ((readSize = is.read(buffer, 0, bufferSize)) != -1){
				byte[] rawBytes = new byte[readSize];
				System.arraycopy(buffer,0,rawBytes,0,readSize);
				arraysList.add(rawBytes);
				readedSize += readSize;
			}
			
			byte[] mergedArray = new byte[readedSize];
		    int start = 0;
		    
		    for(int i=0;i<arraysList.size();i++){
		    	byte[] array = arraysList.get(i);
		    	if(array!=null){
		    		System.arraycopy(array, 0,mergedArray, start, array.length);
		    		arraysList.set(i, null);
		    		start += array.length;
		    	}
		    }
		    //is.close();//Don't close because zip in use
		    //arraysList = null;
		    
		    this.setRawBitmap(mergedArray);
		    this.setRawBitmapLength(mergedArray.length);
		    Log.d(TAG,"Raw Length : " + mergedArray.length);
			//--------------------------------------------------------------------
			int sampleSize = 1;
			if(this.getRawBitmapLength() > 819200){//800KB
				sampleSize = 2;
			}
		    boolean enableSave = false;
			boolean indexOver = false;
			Bitmap newBitmap = null;

			while(enableSave==false){
				if(sampleSize>16){
					indexOver = true;
					Log.e(TAG,"Index Over");
					Log.d(TAG, "Reduce Ratio");
					break;
				}
				//---------------------------------------------------------------
				if(indexOver){
					try{
						//Reduce by Ratio
					} catch(OutOfMemoryError e){
						Log.e(TAG, "Request Size : " + sampleSize);
						continue;
					}
				}else{
					try {
						Options opts = new Options();
						opts.inSampleSize = sampleSize;
						Log.d(TAG,"Sample Size : " + sampleSize);
						newBitmap = BitmapFactory.decodeByteArray(mergedArray, 0, this.getRawBitmapLength(),opts);
					} catch(OutOfMemoryError e){
						Log.e(TAG, "OutOfMemory : Can't Serialize File");
						sampleSize++;
						e.printStackTrace();
						continue;
					}
				}
				//---------------------------------------------------------------
				ByteArrayOutputStream bos = null;
				try{
					bos = new ByteArrayOutputStream();
					newBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
					this.setRawBitmap(bos.toByteArray());
					this.setRawBitmapLength(bos.toByteArray().length);
					bos = null;
					newBitmap = null;
				} catch(OutOfMemoryError e){
					Log.e(TAG, "Compress : OutOfMemoryError");
					sampleSize++;
					e.printStackTrace();
					continue;
				} catch(NullPointerException e){
					Log.e(TAG, "Compress : NullPointerException");
					sampleSize++;
					e.printStackTrace();
					continue;
				}
				//---------------------------------------------------------------
				try {
					bos = new ByteArrayOutputStream();
					ObjectOutputStream objOut = new ObjectOutputStream(bos);
					objOut.writeObject(this);
					objOut.close();
					Log.e(TAG,"Enable to Save File");
				} catch (IOException e) {
					Log.e(TAG, "WriteObject : IOException["+e.getMessage()+"]");
					e.printStackTrace();
					continue;
				} catch (OutOfMemoryError e){
					Log.e(TAG, "WriteObject : OutOfMemoryError");
					sampleSize++;
					e.printStackTrace();
					continue;
				}
				//---------------------------------------------------------------
				try {
					byte[] bytes = bos.toByteArray();
					FileOutputStream out = new FileOutputStream(savePath);
					int bytesLength = bytes.length;
					int diffLength = 0;
					while(bytesLength > 0){
						diffLength = bytesLength - bufferPattern;
						if(diffLength < bufferPattern){
							out.write(bytes, diffLength, bufferPattern);//
							out.write(bytes, 0, diffLength);
							break;
						}else{
							out.write(bytes, diffLength, bufferPattern);
						}
						bytesLength -= bufferPattern;
						if(bytesLength < 0){
							break;
						}
					}
					out.flush();
					out.close();
					enableSave = true;
				} catch (FileNotFoundException e) {
					Log.e(TAG, "EbooksPageFile : FileNotFoundException["+e.getMessage()+"]");
					e.printStackTrace();
					break;
				} catch (IOException e) {
					Log.e(TAG, "EbooksPageFile : IOException["+e.getMessage()+"]");
					e.printStackTrace();
					break;
				} catch (OutOfMemoryError e){
					Log.e(TAG, "EbooksPageFile : OutOfMemoryError");
					sampleSize++;
					e.printStackTrace();
					continue;
				}
			}
			//--------------------------------------------------------------------
			if(enableSave){
				mergedArray = null;
			    throwComplete();
			}
		}catch (IOException e) {
			Log.e("Ebooks.in.th", "EbooksPageFile - IOException[" + e.getMessage() + "]");
			e.printStackTrace();
		}
	}

	public String getBookName() {
		return bookName;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public int getOwnerId() {
		return ownerId;
	}
	
	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setRawBitmap(byte[] bm){
		this.raw = bm;
	}
	
	public byte[] getRawBitmap(){
		return raw;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public String getSavePath() {
		return savePath;
	}

	public void setRawBitmapLength(int rawBitmapLength) {
		this.rawLength = rawBitmapLength;
	}

	public int getRawBitmapLength() {
		return rawLength;
	}
}
