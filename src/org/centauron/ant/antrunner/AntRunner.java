package org.centauron.ant.antrunner;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.tools.ant.launch.AntMain;
import org.centauron.ant.antrunner.actions.shortcut.ShortCutAction;
import org.centauron.utility.FileUtility;
import org.centauron.utility.PopupMenuAdapter;
import org.centauron.utility.StretchingFlowLayout;
import org.centauron.utility.Utility;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class AntRunner extends JFrame implements AntMain {
	public final static int SAVEMODUS_ALWAYSRELATIVE=0;
	public final static int SAVEMODUS_ALWAYSABSOLUTE=1;
	public final static int SAVEMODUS_ABSOLUTEWHENNONSUB=2;
	public final static int SAVEMODUS_ASKWHENNONSUB=3;
	public static final int SHORTCUTMODE_HIDE = 0;
	public static final int SHORTCUTMODE_DISPLAY = 1;
	public static final String applicatonName = "AntGlider";
	private static final boolean dologging=true;

	private File m_configfile;
	private JPanel shortcutpanel;
	private JPanel shortcutholder;
	private JMenuBar  themenubar;
	public JTabbedPane leftpanelHolder;
	private boolean leftpanelValidated;
	public JTabbedPane rightpanelHolder;
	private Properties myProperties;
	private AntRunnerActionFactory m_factory;
	private JPopupMenu panelpopup;
	public TitleAndIconSelection tabForm;
	private File m_lastfile;
	private JSplitPane mainpane;
	public AntRunnerConsole console;
	public AntRunnerBatchView batchview;
	private ButtonGroup consolegroup;
	private ButtonGroup batchgroup;
	private ButtonGroup shortgroup;
	private ButtonGroup saveoptiongroup;
	private double m_tempdivider;
	private int m_savemodus;
	private boolean m_configfilechanged;
	private int m_shortcutmode;
	private AntRunnerButton m_currentshortcut;
	public ClassLoader coreLoader;
	public AntRunner() throws Exception {
		//AntUtils.mainClassLoader=Thread.currentThread().getContextClassLoader();
		m_factory=new AntRunnerActionFactory(this);
		shortcutpanel=new JPanel(new StretchingFlowLayout(StretchingFlowLayout.LEFT,3,3));
		validateShortCutPanel();
		this.initMenuBars();
		this.setIconImage(AntRunner.getResourceImageIcon("ant.png").getImage());
		console=new AntRunnerConsole(this);
		batchview=new AntRunnerBatchView(this);
		
		leftpanelHolder=new JTabbedPane();
		leftpanelHolder.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					armPanelActions();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		PopupMenuAdapter.attachPopup(leftpanelHolder,panelpopup);
		validateLeftPanel();
		mainpane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainpane.add(leftpanelHolder);
		
		rightpanelHolder=new JTabbedPane();
		validateLeftPanel();
		mainpane.add(rightpanelHolder);
		
		JPanel pane=new JPanel(new BorderLayout());
		shortcutholder=new JPanel(new BorderLayout());
		shortcutholder.add("Center",shortcutpanel);
		pane.add("North",shortcutholder);
		this.setJMenuBar(themenubar);
		pane.add("Center",mainpane);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add("Center",pane);
		
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					m_factory.getActionForName("CloseAction").fire();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		this.armPanelActions();
		this.setDefaultLookAndFeelDecorated(true);
	}
	
	private void initMenuBars() throws Exception {
		; 
		themenubar=new JMenuBar();
		JMenu file=new JMenu("File");
		file.add(m_factory.getActionForName("OpenConfigFileAction"));
		file.add(m_factory.getActionForName("SaveConfigFileAction"));
		file.add(m_factory.getActionForName("SaveAsConfigFileAction"));
		file.addSeparator();
		file.add(m_factory.getActionForName("RefreshFileAction"));
		file.addSeparator();
		file.add(m_factory.getActionForName("OptionAction"));		
		file.addSeparator();		
		file.add(m_factory.getActionForName("CloseAction"));
		themenubar.add(file);
		
		JMenu modify=new JMenu("Modify");
		modify.add(m_factory.getActionForName("panel.AddPanelAction"));
		modify.add(m_factory.getActionForName("AddDirAction"));
		modify.add(m_factory.getActionForName("AddFileAction"));
		modify.add(m_factory.getActionForName("AddTargetAction"));
		themenubar.add(modify);
		
		JMenu view=new JMenu("View");
		
		JMenu consolemenu=new JMenu("Console");
		consolegroup = new ButtonGroup();
		JMenuItem item = new JRadioButtonMenuItem(m_factory.getActionForName("ConsoleHideAction"));
		consolegroup.add(item);
		consolemenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("ConsoleAttachAction"));
		consolegroup.add(item);
		consolemenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("ConsoleFrameAction"));
		consolegroup.add(item);
		consolemenu.add(item);
		view.add(consolemenu);

		JMenu batchmenu=new JMenu("Batch");
		batchgroup = new ButtonGroup();
		item = new JRadioButtonMenuItem(m_factory.getActionForName("BatchHideAction"));
		batchgroup.add(item);
		batchmenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("BatchAttachAction"));
		batchgroup.add(item);
		batchmenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("BatchFrameAction"));
		batchgroup.add(item);
		batchmenu.add(item);
		view.add(batchmenu);

		JMenu shortmenu=new JMenu("Shortcuts");
		shortgroup = new ButtonGroup();
		item = new JRadioButtonMenuItem(m_factory.getActionForName("ShortCutsHideAction"));
		shortgroup.add(item);
		shortmenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("ShortCutsAttachAction"));
		shortgroup.add(item);
		shortmenu.add(item);

		view.add(shortmenu);		

		
		themenubar.add(view);
		
		
		JMenu options=new JMenu("Options");
		JMenu saveoptionmenu=new JMenu("File Paths");
		saveoptiongroup = new ButtonGroup();
		item = new JRadioButtonMenuItem(m_factory.getActionForName("saveoption.AlwaysRelativeAction"));
		saveoptiongroup.add(item);
		saveoptionmenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("saveoption.AlwaysAbsoluteAction"));
		saveoptiongroup.add(item);
		saveoptionmenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("saveoption.AbsoluteWhenNonSubAction"));
		saveoptiongroup.add(item);
		saveoptionmenu.add(item);
		item = new JRadioButtonMenuItem(m_factory.getActionForName("saveoption.AskWhenNonSubAction"));
		saveoptiongroup.add(item);
		saveoptionmenu.add(item);

		options.add(saveoptionmenu);
		
		themenubar.add(options);
		
		
		
		panelpopup=new JPopupMenu();
		panelpopup.add(getFactory().getActionForName("panel.AddPanelAction"));
		panelpopup.add(getFactory().getActionForName("panel.EditPanelAction"));
		panelpopup.add(getFactory().getActionForName("panel.RemovePanelAction"));
		panelpopup.addSeparator();
		panelpopup.add(getFactory().getActionForName("panel.MoveBackwardPanelAction"));
		panelpopup.add(getFactory().getActionForName("panel.MoveForwardPanelAction"));
		
	}
	private void validateShortCutPanel() {
		//TODO 
	}
	public void validateLeftPanel() throws Exception {
		if (leftpanelValidated) {
			if (leftpanelHolder.getTabCount()==2) {
				leftpanelValidated=false;
				this.leftpanelHolder.removeTabAt(0);
			}
		} else {
			if (leftpanelHolder.getTabCount()==0) {
				this.addPanel("", "newpanel");
			}
			leftpanelValidated=true;
		}
		
		
	}
	private void unArmPanelActions() throws Exception {
		getFactory().getActionForName("panel.AddPanelAction").setEnabled(false);
		getFactory().getActionForName("panel.EditPanelAction").setEnabled(false);
		getFactory().getActionForName("panel.RemovePanelAction").setEnabled(false);
		getFactory().getActionForName("panel.MoveBackwardPanelAction").setEnabled(false);
		getFactory().getActionForName("panel.MoveForwardPanelAction").setEnabled(false);
		
	}
	private void armPanelActions() throws Exception {
		
		unArmPanelActions();
		int i=leftpanelHolder.getTabCount();
		int idx=leftpanelHolder.getSelectedIndex();
		getFactory().getActionForName("panel.AddPanelAction").setEnabled(true);
		if (idx==-1) return;
		getFactory().getActionForName("panel.EditPanelAction").setEnabled(true);
		getFactory().getActionForName("panel.RemovePanelAction").setEnabled(true);
		if (idx!=0)  {
			getFactory().getActionForName("panel.MoveBackwardPanelAction").setEnabled(true);
		}
		if (idx!=(i-1)) {
			getFactory().getActionForName("panel.MoveForwardPanelAction").setEnabled(true);
		}
	}	
	public void validateRightPanel() {
		// TODO Auto-generated method stub
		if (this.rightpanelHolder.getTabCount()==0) {
			if (Utility.isContainerChild(mainpane,rightpanelHolder)) {
				m_tempdivider=this.getDividerPercentage();
				mainpane.remove(rightpanelHolder);
			}
		} else {
			if (!Utility.isContainerChild(mainpane,rightpanelHolder)) {
				mainpane.add(rightpanelHolder);
				if ((m_tempdivider<=0) || (m_tempdivider>=1))  m_tempdivider=0.6;
				mainpane.setDividerLocation(m_tempdivider);
			}
		}
	}	


	
	public AntRunnerPanel addPanel(String iconname,String panelname) throws Exception {
		AntRunnerPanel pan=new AntRunnerPanel(this,iconname,panelname);
		leftpanelHolder.addTab(pan.getCaption(), pan.getIcon(), pan);
		leftpanelHolder.setSelectedComponent(pan);
		configurationChanged();
		validateLeftPanel();
		return pan;
	}
	
	public File getConfigDir() {
		return m_configfile.getParentFile();
	}
	
	public File getAbsolutFileRelativeToConfig(String file) {
		File ff;
		if (!FileUtility.isAbsolutePath(file)) {
			ff=new File(this.getConfigDir(),file);
		} else {
			ff=new File(file);
		}		
		return ff;
	}
	public void initConfigFile(String filename) throws Exception {
		setConfigFile(new File(filename));
		
		if (m_configfile.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(m_configfile.getAbsolutePath());
			
			NodeList panelnodes=	doc.getElementsByTagName("panel");
			if (panelnodes.getLength()==0) {
				throw new Exception("NOT A CONFIG FILE");
			}
			for (int i=0;i<panelnodes.getLength();i++) {
				Element panelelement=(Element)panelnodes.item(i);
				AntRunnerPanel pan=this.addPanel(panelelement.getAttribute("icon"),panelelement.getAttribute("caption"));
				NodeList nl=panelelement.getChildNodes();
				for (int ii=0;ii<nl.getLength();ii++) {
					if (nl.item(ii) instanceof Element) {
						Element ce=(Element)nl.item(ii);
						if (ce.getNodeName().equalsIgnoreCase(AntRunnerNode.getXMLTagName(AntRunnerNode.MODE_STANDALONEBUILDFILE))) {
							String buildfile=ce.getTextContent();
							File ff=getAbsolutFileRelativeToConfig(buildfile);
							pan.addBuildFile(ff);
						}
						if (ce.getNodeName().equalsIgnoreCase(AntRunnerNode.getXMLTagName(AntRunnerNode.MODE_STANDALONEDIR))) {
							String dir=ce.getTextContent();
							File ff=getAbsolutFileRelativeToConfig(dir);
							pan.addDir(ff,"*.xml");	
						}
						if (ce.getNodeName().equalsIgnoreCase(AntRunnerNode.getXMLTagName(AntRunnerNode.MODE_STANDALONETARGET))) {
							String tname=ce.getTextContent();
							String fname=ce.getAttribute("buildfile");
							File ff=getAbsolutFileRelativeToConfig(fname);
							try {
								BuildFileInfo bi=AntUtils.getBuildFileInfo(ff);
								pan.addTarget(ff, bi.name, tname);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}						
					}
				}
			}
			
			NodeList shortcutnodes=doc.getElementsByTagName("shortcut");
			for (int i=0;i<shortcutnodes.getLength();i++) {
				Element element=(Element)shortcutnodes.item(i);	
				String buildfile=element.getAttribute("buildfile");
				File ff=this.getAbsolutFileRelativeToConfig(buildfile);
				AntRunnerNode an=this.findNodeFor(ff,element.getAttribute("target"));
				if (an!=null) {
					this.addShortCut(an,element.getAttribute("icon"),element.getAttribute("title"));
				}
			}
			
			this.m_configfilechanged=false;
			this.refreshTitle();
			this.validate();
		} else {
			throw new Exception("File does not exitst");
		}

	}
	private AntRunnerNode findNodeFor(File file, String target) {
		//RUN ALL PANELS /RUN THE TREE
		for (int i=0;i<leftpanelHolder.getTabCount();i++) {
			AntRunnerPanel ap=(AntRunnerPanel)leftpanelHolder.getComponentAt(i);
			AntRunnerNode an=ap.findNodeFor(file,target);
			if (an!=null) return an;
		}
		return null;
	}

	public void addShortCut(AntRunnerNode an,String icon,String title) throws Exception {
		Icon ic=null;
		ic=this.getScaledIconFromImageName(icon, 20, 20);
		if (ic!=null) {
			//REQUEST DEFAULT IMAGE
			if (ic.getIconWidth()==-1) {
				ic=this.getResourceImageIcon("ant.png");
			}
		}
		ShortCutAction ac=this.getFactory().getShortCutAction(an,(Icon)ic,title);
		ac.setIconFile(getAbsolutFileRelativeToConfig(icon));
		shortcutpanel.add(new AntRunnerButton(ac));
	}

	private void setConfigFile(File file) {
		m_configfile=file;
		this.refreshTitle();
		
	}

	public void saveConfigFile(String filename) throws Exception {
		if (filename!=null) {
			setConfigFile(new File(filename));
		}
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("config");
		doc.appendChild(rootElement);
 
		for (int i=0;i<leftpanelHolder.getTabCount();i++) {
			Element pelement = doc.createElement("panel");
			rootElement.appendChild(pelement);
			pelement.setAttribute("caption",leftpanelHolder.getTitleAt(i));
			AntRunnerPanel antpanel=(AntRunnerPanel)leftpanelHolder.getComponentAt(i);
			String ifilename=null;
			if (antpanel.m_ifile!=null) {
				ifilename=getOptionDrivenFileLocation(antpanel.m_ifile,this.getConfigDir());
			}
			pelement.setAttribute("icon",ifilename);
			for (int ii=0;ii<antpanel.rootNode.getChildCount();ii++) {
				AntRunnerNode node=(AntRunnerNode)antpanel.rootNode.getChildAt(ii);
				Element enode=node.createXMLNode(this.getConfigDir(),doc);
				pelement.appendChild(enode);				
			}
		}
		
		for (int i=0;i<this.shortcutpanel.getComponentCount();i++) {
			AntRunnerButton ab=(AntRunnerButton)this.shortcutpanel.getComponent(i);
			Element pelement = doc.createElement("shortcut");
			rootElement.appendChild(pelement);
			pelement.setAttribute("title", ab.getText());
			pelement.setAttribute("icon", getOptionDrivenFileLocation(ab.getIconFile(),this.getConfigDir()));
			pelement.setAttribute("buildfile", getOptionDrivenFileLocation(ab.getNode().getBuildFile(),this.getConfigDir()));
			pelement.setAttribute("target", ab.getNode().getTargetName());
		}
 
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", new Integer(2));
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(m_configfile);
		
		transformer.transform(source, result);
		this.setConfigFile(m_configfile);
		m_configfilechanged=false;
		this.refreshTitle();
	}
	public void saveProperties() throws Exception {
		Rectangle r=this.getBounds();
		myProperties.put("application.width",Integer.toString(r.width));
		myProperties.put("application.height",Integer.toString(r.height));
		myProperties.put("application.x",Integer.toString(r.x) );
		myProperties.put("application.y",Integer.toString(r.y) );

		r=console.m_frame.getBounds();
		myProperties.put("consoleframe.width",Integer.toString(r.width));
		myProperties.put("consoleframe.height",Integer.toString(r.height));
		myProperties.put("consoleframe.x",Integer.toString(r.x) );
		myProperties.put("consoleframe.y",Integer.toString(r.y) );
		
		myProperties.put("console.modus",Integer.toString(console.getModus()));
		
		r=batchview.m_frame.getBounds();
		myProperties.put("batchview.width",Integer.toString(r.width));
		myProperties.put("batchview.height",Integer.toString(r.height));
		myProperties.put("batchview.x",Integer.toString(r.x) );
		myProperties.put("batchview.y",Integer.toString(r.y) );
		
		myProperties.put("batchview.modus",Integer.toString(batchview.getModus()));
		
		myProperties.put("shortcuts.display", Integer.toString(this.m_shortcutmode));
		
		myProperties.put("application.savemode",Integer.toString(this.getSaveModus()));
	
		if (batchview.getCurrentFile()!=null) {
			myProperties.put("application.batchfile",batchview.getCurrentFile().getAbsolutePath());
		} else {
			myProperties.remove("application.batchfile");
		}
		
		myProperties.put("application.divider",Double.toString(getDividerPercentage()));		
		
		File file=new File(System.getProperty("user.home") ,"antglider.properties");
		myProperties.store(new FileOutputStream(file),"last changed - " +(new SimpleDateFormat()).format(new Date()));
	}	
	private double getDividerPercentage() {
		return (double)mainpane.getDividerLocation()/(double)mainpane.getWidth();
	}

	private void resetLast() throws Exception {
		//TODO GET .settings File
		String w=myProperties.getProperty("application.width", "400");
		String h=myProperties.getProperty("application.height", "400");
		String x=myProperties.getProperty("application.x", "0");
		String y=myProperties.getProperty("application.y", "0");
		
		this.setSize(Integer.parseInt(w), Integer.parseInt(h));
		this.setLocation(Integer.parseInt(x),Integer.parseInt(y));

		//CONSOLE FRAME
		 w=myProperties.getProperty("consoleframe.width", "400");
		 h=myProperties.getProperty("consoleframe.height", "400");
		 x=myProperties.getProperty("consoleframe.x", "0");
		 y=myProperties.getProperty("consoleframe.y", "0");
		
		console.m_frame.setSize(Integer.parseInt(w), Integer.parseInt(h));
		console.m_frame.setLocation(Integer.parseInt(x),Integer.parseInt(y));
		
		
		
		//CONSOLE MODE
		String mode=myProperties.getProperty("console.modus", "1");
		console.setModus(Integer.parseInt(mode));
		Utility.setButtonGroupIndex(consolegroup,Integer.parseInt(mode));

		
		
		//BATCH FRAME
		 w=myProperties.getProperty("batchview.width", "400");
		 h=myProperties.getProperty("batchview.height", "400");
		 x=myProperties.getProperty("batchview.x", "0");
		 y=myProperties.getProperty("batchview.y", "0");
		
		batchview.m_frame.setSize(Integer.parseInt(w), Integer.parseInt(h));
		batchview.m_frame.setLocation(Integer.parseInt(x),Integer.parseInt(y));
		//BATCH MODE
		mode=myProperties.getProperty("batchview.modus", "1");
		batchview.setModus(Integer.parseInt(mode));
		Utility.setButtonGroupIndex(batchgroup,Integer.parseInt(mode));
		
		//LAST BATCH FILE
		String fn=myProperties.getProperty("application.batchfile",null);
		if (fn!=null) {
			File f=new File(fn);
			if (f.exists()) {
				batchview.openFile(f);
			}
		}
		
		//SHORTCUTS OPTION
		mode=myProperties.getProperty("shortcuts.display", "0");
		this.setShortCutMode(Integer.parseInt(mode));
		
		//SAVE OPTIONS
		int savemode=Integer.parseInt(myProperties.getProperty("application.savemode","0"));
		this.setSaveModus(savemode);
		Utility.setButtonGroupIndex(saveoptiongroup,savemode);
		
		
		
		
		//DIVIDER POSITION
		String div=myProperties.getProperty("application.divider", "0.6");
		int idiv=(int)((double)this.getWidth()*Double.parseDouble(div));
		mainpane.setDividerLocation(idiv);
		
	}
	
	public void setShortCutMode(int i) {
		m_shortcutmode=i;
		Utility.setButtonGroupIndex(this.shortgroup,m_shortcutmode);
		if (i==0) {
			shortcutholder.remove(shortcutpanel);
		} else {
			shortcutholder.add("Center",shortcutpanel);
		}
		this.validate();
	}

	public void loadProperties() throws Exception {
		
		
		myProperties=new Properties();
		File file=new File(System.getProperty("user.home") ,"antglider.properties");
		if (file.exists()) {
			myProperties.load(new FileInputStream(file));
		}
	}


	private static void printOutArgs(String[] args) {
		if (args.length==0) {
			AntRunner.logout("ARG : NO ARGS ");			
		}
		for (int i=0;i<args.length;i++) {
			AntRunner.logout("ARG "+ i +" :"+args[i]);
		}
	}


	public void startAnt(String[] args, Properties additionalUserProperties,
			ClassLoader coreLoader) {
		AntRunner.printOutArgs(args);		
		try {
			this.coreLoader=coreLoader;
			if (this.coreLoader==null) {
				this.coreLoader=Thread.currentThread().getContextClassLoader();
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			
			String configfile=AntRunner.examineConfigFileFromArgs(args);
			if (configfile!=null) {
				this.initConfigFile(configfile);
			} else {
				File file=new File(".");
				this.setLastFile(file);
				this.openNewFile();
				
			}
				
			this.loadProperties();
			
			this.setVisible(true);
			this.resetLast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	private static String examineConfigFileFromArgs(String[] args) {
		int i=AntRunner.getIndexFromArgs(args,"-configfile");
		if (i!=-1) return AntRunner.getArg(args,i+1);
		if (args.length==1) {
			return args[0];
		}
		return null;
	}

	private static String getArg(String[] args, int i) {
		if (args.length>i) return args[i];
		return null;
	}

	private static int getIndexFromArgs(String[] args, String string) {
		for (int i=0;i<args.length;i++) {
			if (args[i].equalsIgnoreCase(string)) return i;
		}
		return -1;
	}

	public static void main(String[] args) {
		AntRunner ar;
		try {
			ar = new AntRunner();
			ar.startAnt(args, null, AntRunner.class.getClassLoader());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void openNewFile() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open AntGlider Config File...");
		fc.setCurrentDirectory(this.getLastFile());
		FileFilter filter=new FileNameExtensionFilter("AntGlider Config Files (*.xml)", "xml"); 
		fc.setFileFilter(filter);
		int ret=fc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            this.setLastFile(file);
            try {
            	this.initConfigFile(file.getPath());
			} catch (Exception e1) {
				//ERROR OPENING
				this.setConfigFile(null);
				JOptionPane.showMessageDialog(this, "Error Opening Config-File","AntGlider",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			
		}
		this.refreshTitle();
		
	}

	public Icon getScaledIconFromImageName(String icon,int x,int y) {
		
		File ff=getAbsolutFileRelativeToConfig(icon);
		ImageIcon ii=new ImageIcon(ff.getAbsolutePath());
		ImageIcon m_ic=null;
		if (ii.getIconWidth()==-1) {
			//RETURN A DEFAULT IMAGE
		} else {
			if (ii.getIconWidth()==x && ii.getIconHeight()==y) {
				m_ic=ii;
			}
			m_ic=new ImageIcon(ii.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH));
		}
		return m_ic;
	}

	public AntRunnerActionFactory getFactory() {
		return m_factory;
	}

	public void moveCurrentTab(int i) {
		int idx=leftpanelHolder.getSelectedIndex();
		int nidx=idx+i;
		if (nidx>=leftpanelHolder.getTabCount()) {
			return;
		}
		if (nidx<0) {
			return;
		}
		Component comp = leftpanelHolder.getComponentAt(idx);
		String label = leftpanelHolder.getTitleAt(idx);
		Icon icon = leftpanelHolder.getIconAt(idx);
		String tooltip=leftpanelHolder.getToolTipTextAt(idx);

		leftpanelHolder.remove(idx);

		// Add a new tab
		leftpanelHolder.insertTab(label, icon, comp, tooltip, nidx);
		leftpanelHolder.setSelectedIndex(nidx);
		configurationChanged();
	}

	public static ImageIcon getResourceImageIcon(String path) {
		URL imgURL = AntRunner.class.getResource("/resources/"+path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}	
	public AntRunnerPanel getCurrentPanel() {
		return (AntRunnerPanel)leftpanelHolder.getSelectedComponent();
	}

	public void setLastFile(File file) {
		m_lastfile=file;
		
	}
	public File getLastFile() {
		if (m_lastfile==null) {
			m_lastfile=getConfigDir();
		}
		return m_lastfile;
	}

	public void startAntRunnerNode(AntRunnerNode ar) {
		AntUtils.startForNode(ar,false);
	}

	public String getOptionDrivenFileLocation(File file,File baseDir) {
		if (this.getSaveModus()==AntRunner.SAVEMODUS_ALWAYSRELATIVE) {
			return FileUtility.getRelativePath(baseDir, file);
		}
		if (this.getSaveModus()==AntRunner.SAVEMODUS_ALWAYSABSOLUTE) {
			return file.getAbsolutePath();
		}
		if (this.getSaveModus()==AntRunner.SAVEMODUS_ABSOLUTEWHENNONSUB) {
			if (FileUtility.isSubFile(baseDir,file)) {
				return FileUtility.getRelativePath(baseDir, file);
			} else {
				return file.getAbsolutePath();
			}
		}
		return file.getAbsolutePath();
	}

	int getSaveModus() {
		return this.m_savemodus;
	}

	public void setSaveModus(int mode) {
		m_savemodus=mode;
		
	}

	public void configurationChanged() {
		this.m_configfilechanged=true;
		this.refreshTitle();
	}

	private void refreshTitle() {
		String name="no File";
		if (m_configfile!=null) {
			name=m_configfile.getPath();
			if (!m_configfile.exists()) name=name+" (new)";
		}
		String title="AntGlider - "+name;
		if (m_configfilechanged) {
			title=title+" *";
		}
		this.setTitle(title);
	}
	public void askforSaveAndQuit() throws Exception {
		if (m_configfilechanged) {
			int answer=JOptionPane.showConfirmDialog(this, "config file has been modified! Save?", AntRunner.applicatonName,JOptionPane.YES_NO_CANCEL_OPTION);
			if (answer==JOptionPane.CANCEL_OPTION) return;
			if (answer==JOptionPane.YES_OPTION) this.saveConfigFile(null);
		}
		this.saveProperties();
		System.exit(0);		
	}

	public boolean isShortCutEnabled() {
		return (this.m_shortcutmode==1);
	}

	public boolean isShortCut(AntRunnerNode antRunnerNode) {
		for (int i=0;i<this.shortcutpanel.getComponentCount();i++) {
			AntRunnerButton ab=(AntRunnerButton)this.shortcutpanel.getComponent(i);
			if (ab.getNode()==antRunnerNode) {
				return true;
			}
		}
		return false;
	}

	public void selectAndDisplayNode(AntRunnerNode node) {
		AntRunnerPanel ap=this.findPanelForNode(node);
		this.setCurrentPanel(ap);
		this.getCurrentPanel().selectNode(node);
	}

	private void setCurrentPanel(AntRunnerPanel ap) {
		leftpanelHolder.setSelectedComponent(ap);
	}

	private AntRunnerPanel findPanelForNode(AntRunnerNode node) {
		for (int i=0;i<leftpanelHolder.getTabCount();i++) {
			AntRunnerPanel ap=(AntRunnerPanel)leftpanelHolder.getComponentAt(i);
			if (ap.isElement(node)) {
				return ap;
			}
		}
		return null;
	}

	public TitleAndIconSelection getTabForm() throws Exception {
		if (this.tabForm==null) {
			this.tabForm=new TitleAndIconSelection(this);
			this.tabForm.setLocation(this.getBounds().x +50,this.getBounds().y+50);
			this.tabForm.setModal(true);
		}		
		return tabForm;
	}

	public AntRunnerButton getCurrentShortCut() {
		return this.m_currentshortcut;
	}
	public void removeCurrentShortCut() {
		this.shortcutpanel.remove(m_currentshortcut);
		m_currentshortcut=null;
		this.configurationChanged();
		this.invalidate();
		this.validate();
	}

	public void moveCurrentShortCut(int i) {
		Vector<Component> vc=Utility.getVectorFromContainer(this.shortcutpanel);
		int idx=Utility.getComponentIndex(shortcutpanel,this.getCurrentShortCut());
		vc.remove(idx);
		vc.insertElementAt(this.getCurrentShortCut(),idx+i);
		shortcutpanel.removeAll();
		Utility.putVectorToConainer(shortcutpanel, vc);
		this.invalidate();
		this.validate();		
	}

	public void setCurrentShortCut(AntRunnerButton antRunnerButton) throws Exception {
		m_currentshortcut=antRunnerButton;	
		this.armShortCutActions();
	}

	private void unArmShortCutActions() throws Exception {
		getFactory().getActionForName("shortcut.EditShortCutAction").setEnabled(false);
		getFactory().getActionForName("shortcut.RemoveShortCutAction").setEnabled(false);
		getFactory().getActionForName("shortcut.MoveBackwardShortCutAction").setEnabled(false);
		getFactory().getActionForName("shortcut.MoveForwardShortCutAction").setEnabled(false);
			
	}
	private void armShortCutActions() throws Exception {
		this.unArmShortCutActions();
		int idx=Utility.getComponentIndex(this.shortcutpanel,this.getCurrentShortCut());
		int cc=this.shortcutpanel.getComponentCount();
		getFactory().getActionForName("shortcut.EditShortCutAction").setEnabled(true);
		getFactory().getActionForName("shortcut.RemoveShortCutAction").setEnabled(true);
		if (idx!=0) {
			getFactory().getActionForName("shortcut.MoveBackwardShortCutAction").setEnabled(true);			
		}
		if (idx!=(cc-1)) {
			getFactory().getActionForName("shortcut.MoveForwardShortCutAction").setEnabled(true);			
		}
	}


	public static void logout(String string) {
		if (dologging) {
			System.out.println(string);
		}
	}

	public static void printOutArray(URL[] s) {
		for (int i=0;i<s.length;i++) {
			AntRunner.logout(s[i].toString());
		}
		
	}

	public int getShortCutMode() {
		return this.m_shortcutmode;
	}

}
