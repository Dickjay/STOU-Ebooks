package com.porar.ebooks.model;

import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.utils.StaticUtils;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

@SuppressWarnings("serial")
public class Model_EBook_Shelf_List implements Serializable, Comparable<Model_EBook_Shelf_List> {

    private final int BID;
    private final String Publisher;
    private final String Name;
    private final String Cover;
    private final String Extension;
    private final String Price;
    private final int Pages;
    private final String FileSize;
    private final String File;
    private int Pointer;
    private final String FreeFlag;
    private StatusFlag StatusFlag;
    private final boolean SourceUpdateFlag;
    private boolean ReadingFlag;

    private final boolean PrintEnable;

    public static enum StatusFlag {
        New, Favourite, Ordinary
    };

    public Model_EBook_Shelf_List(PListObject plistObject) {
        Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();
        this.BID = Integer.parseInt(map.get("BID").toString());
        this.Publisher = map.get("Publisher").toString();
        this.Name = map.get("Name").toString();
        this.Cover = map.get("Cover").toString();
        this.Extension = map.get("Extension").toString();
        this.Price = map.get("Price").toString();
        this.Pages = Integer.parseInt(map.get("Pages").toString());
        this.FileSize = map.get("FileSize").toString();
        this.File = map.get("File").toString();
        this.Pointer = Integer.parseInt(map.get("Pointer").toString());
        this.FreeFlag = map.get("FreeFlag").toString();
        this.setStatusFlag(map.get("StatusFlag").toString());
        this.SourceUpdateFlag = Boolean.parseBoolean(map.get("SourceUpdateFlag").toString());
        this.ReadingFlag = Boolean.parseBoolean(map.get("ReadingFlag").toString());
        this.PrintEnable = Boolean.parseBoolean(map.get("PrintEnable").toString());


    }

    public int getBID() {
        return this.BID;
    }

    public String getName() {
        return this.Name;
    }

    public String getPublisher() {
        return this.Publisher;
    }

    public String getCover() {

        return StaticUtils.escapeHTML(Cover);

    }

    public String getCoverURL() {
        String result = URLEncoder.encode(Cover);
        result = result.replaceAll("\\+", "%20");
        return AppMain.COVER_URL + BID + "/" + result;
    }

    public String getExtension() {
        return this.Extension;
    }

    public String getPrice() {
        return this.Price;
    }

    public int getPages() {
        return this.Pages;
    }

    public String getFileSize() {
        return this.FileSize;
    }

    public String getFile() {
        return this.File;
    }

    public int getPointer() {
        return this.Pointer;
    }

    public void setPointer(int pageIndex) {
        this.Pointer = pageIndex;
    }

    public String getFreeFlag() {
        return this.FreeFlag;
    }

    public void setStatusFlag(StatusFlag status) {
        this.StatusFlag = status;
    }

    @SuppressWarnings("static-access")
    private void setStatusFlag(String status) {
        // Default
        this.StatusFlag = StatusFlag.Ordinary;

        if (status.equals("O")) {
            this.StatusFlag = StatusFlag.Ordinary;
        }
        if (status.equals("N")) {
            this.StatusFlag = StatusFlag.New;
        }
        if (status.equals("F")) {
            this.StatusFlag = StatusFlag.Favourite;
        }
    }

    public StatusFlag getStatusFlag() {
        return this.StatusFlag;
    }

    public boolean getSourceUpdateFlag() {
        return this.SourceUpdateFlag;
    }

    public boolean getReadingFlag() {
        return this.ReadingFlag;
    }

    public void setReadingFlag(boolean reading) {
        this.ReadingFlag = reading;
    }

    public boolean getPrintEnable() {
        return this.PrintEnable;
    }

    @Override
    public String toString() {
        return this.BID + "";
    }

    @Override
    public int compareTo(Model_EBook_Shelf_List another) {
        if (another.getBID() == this.BID) {
            return 0;
        }
        if (another.getBID() > this.BID) {
            return 1;
        }
        if (another.getBID() < this.BID) {
            return -1;
        }
        return 0;
    }

}
