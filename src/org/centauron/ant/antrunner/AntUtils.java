package org.centauron.ant.antrunner;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.launch.AntMain;


public class AntUtils {
	public static BuildFileInfo getBuildFileInfo(File file) throws Exception {
		BuildFileInfo info=new BuildFileInfo();
		Project project= new Project();		
		project.init();
		ProjectHelper helper = new ProjectHelper2();
		project.addReference("ant.projectHelper", helper);
        helper.parse( project, file);  	
        info.defaultTarget= project.getDefaultTarget();
        info.name=project.getName();
        for (Object s:project.getTargets().keySet()) {
        	String ss=(String)s;
        	if (ss.length()!=0) {
        		info.targets.add(ss);
        	}
        }
        return info;
	}

	public static void startForNode(final AntRunnerNode ar,boolean wait) {
		Thread t=new Thread() {
			public void run() {
				try {
					Project project= new Project();		
					project.init();
					ProjectHelper helper = new ProjectHelper2();
					project.addReference("ant.projectHelper", helper);
					project.addBuildListener(ar.getAntRunner().console);
					project.addBuildListener(ar);
					helper.parse( project, ar.getBuildFile());  
			        project.executeTarget(ar.getTargetName());
			        ar.setLastResult(AntRunnerNode.RESULT_SUCCESS);
			        ar.getAntRunner().console.setMessage("BUILD SUCCESSFUL.");
				} catch (Exception e) {
					e.printStackTrace();
					ar.setLastResult(AntRunnerNode.RESULT_FAILED);
					StringWriter buf=new StringWriter();
					PrintWriter pr=new PrintWriter(buf);
					e.printStackTrace(pr);
					ar.getAntRunner().console.setMessage(buf.toString());
					
				}
		        
			}
		};
		if (wait) {
			t.run();
		} else {
			t.start();
		}
	}
}
