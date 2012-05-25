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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.tools.ant.util.FileUtils;
import org.centauron.utility.FileUtility;
import org.centauron.utility.PopupMenuAdapter;
import org.centauron.utility.Utility;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AntRunner extends JFrame {
	public final static int SAVEMODUS_ALWAYSRELATIVE=0;
	public final static int SAVEMODUS_ALWAYSABSOLUTE=1;
	public final static int SAVEMODUS_ABSOLUTEWHENNONSUB=2;
	public final static int SAVEMODUS_ASKWHENNONSUB=3;
	
	public final static String SHORTCUTPANEL="shortcut";
	private File m_configfile;
	private JPanel shortcutpanel;
	private JMenuBar  themenubar;
	public JTabbedPane leftpanelHolder;
	public JTabbedPane rightpanelHolder;
	private Properties myProperties;
	private AntRunnerActionFactory m_factory;
	private JPopupMenu panelpopup;
	public TabForm tabForm;
	private File m_lastfile;
	private JSplitPane mainpane;
	public AntRunnerConsole console;
	public AntRunnerBatchView batchview;
	private ButtonGroup consolegroup;
	private ButtonGroup batchgroup;
	private ButtonGroup saveoptiongroup;
	private double m_tempdivider;
	private int m_savemodus;
	public AntRunner() throws Exception {
		m_factory=new AntRunnerActionFactory(this);
		shortcutpanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
		
		this.initMenuBars();
		this.setIconImage(AntRunner.getResourceImageIcon("ant.png").getImage());
		console=new AntRunnerConsole(this);
		batchview=new AntRunnerBatchView(this);
		
		leftpanelHolder=new JTabbedPane();
		leftpanelHolder.addMouseListener(new PopupMenuAdapter(panelpopup));
		validateLeftPanel();
		mainpane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainpane.add(leftpanelHolder);
		
		rightpanelHolder=new JTabbedPane();
		validateLeftPanel();
		mainpane.add(rightpanelHolder);
		
		JPanel pane=new JPanel(new BorderLayout());
		//pane.add("North",themenubar);
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
		file.add(m_factory.getActionForName("CloseAction"));
		themenubar.add(file);
		
		JMenu modify=new JMenu("Modify");
		modify.add(m_factory.getActionForName("AddPanelAction"));
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

		themenubar.add(view);
		
		
		JMenu options=new JMenu("Options");
		JMenu saveoptionmenu=new JMenu("Save Options");
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
		panelpopup.add(getFactory().getActionForName("AddPanelAction"));
		panelpopup.add(getFactory().getActionForName("EditPanelAction"));
		panelpopup.add(getFactory().getActionForName("RemovePanelAction"));
		panelpopup.addSeparator();
		panelpopup.add(getFactory().getActionForName("MoveBackwardPanelAction"));
		panelpopup.add(getFactory().getActionForName("MoveForwardPanelAction"));
		
	}
	private void validateLeftPanel() {
		if (leftpanelHolder.getTabCount()==0) {
			//CREATE EMPTY TAB
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

	public void addButton(String panelname,AntRunnerElement al) {
		if (panelname.equalsIgnoreCase(AntRunner.SHORTCUTPANEL)) {
			this.shortcutpanel.add(new AntRunnerButton(al));
		}
		
	}
	
	public AntRunnerPanel addPanel(String iconname,String panelname) throws Exception {
		File fullic=new File(this.getConfigDir(),iconname);
		AntRunnerPanel pan=new AntRunnerPanel(this,fullic.getAbsolutePath(),panelname);
		leftpanelHolder.addTab(pan.getCaption(), pan.getIcon(), pan);
		leftpanelHolder.setSelectedComponent(pan);
		return pan;
	}
	
	public File getConfigDir() {
		return m_configfile.getParentFile();
	}
	public void initConfigFile(String filename) throws Exception {
		setConfigFile(new File(filename));
		
		if (m_configfile.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filename);
			NodeList panelnodes=	doc.getElementsByTagName("panel");
			for (int i=0;i<panelnodes.getLength();i++) {
				Element panelelement=(Element)panelnodes.item(i);
				AntRunnerPanel pan=this.addPanel(panelelement.getAttribute("icon"),panelelement.getAttribute("caption"));
				NodeList nl=panelelement.getChildNodes();
				for (int ii=0;ii<nl.getLength();ii++) {
					if (nl.item(ii) instanceof Element) {
						Element ce=(Element)nl.item(ii);
						if (ce.getNodeName().equalsIgnoreCase(AntRunnerNode.getXMLTagName(AntRunnerNode.MODE_STANDALONEBUILDFILE))) {
							String buildfile=ce.getTextContent();
							File ff;
							//TODO GET BUILDFILEINFO TO SEE IF IT IS OK
							
							if (!FileUtils.isAbsolutePath(buildfile)) {
								ff=new File(this.getConfigDir(),buildfile);
							} else {
								ff=new File(buildfile);
							}
							pan.addBuildFile(ff);
						}
						if (ce.getNodeName().equalsIgnoreCase(AntRunnerNode.getXMLTagName(AntRunnerNode.MODE_STANDALONEDIR))) {
							String dir=ce.getTextContent();
							File ff;
							if (!FileUtils.isAbsolutePath(dir)) {
								ff=new File(this.getConfigDir(),dir);
							} else {
								ff=new File(dir);
							}
							pan.addDir(ff,"*.xml");	
						}
						if (ce.getNodeName().equalsIgnoreCase(AntRunnerNode.getXMLTagName(AntRunnerNode.MODE_STANDALONETARGET))) {
							String tname=ce.getTextContent();
							String fname=ce.getAttribute("buildfile");
							File ff;
							if (!FileUtils.isAbsolutePath(fname)) {
								ff=new File(this.getConfigDir(),fname);
							} else {
								ff=new File(fname);
							}
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
		} else {
			throw new Exception("File does not exitst");
		}

	}
	private void setConfigFile(File file) {
		m_configfile=file;
		String name=m_configfile.getPath();
		if (!m_configfile.exists()) name=name+" (new)";
		this.setTitle("AntRunner - "+name);
		
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
			//panel.setAttribute("icon",leftpanelHolder.getIconAt(i));
			AntRunnerPanel antpanel=(AntRunnerPanel)leftpanelHolder.getComponentAt(i);
			//ALL SAVEABLE NODES ARE CHILDS OF THE ROOT
			for (int ii=0;ii<antpanel.rootNode.getChildCount();ii++) {
				AntRunnerNode node=(AntRunnerNode)antpanel.rootNode.getChildAt(ii);
				Element enode=node.createXMLNode(this.getConfigDir(),doc);
				pelement.appendChild(enode);				
			}
		}
 
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", new Integer(2));
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(m_configfile);
		
		transformer.transform(source, result);
		this.setConfigFile(m_configfile);
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
		
		
		myProperties.put("application.divider",Double.toString(getDividerPercentage()));		
		
		File file=new File(System.getProperty("user.home") ,"antrunner.properties");
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
		

		//DIVIDER POSITION
		String div=myProperties.getProperty("application.divider", "0.6");
		mainpane.setDividerLocation(Double.parseDouble(div));
		
	}
	
	public void loadProperties() throws Exception {
		
		
		myProperties=new Properties();
		File file=new File(System.getProperty("user.home") ,"antrunner.properties");
		if (file.exists()) {
			myProperties.load(new FileInputStream(file));
		}
	}

	
	public static void main(String[] args) {
		//PARSE PARAMS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			AntRunner ar=new AntRunner();
			if (args.length==1) {
				ar.initConfigFile(args[0]);
			}
			ar.loadProperties();
			ar.resetLast();
			ar.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Icon getScaledIconFromImageName(String icon,int x,int y) {
		
		ImageIcon ii=new ImageIcon(icon);
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
		return null;
	}

	private int getSaveModus() {
		return this.m_savemodus;
	}

	public void setSaveModus(int mode) {
		m_savemodus=mode;
		
	}


}
