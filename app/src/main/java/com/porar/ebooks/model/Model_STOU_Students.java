package com.porar.ebooks.model;

import android.util.Log;

import com.porar.ebooks.utils.MySharedPref;
import com.porar.ebooks.utils.StaticUtils;

import java.io.Serializable;
import java.util.Map;

import plist.type.Array;
import plist.type.Dict;
import plist.xml.PListObject;

@SuppressWarnings("serial")
public class Model_STOU_Students implements Serializable {

    private String CID = "";

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    private String FirstName = "";
    private String LastName = "";
    private int MasterDegree = 0;
    private int BechalorDegree = 0;
    private int IsTeacher = 0;
    private String Student_ID = "";
    private String SubjectID = "";
    private int Error = 0;


    public int getBechalorDegree() {
        return BechalorDegree;
    }

    public void setBechalorDegree(int bechalorDegree) {
        BechalorDegree = bechalorDegree;
    }

    public Model_STOU_Students(Array plistObject) {
        Map<java.lang.String, PListObject> map = null;
        if (plistObject != null) {
            try {
                map = ((Dict) plistObject.get(0)).getConfigMap();

            } catch (IndexOutOfBoundsException e) {
                plistObject = null;
            }
        }
        if (plistObject != null) {
            this.CID = map.get("CID").toString();
            this.FirstName = map.get("FirstName").toString();
            this.LastName = map.get("LastName").toString();
            this.MasterDegree = Integer.parseInt(map.get("MasterDegree").toString());
            try {
                this.BechalorDegree = Integer.parseInt(map.get("BachelorDegree").toString());

            } catch (NullPointerException e) {
                e.printStackTrace();
                this.BechalorDegree = 0;
            }
            try {
                this.IsTeacher = Integer.parseInt(map.get("IsTeacher").toString());

            } catch (NullPointerException e) {
                e.printStackTrace();
                this.IsTeacher = 0;
            }
            this.Student_ID = map.get("StudentID").toString();
            this.SubjectID = map.get("SubjectID").toString();
            this.Error = Integer.parseInt(map.get("Error").toString());

        }

    }

    public String getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(String subjectID) {
        SubjectID = subjectID;
    }

    public int getError() {
        return Error;
    }

    public void setError(int error) {
        Error = error;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String cID) {
        CID = cID;
    }

    public int getMasterDegree() {
        return MasterDegree;
    }

    public void setMasterDegree(int masterDegree) {
        MasterDegree = masterDegree;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    public int getIsTeacher() {
        return IsTeacher;
    }

    public void setIsTeacher(int isTeacher) {
        IsTeacher = isTeacher;
    }
}
