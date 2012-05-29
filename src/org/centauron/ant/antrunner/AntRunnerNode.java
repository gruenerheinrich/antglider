package org.centauron.ant.antrunner;

import java.awt.Color;
import java.io.File;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.centauron.utility.Utility;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class AntRunnerNode extends DefaultMutableTreeNode implements BuildListener {
	public final static int MODE_STANDALONEBUILDFILE=0;
	public static final int MODE_BUILDFILE = 1;
	public final static int MODE_TARGET=2;
	public final static int MODE_DEFAULTTARGET=3;
	public static final int MODE_STANDALONETARGET = 4;
	public static final int MODE_STANDALONEDIR = 5;
	public static final int MODE_BATCHTARGET = 6;
	public static final int MODE_SHORTCUTTARGET = 7;
	public static final int MODE_DIR = 6;
	public final static int RESULT_SUCCESS=1;
	public final static int RESULT_FAILED=2;
	
	
	
	
	
	
	private int m_mode;
	private AntRunner antrunner;
	private boolean m_running;
	private Date m_starttime;
	private File m_buildfile;
	private String m_targetname;
	private int m_ping;
	private int m_lastresult;
	private String m_name;
	private Object notifyParent;
	private File m_dir;
	public AntRunnerNode(AntRunner main,Object n,String name,int mode) {
		super(name);
		m_name=name;
		antrunner=main;
		notifyParent=n;
		m_mode=mode;
	}
	public int getMode() {
		// TODO Auto-generated method stub
		return m_mode;
	}
	

	public void selectNode() throws Exception {
		//ARM THE ACTIONS
		//this.antrunnerpanel.antrunner.getFactory().getActionForName("BatchAddSelectedAction").setEnabled(this.isBatchable());

		this.antrunner.getFactory().getActionForName("RunAction").setEnabled(!this.isRunning());
		this.antrunner.getFactory().getActionForName("RunDefaultAction").setEnabled(!this.isRunning());
		this.antrunner.getFactory().getActionForName("HaltAction").setEnabled(!this.isRunning());
		
	}
	
	private boolean isBatchable() {
		return (this.getMode()==AntRunnerNode.MODE_TARGET || this.getMode()==AntRunnerNode.MODE_DEFAULTTARGET || this.getMode()==AntRunnerNode.MODE_STANDALONETARGET);
	}
	public double getAverageDuration() {
		//ask statistics
		return 10;
	}
	public double getCurrentDuration() {
		double d=((new Date().getTime() - m_starttime.getTime()));
		return d/1000;
	}
	public void setMode(int mode) {
		m_mode=mode;
	}
	public JPopupMenu getPopupMenu() throws Exception {
		JPopupMenu pop=new JPopupMenu();
		if (m_mode==AntRunnerNode.MODE_STANDALONEBUILDFILE) {
			pop.add(this.antrunner.getFactory().getActionForName("RunDefaultAction"));
			pop.add(this.antrunner.getFactory().getActionForName("RefreshFileAction"));
			pop.addSeparator();
			pop.add(this.antrunner.getFactory().getActionForName("RemoveNodeAction"));
		}
		if (m_mode==AntRunnerNode.MODE_BUILDFILE) {
			pop.add(this.antrunner.getFactory().getActionForName("RunDefaultAction"));
			pop.add(this.antrunner.getFactory().getActionForName("RefreshFileAction"));
		}		
		if (m_mode==AntRunnerNode.MODE_TARGET || m_mode==AntRunnerNode.MODE_DEFAULTTARGET) {
			pop.add(this.antrunner.getFactory().getActionForName("RunAction"));
			pop.add(this.antrunner.getFactory().getActionForName("BatchAddSelectedAction"));
			eventuallyAddShortCut(pop);
			pop.add(this.antrunner.getFactory().getActionForName("LoadStatisticsAction"));			
		}
		if (m_mode==AntRunnerNode.MODE_STANDALONETARGET) {
			pop.add(this.antrunner.getFactory().getActionForName("RunAction"));
			//TODO ADD TO SHORTCUT
			pop.add(this.antrunner.getFactory().getActionForName("BatchAddSelectedAction"));
			eventuallyAddShortCut(pop);
			pop.add(this.antrunner.getFactory().getActionForName("LoadStatisticsAction"));			
			pop.addSeparator();
			pop.add(this.antrunner.getFactory().getActionForName("RemoveNodeAction"));
		}
		if (m_mode==AntRunnerNode.MODE_STANDALONEDIR) {
			//pop.add(this.antrunnerpanel.antrunner.getFactory().getActionForName("RunAction"));
			pop.addSeparator();
			pop.add(this.antrunner.getFactory().getActionForName("RemoveNodeAction"));			
		}
		return pop;
	}
	private void eventuallyAddShortCut(JPopupMenu pop) throws Exception {
		if (this.antrunner.isShortCutEnabled()) {
			if (this.antrunner.isShortCut(this)) {
				pop.add(this.antrunner.getFactory().getActionForName("shortcut.RemoveShortCutAction"));					
			} else {
				pop.add(this.antrunner.getFactory().getActionForName("shortcut.AddShortCutAction"));
			}
		}
	}
	@Override
	public void buildFinished(BuildEvent arg0) {

	}
	@Override
	public void buildStarted(BuildEvent arg0) {

	}
	@Override
	public void messageLogged(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void targetFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		m_running=false;	
		//NOTIFY THE TREE
		//this.setLastResult(RESULT_SUCCESS);
	}
	@Override
	public void targetStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		m_running=true;
		m_starttime=new Date();
		startUpdaterThread();
		//NOTIFY THE TREE
		//this.setLastResult(0);
	}
	private void startUpdaterThread() {
			final AntRunnerNode node=this;
			Thread t=new Thread() {
				public void run() {
					try {
						while (m_running) {
							node.doPing();							
							Thread.sleep(50);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
	
	}
	
	public int getCurrentPercent() {
		if (isInOverTime()) {
			return m_ping;			
		} else {
			return (int)(100*this.getCurrentDuration()/this.getExpectedDuration());
		}
	}
	public boolean isInOverTime() {
		return (this.getCurrentDuration()>this.getExpectedDuration());
	}
	private double getExpectedDuration() {
		return this.getAverageDuration();
	}
	public void doPing() {
		if (this.isInOverTime()) {
			++m_ping;
			if (m_ping>=100) {
				m_ping=0;
			}
		}
		this.doNotification();
	}
	@Override
	public void taskFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void taskStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public File getBuildFile() {
		return m_buildfile;
	}
	public String getTargetName() {
		return m_targetname;
	}
	public void setBuildFile(File bn) {
		m_buildfile=bn;
	}
	public void setTargetName(String tn) {
		m_targetname=tn;
	}
	public boolean isRunning() {
		return m_running;
	}
	public int getLastResult() {
		return m_lastresult;
	}
	public void setLastResult(int result) {
		m_lastresult=result;
		this.doNotification();
		
	}
	
	private void doNotification() {
		if (notifyParent instanceof AntRunnerPanel) {
			DefaultTreeModel mod=(DefaultTreeModel)((AntRunnerPanel)notifyParent).tree.getModel();
			mod.nodeChanged(this);
		}
		if (notifyParent instanceof AntRunnerBatchView) {
			DefaultListModel mod=((AntRunnerBatchView)notifyParent).getListModel();
			int idx=Utility.getListModelIndex(mod,this);
			if (idx!=-1) {
				mod.setElementAt(this, idx);
			}
		}		
	}
	public AntRunner getAntRunner() {
		// TODO Auto-generated method stub
		return this.antrunner;
	}
	
	public Object clone() {
		AntRunnerNode ar=new AntRunnerNode(antrunner,notifyParent,(String)this.userObject,m_mode);
		ar.setBuildFile(this.m_buildfile);
		ar.setTargetName(this.m_targetname);
		return ar;
	}
	public String getCaption() {
		return m_name;
	}
	public void setNotifier(Object not) {
		notifyParent=not;
	}
	public Element createXMLNode(File baseDir,Document doc) {
		if (!this.canCreateXMLNode()) return null;
		Element node=doc.createElement(AntRunnerNode.getXMLTagName(getMode()));
		if (getMode()==AntRunnerNode.MODE_STANDALONEBUILDFILE)  {
			node.setTextContent(this.antrunner.getOptionDrivenFileLocation(this.getBuildFile(),baseDir));
		}
		if (getMode()==AntRunnerNode.MODE_STANDALONETARGET)  {
			node.setTextContent(this.getTargetName());
			node.setAttribute("buildfile", this.antrunner.getOptionDrivenFileLocation(this.getBuildFile(),baseDir));
		}
		if (getMode()==AntRunnerNode.MODE_STANDALONEDIR) {
			node.setTextContent(this.antrunner.getOptionDrivenFileLocation(this.getDir(),baseDir));
		}
		return node;
	}
	public static String getXMLTagName(int mode) {
		if (mode==AntRunnerNode.MODE_STANDALONEBUILDFILE) {
			return "buildfile";
		}
		if (mode==AntRunnerNode.MODE_STANDALONEDIR) {
			return "dir";
		}
		if (mode==AntRunnerNode.MODE_STANDALONETARGET) {
			return "target";
		}		
		return null;
	}
	private boolean canCreateXMLNode() {
		if (getMode()==AntRunnerNode.MODE_STANDALONEBUILDFILE || getMode()==AntRunnerNode.MODE_STANDALONEDIR || getMode()==AntRunnerNode.MODE_STANDALONETARGET) {
			return true;
		}
		return false;
	}
	public void setDir(File dir) {
		m_dir=dir;
	}
	public File getDir() {
		return m_dir;
	}
	public boolean isDeleteable() {
		//ALL NODES THAT CAN BE SAVED
		return this.canCreateXMLNode();
	}
	public boolean isStartable() {
		if (getMode()==AntRunnerNode.MODE_STANDALONEBUILDFILE || getMode()==AntRunnerNode.MODE_BUILDFILE || getMode()==AntRunnerNode.MODE_STANDALONETARGET || getMode()==AntRunnerNode.MODE_TARGET || getMode()==AntRunnerNode.MODE_DEFAULTTARGET) {
			return true;
		}
		return false;
	}
	public boolean isBatchAddable() {
		return(getMode()==AntRunnerNode.MODE_STANDALONETARGET || getMode()==AntRunnerNode.MODE_TARGET);
	}
}
