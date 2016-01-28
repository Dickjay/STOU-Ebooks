package com.porar.ebooks.stou;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class EbooksHeaderFile implements Serializable{
	private String Username = "";
	private int BID = 0;
	private int CID = 0;
	private String Name = "";
	private String Publisher = "0";
	private String Price = null;
	private Date SaveDate = null;
	private String Cover;
	private String Extension;
	private int Pages;
	private int LastPages;
	private String FileSize;
	private String File;
	private boolean Favourite;
	private String StatusFlag;
	private  String  FirstName;
	private  String LastName ;
	private  String Email ;
	private  String Comments ; 
	private  String eBooks ;
	private  String Favorites ;
	private  String Total ;
	
	
	public EbooksHeaderFile(){
		System.out.println("Ebooks.in.th - Prepare Ebooks File");
	}
	
	@Override
	public String toString() {
		return "Ebooks.in.th - Download by - "+Username+" [ID - "+this.BID+":Date - "+this.SaveDate+"], Read Page : " + this.LastPages;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setPublisher(String publisher) {
		Publisher = publisher;
	}
	
	public String getPublisher() {
		return Publisher;
	}
	
	public void setPrice(String price) {
		Price = price;
	}
	
	public String getPrice() {
		return Price;
	}
	
	public Date getSaveDate() {
		return SaveDate;
	}
	
	public void setSaveDate(Date saveDate) {
		SaveDate = saveDate;
	}
	
	public void setCover(String cover) {
		Cover = cover;
	}
	
	public String getCover() {
		return Cover;
	}
	
	public void setExtension(String extension) {
		Extension = extension;
	}
	
	public String getExtension() {
		return Extension;
	}
	
	public void setPages(int pages) {
		Pages = pages;
	}
	
	public int getPages() {
		return Pages;
	}
	
	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}
	
	public String getFileSize() {
		return FileSize;
	}
	
	public void setFile(String file) {
		File = file;
	}
	
	public String getFile() {
		return File;
	}
	
	public void setFavourite(boolean favourite) {
		Favourite = favourite;
	}
	
	public boolean isFavourite() {
		return Favourite;
	}
	
	public Integer getBID() {
		return BID;
	}
	
	public void setBID(Integer bID) {
		BID = bID;
	}
	
	public String getUsername() {
		return Username;
	}
	
	public void setUsername(String username) {
		Username = username;
	}

	public void setLastPages(int lastPages) {
		LastPages = lastPages;
	}

	public int getLastPages() {
		return LastPages;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public int getCID() {
		return CID;
	}

	public String getStatusFlag() {
		return StatusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		StatusFlag = statusFlag;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public String geteBooks() {
		return eBooks;
	}

	public void seteBooks(String eBooks) {
		this.eBooks = eBooks;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}

	public String getFavorites() {
		return Favorites;
	}

	public void setFavorites(String favorites) {
		Favorites = favorites;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
}