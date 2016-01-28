package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import plist.type.Array;
import plist.type.Dict;
import plist.xml.PListObject;

@SuppressWarnings("serial")
public class Model_Customer_Detail implements Serializable {

    private int CID = 0;
    private String FirstName = "";
    private String LastName = "";
    private String Email = "";
    private String PictureUrl = "";
    private int Comments = 0;
    private String eBooks = "";
    private int Favorites = 0;
    private String Total = "";

    private String FacebookId = "";
    private String Password = "";
    private Date loginDate = new Date();

    private String sso_password = null;
    private String sso_email = null;
    private String image_avatar = null;
    private String Tel = null;
    private String Address = null;
    private String SubjectID = "";

    public int getBechalorDegree() {
        return BechalorDegree;
    }

    public void setBechalorDegree(int bechalorDegree) {
        BechalorDegree = bechalorDegree;
    }

    private int BechalorDegree = 0;
    private int MasterDegree = 0;
    private int IsTeacher = 0;

    public Model_Customer_Detail(Array plistObject) {
        Map<java.lang.String, PListObject> map = null;
        if (plistObject != null) {
            try {
                map = ((Dict) plistObject.get(0)).getConfigMap();
            } catch (IndexOutOfBoundsException e) {
                plistObject = null;
            }
        }
        if (plistObject != null) {
            this.CID = Integer.parseInt(map.get("CID").toString());
            this.FirstName = map.get("FirstName").toString();
            this.LastName = map.get("LastName").toString();
            this.Email = map.get("Email").toString();
            this.PictureUrl = map.get("PictureUrl").toString();
            this.Comments = Integer.parseInt(map.get("Comments").toString());
            try {
                this.eBooks =map.get("eBooks").toString();
            } catch (NullPointerException e) {
                this.eBooks ="";
            }

            this.Favorites = Integer.parseInt(map.get("Favorites").toString());
            try {
                this.Total =map.get("Total").toString();
            } catch (NullPointerException e) {
                this.Total ="";
            }
            this.Tel = map.get("Tel").toString();
            this.Address = map.get("Address").toString();
            this.MasterDegree = Integer.parseInt(map.get("MasterDegree").toString());
            try {
                this.BechalorDegree = Integer.parseInt(map.get("BachelorDegree").toString());
            } catch (NullPointerException e) {
                this.BechalorDegree = 0;
            }
            try {
                this.IsTeacher = Integer.parseInt(map.get("IsTeacher").toString());
            } catch (NullPointerException e) {
                this.IsTeacher = 0;
            }
            try {
                this.SubjectID = map.get("SubjectID").toString();
            } catch (NullPointerException e) {
                e.printStackTrace();
                this.SubjectID = "0";
            }
            this.setLoginDate(new Date());
        }
    }

    public String getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(String subjectID) {
        SubjectID = subjectID;
    }

    public int getMasterDegree() {
        return MasterDegree;
    }

    public void setMasterDegree(int masterDegree) {
        MasterDegree = masterDegree;
    }

    public int getCID() {
        return this.CID;
    }

    public String getFirstName() {
        if (FirstName == "") {
            return "default";
        }
        return this.FirstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public String getEmail() {
        return this.Email;
    }

    public String getPictureUrl() {
        return this.PictureUrl;
    }

    public int getComments() {
        return this.Comments;
    }

    public String geteBooks() {
        return this.eBooks;
    }

    public void seteBooks(String eBooks) {
        this.eBooks = eBooks;
    }

    public int getFavorites() {
        return this.Favorites;
    }

    public void setFavorites(int favourite) {
        this.Favorites = favourite;
    }

    public String getTotal() {
        return this.Total;
    }

    public void setTotal(String total) {
        this.Total = total;
    }

    public String getFacebookId() {
        return FacebookId;
    }

    public void setFacebookId(String facebookId) {
        FacebookId = facebookId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getSso_email() {
        return sso_email;
    }

    public void setSso_email(String sso_email) {
        this.sso_email = sso_email;
    }

    public String getSso_password() {
        return sso_password;
    }

    public void setSso_password(String sso_password) {
        this.sso_password = sso_password;
    }

    public String getImage_avatar() {
        return image_avatar;
    }

    public void setImage_avatar(String image_avatar) {
        this.image_avatar = image_avatar;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return Tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        Tel = tel;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        Address = address;
    }

    public int getIsTeacher() {
        return IsTeacher;
    }

    public void setIsTeacher(int isTeacher) {
        IsTeacher = isTeacher;
    }
}
