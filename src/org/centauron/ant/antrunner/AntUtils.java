package org.centauron.ant.antrunner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.launch.AntMain;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.util.LoaderUtils;
import org.centauron.utility.Utility;


public class AntUtils {
	public static ClassLoader mainClassLoader;
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
		//START GENERATING A WHOLE NEW PROCESS
		Thread t=new Thread() {
			public void run() {
				try {		
					String[] args=Launcher.startArgs;
					args=AntUtils.removeNonAntArgs(args);
					args=Utility.addToArray(args,"-buildfile");
					args=Utility.addToArray(args,ar.getBuildFile().getAbsolutePath());					
			        args=Utility.addToArray(args, ar.getTargetName());
					String params=Utility.joinArray(args," ");	
					System.out.println("PARAMS:" +params);
					//LETS CHECK FOR ANT_HOME
					File antDir=AntUtils.guessAntDir();
					ar.informTargetStarted();
					File antBat=new File(antDir,"ant.bat");
					Process process = Runtime.getRuntime().exec(antBat.getAbsolutePath()+" "+params);
					StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(),true,ar);
					StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(),false,ar);
					errorGobbler.start();
					outputGobbler.start();
					int ext=process.waitFor();
					ar.informTargetFinished();
					if (ext==0) {
						ar.setLastResult(AntRunnerNode.RESULT_SUCCESS);
					} else {
						ar.setLastResult(AntRunnerNode.RESULT_FAILED);
					}
					System.out.println("RET:"+ext);
				} catch (Exception e) {
					e.printStackTrace();
					ar.informTargetFinished();
					ar.setLastResult(AntRunnerNode.RESULT_FAILED);
				}
			}
		};
		
		if (wait) {
			t.run();
		} else {
			t.start();
		}
	}
	/*
	public static void startForNode(final AntRunnerNode ar,boolean wait) {
		Thread t=new Thread() {
			public void run() {
				try {
					//LOAD ORIGINAL LAUNCHER VIA REFLECTION
					Launcher ls=new Launcher();
					//String[] args=ls.lastArgs;
					String args[]=new String[0];
					args=Utility.addToArray(args,"-buildfile");
					args=Utility.addToArray(args,ar.getBuildFile().getAbsolutePath());					
					AntRunner.logout("==================================================");
					AntRunner.logout("==================================================");
					AntRunner.logout("==================================================");
					ls.run(args, "org.apache.tools.ant.Main");
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
		t.setContextClassLoader(Thread.currentThread().getContextClassLoader());
		if (wait) {
			t.run();
		} else {
			t.start();
		}
	}	
	*/

	protected static File guessAntDir() throws Exception {
		//CHECK FOR ANTHOME
		String antHomeDir=System.getenv("ANT_HOME");
		if (antHomeDir==null) {
			File libDir=new File(Utility.getApplicationDirectory());
			return new File(libDir.getParentFile(),"bin");
		} else {
			File homeDir=new File(antHomeDir);
			return new File(homeDir,"bin");
		}
	}
	protected static String[] removeNonAntArgs(String[] args) {
		if (args==null) return null;
		for (int i=0;i<args.length;i++) {
			if (args[i].startsWith("-")) {
				//jump over the next argument
				++i;
			} else {
				//REMOVE THIS
				args=Utility.removeIndexFromArray(args,i);
				--i;
			}
		}
		return args;
	}
	public static ClassLoader newClassLoader(ClassLoader cl) {
		File file = new File("D:/java_apache_ant/lib/ant-contrib-1.0b3.jar");
		
		URLClassLoader urlClassLoader;
		try {
			urlClassLoader = new URLClassLoader(new URL[]{file.toURL()},cl);
			return urlClassLoader;	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;	
	}
	/*
	public static void startForNode(final AntRunnerNode ar,boolean wait) {
		Thread t=new Thread() {
			public void run() {
				try {
					
					Thread.currentThread().setContextClassLoader(mainClassLoader);
					Project project=new Project();
					project.init();
					project.setSystemProperties();
					project.addBuildListener(ar.getAntRunner().console);
					project.addBuildListener(ar);
					ProjectHelper.configureProject(project, ar.getBuildFile());
					
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
		t.setContextClassLoader(Thread.currentThread().getContextClassLoader());
		if (wait) {
			t.run();
		} else {
			t.start();
		}
	}
	*/
	
	/*
	public static void checkForLimit() throws Exception {
		//TODO 30.06.2012
		InputStream is=	AntUtils.class.getClassLoader().getResourceAsStream("/net/sf/antcontrib/process/Limit.class");
		if (is==null) {
			throw new Exception("FEHLER");
		}
	}
	*/
	
	
}
