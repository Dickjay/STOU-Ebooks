package com.porar.ebooks.stou;

import java.io.Serializable;
import java.util.Date;

public class EbooksHashFile implements Serializable {
	
	private long HashBytes = 0;
	private String Username = "";
	private int BID = 0;
	private int CID = 0;
	private Date SaveDate = null;
	private String Extension;
	private String File;
	
	private static final long serialVersionUID = -8622852545445152838L;
	
	public long getHashBytes() {
		return HashBytes;
	}

	public void setHashBytes(int hashBytes) {
		HashBytes = hashBytes;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public int getBID() {
		return BID;
	}

	public void setBID(int bID) {
		BID = bID;
	}

	public int getCID() {
		return CID;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public Date getSaveDate() {
		return SaveDate;
	}

	public void setSaveDate(Date saveDate) {
		SaveDate = saveDate;
	}

	public String getExtension() {
		return Extension;
	}

	public void setExtension(String extension) {
		Extension = extension;
	}

	public String getFile() {
		return File;
	}

	public void setFile(String file) {
		File = file;
	}
	
	@Override
	public String toString(){
		return this.HashBytes + "";
	}
}
