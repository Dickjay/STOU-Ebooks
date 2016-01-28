package com.porar.ebooks.model;

import java.io.Serializable;

/**
 * Created by Porar on 10/12/2015.
 */
public class ModelSubjectBookID implements Serializable {
    private String bCODE = "";

    ModelSubjectBookID(String bCODE) {
        this.bCODE = bCODE;
    }

    public String getbCODE() {
        return bCODE;
    }

    public void setbCODE(String bCODE) {
        this.bCODE = bCODE;
    }
}
