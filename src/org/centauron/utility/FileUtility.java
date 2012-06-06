package org.centauron.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {
    private static final boolean ON_NETWARE = Os.isFamily("netware");
    private static final boolean ON_DOS = Os.isFamily("dos");
    private static final boolean ON_WIN9X = Os.isFamily("win9x");
    private static final boolean ON_WINDOWS = Os.isFamily("windows");	
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
	public static boolean sameFile(File file1, File file2) {
		if (file1==null && file2==null) return true;
		if (file1==null) return false;
		if (file2==null) return false;
		File f1=file1.getAbsoluteFile();
		File f2=file2.getAbsoluteFile();
		return f1.equals(f2);
	}
    public static boolean isAbsolutePath(String filename) {
        int len = filename.length();
        if (len == 0) {
            return false;
        }
        char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        char c = filename.charAt(0);
        if (!(ON_DOS || ON_NETWARE)) {
            return (c == sep);
        }
        if (c == sep) {
            // CheckStyle:MagicNumber OFF
            if (!(ON_DOS && len > 4 && filename.charAt(1) == sep)) {
                return false;
            }
            // CheckStyle:MagicNumber ON
            int nextsep = filename.indexOf(sep, 2);
            return nextsep > 2 && nextsep + 1 < len;
        }
        int colon = filename.indexOf(':');
        return (Character.isLetter(c) && colon == 1
                && filename.length() > 2 && filename.charAt(2) == sep)
                || (ON_NETWARE && colon > 0);
    }

}
