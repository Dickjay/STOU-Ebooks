package com.porar.ebooks.model;

import com.porar.ebooks.utils.MySharedPref;
import com.porar.ebooks.utils.StaticUtils;

import java.io.Serializable;
import java.util.Map;

import plist.type.Array;
import plist.type.Dict;
import plist.xml.PListObject;

@SuppressWarnings("serial")
public class Model_ShowCase_View implements Serializable {

    private String URL = "";

    public Model_ShowCase_View(PListObject plistObject) {
        Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();
        this.URL = map.get("URL").toString();
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
