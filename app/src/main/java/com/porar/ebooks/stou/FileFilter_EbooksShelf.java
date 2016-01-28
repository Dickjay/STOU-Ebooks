package com.porar.ebooks.stou;

import java.io.File;
import java.io.FileFilter;

public class FileFilter_EbooksShelf implements FileFilter{
	//private final String[] okFileExtensions = new String[] {".porar"};
	
	public boolean accept(File pathname) {
		String suffix = ".porar";
        if( pathname.getName().toLowerCase().endsWith(suffix) ) {
            return true;
        }
        return false;
	}

}
