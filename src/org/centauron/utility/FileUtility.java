package org.centauron.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {
	private static List getPathList(File f) {
		List l = new ArrayList();
		File r;
		try {
			r = f.getCanonicalFile();
			while(r != null) {
				l.add(r.getName());
				r = r.getParentFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			l = null;
		}
		return l;
	}
	private static String matchPathLists(List r,List f) {
		int i;
		int j;
		String s;
		// start at the beginning of the lists
		// iterate while both lists are equal
		s = "";
		i = r.size()-1;
		j = f.size()-1;
		// first eliminate common root
		while((i >= 0)&&(j >= 0)&&(r.get(i).equals(f.get(j)))) {
			i--;
			j--;
		}
		// for each remaining level in the home path, add a ..
		for(;i>=0;i--) {
			s += ".." + File.separator;
		}
		// for each level in the file path, add the path
		for(;j>=1;j--) {
			s += f.get(j) + File.separator;
		}
		// file name
		s += f.get(j);
		return s;
	}
	public static String getRelativePath(File home,File f){
		File r;
		List homelist;
		List filelist;
		String s;
		homelist = getPathList(home);
		filelist = getPathList(f);
		s = matchPathLists(homelist,filelist);
		return s;
	}
	public static File getAbsolutePath(String fname, File dir) {
		return new File(dir,fname);
	}
	public static boolean isSubFile(File baseDir, File file) {
		//TODO 
		return false;
	}
}
