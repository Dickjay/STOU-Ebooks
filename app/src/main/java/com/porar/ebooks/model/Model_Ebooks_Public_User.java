package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/6/2015.
 */
public class Model_Ebooks_Public_User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int BID = 0;
    private String BCode = "";
    private String CatID = "";
    private String MCatID = "";
    private String BookName = "";
    private String BookCover = "";
    private String BookCoverThumbnail = "";
    private String BookPrice = "";
    private int BookRate = 0;
    private String BookPublisher = "";

    public Model_Ebooks_Public_User(PListObject plistObject) {
        Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();

        try{
            this.BID = Integer.parseInt(map.get("BID").toString());
        }catch (NumberFormatException e){
            this.BID =0;
        }
        this.BCode = map.get("BCode").toString();
        this.CatID = map.get("CatID").toString();
        this.MCatID = map.get("MCatID").toString();
        this.BookName = map.get("BookName").toString();
        this.BookCover = map.get("BookCover").toString();
        this.BookCoverThumbnail = map.get("BookCoverThumbnail").toString();
        try{
            this.BookPrice = map.get("BookPrice").toString();
        }catch (NumberFormatException e){
            this.BookPrice ="";
        }

        try{
            this.BookRate = Integer.parseInt(map.get("BookRate").toString());
        }catch (NumberFormatException e){
            this.BookRate =0;
        }
        try{
            this.BookPublisher = map.get("BookPublisher").toString();
        }catch (NullPointerException e){
           this.BookPublisher = "";
        }


    }

    public String getBookPublisher() {
        return BookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        BookPublisher = bookPublisher;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getBID() {
        return BID;
    }

    public void setBID(int BID) {
        this.BID = BID;
    }

    public String getBCode() {
        return BCode;
    }

    public void setBCode(String BCode) {
        this.BCode = BCode;
    }

    public String getCatID() {
        return CatID;
    }

    public void setCatID(String catID) {
        CatID = catID;
    }

    public String getMCatID() {
        return MCatID;
    }

    public void setMCatID(String MCatID) {
        this.MCatID = MCatID;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getBookCover() {
        return BookCover;
    }

    public void setBookCover(String bookCover) {
        BookCover = bookCover;
    }

    public String getBookCoverThumbnail() {
        return BookCoverThumbnail;
    }

    public void setBookCoverThumbnail(String bookCoverThumbnail) {
        BookCoverThumbnail = bookCoverThumbnail;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }

    public int getBookRate() {
        return BookRate;
    }

    public void setBookRate(int bookRate) {
        BookRate = bookRate;
    }
}
