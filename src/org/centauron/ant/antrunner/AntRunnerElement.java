package org.centauron.ant.antrunner;

import javax.swing.Icon;

public class AntRunnerElement {
	public String dir;
	public String buildfile;
	public String targetname;
	public String caption;
	public String getShortCaption() {
		if (caption==null) return "butt";
		return caption;
	}
}
