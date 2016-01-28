package com.porar.ebooks.stou;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter_Cover implements FilenameFilter {
	public boolean accept(File dir, String filename) {
		if (filename.toLowerCase().startsWith("cover_")) {
			return true;
		}
		return false;
	}
}
