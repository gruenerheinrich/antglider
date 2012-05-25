package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.PopupMenu;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.centauron.utility.ArrayListTransferHandler;
import org.centauron.utility.PopupMenuAdapter;
import org.centauron.utility.TreeTransferHandler;

public class AntRunnerPanel extends JPanel {
	private String m_caption;
	private Icon m_icon;
	public JTree tree;
	public DefaultMutableTreeNode  rootNode;
	public DefaultTreeModel fullModel;
	private JPopupMenu popup;
	public AntRunner antrunner;
	public AntRunnerPanel(AntRunner ar,String icon,String name) throws Exception {
		super(new BorderLayout(0,0));
		antrunner=ar;
		m_caption=name;
		m_icon=AntRunner.getScaledIconFromImageName(icon,20,20);
		
		popup=new JPopupMenu();
		popup.add(ar.getFactory().getActionForName("AddDirAction"));
		popup.add(ar.getFactory().getActionForName("AddFileAction"));
		popup.add(ar.getFactory().getActionForName("AddTargetAction"));
		this.setBackground(Color.white);
		this.add(popup);
		rootNode=new DefaultMutableTreeNode("Root");
		fullModel=new DefaultTreeModel(rootNode);
		tree=new JTree(fullModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setRowHeight(0);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				try {
					getCurrentElement().selectNode();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		tree.setDragEnabled(true);
		
		tree.setTransferHandler(new TreeTransferHandler());
		JPanel searchPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
		JLabel flabel=new JLabel("Filter:",antrunner.getResourceImageIcon("search.png"),0);
		AntRunnerTreeFilter searchWord=new AntRunnerTreeFilter(this);
		searchPanel.add(flabel);
		searchPanel.add(searchWord);
		
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
		/*
		JButton runb=new JButton(this.antrunner.getFactory().getActionForName("RunAction"));
		runb.setText(null);
		buttonPanel.add(runb);
		buttonPanel.add(new JButton(this.antrunner.getFactory().getActionForName("HaltAction")));				
		*/
		JPanel toppanel=new JPanel(new BorderLayout());
		toppanel.add("Center",searchPanel);
		toppanel.add("East",buttonPanel);
		this.add("North",toppanel);
		this.add("Center",new JScrollPane(tree));
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					if (e.getButton()==MouseEvent.BUTTON3) {
						
						TreePath tp=tree.getPathForLocation(e.getX(), e.getY());
						if (tp==null) {
							popup.show((Component) e.getSource(), e.getX(), e.getY());
							return;
						}
						if (!tp.equals(tree.getSelectionPath())) {
							tree.setSelectionPath(tp);
						}
						AntRunnerNode an=(AntRunnerNode)tp.getLastPathComponent();
						JPopupMenu pop=an.getPopupMenu();
						pop.show((Component) e.getSource(), e.getX(), e.getY());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}				
		});
		tree.setCellRenderer(new AntRunnerNodeRenderer());
	}
	public String getCaption() {
		return m_caption;
	}
	public Icon getIcon() {
		return m_icon;
	}
	public void addBuildFile(File file) {
		this.addBuildFile(file, null);
	}
	public boolean addBuildFile(File file,AntRunnerNode parent) {
		BuildFileInfo bi;
		try {
			bi = AntUtils.getBuildFileInfo(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (bi.targets.size()==0) {
			//ERROR
			return false;
		}
		AntRunnerNode fnode=new AntRunnerNode(this.antrunner, this, bi.name,(parent==null)?AntRunnerNode.MODE_STANDALONEBUILDFILE:AntRunnerNode.MODE_BUILDFILE);
		fnode.setBuildFile(file);
		fnode.setTargetName(bi.defaultTarget);		
		if (parent==null) {
			rootNode.add(fnode);
		} else {
			parent.add(fnode);
		}
		for (String n:bi.targets) {
			AntRunnerNode tnode=new AntRunnerNode(this.antrunner,this,n,AntRunnerNode.MODE_TARGET);		
			if (n.equalsIgnoreCase(bi.defaultTarget)) {
				tnode.setMode(AntRunnerNode.MODE_DEFAULTTARGET);
			}
			tnode.setBuildFile(file);
			tnode.setTargetName(n);
			fnode.add(tnode);
		}
		reloadTree();
		return true;
	}
	
	public void addTarget(File file,String caption,String targetname) {
		AntRunnerNode fnode=new AntRunnerNode(this.antrunner,this,targetname+" ["+caption+"]" ,AntRunnerNode.MODE_STANDALONETARGET);
		fnode.setBuildFile(file);
		fnode.setTargetName(targetname);		
		rootNode.add(fnode);
		reloadTree();
		
	}
	public boolean addDir(File dir,String filterMask) {
		return addDir(dir,filterMask,null);
	}
	
	
	public boolean addDir(File dir,String filterMask,AntRunnerNode parent) {
		File[] flist=dir.listFiles();
		AntRunnerNode dnode=new AntRunnerNode(this.antrunner,this,(parent==null)?dir.getName():dir.getName(), (parent==null)?AntRunnerNode.MODE_STANDALONEDIR:AntRunnerNode.MODE_DIR);
		boolean atleastone=false;
		for (File f:flist) {
			if (!f.isHidden()) {
				if (f.isFile()) {
					System.out.println("DO:"+f.getAbsolutePath());
					boolean res=this.addBuildFile(f, dnode);
					atleastone=atleastone || res;
				} else {
					boolean res=this.addDir(f, filterMask,dnode);
					atleastone=atleastone || res;
				}
			}
		}
		if (atleastone) {
			if (parent!=null) {
				parent.add(dnode);
			} else {
				rootNode.add(dnode);
			}
			reloadTree();			
		}
		return atleastone;
	}
	

	public AntRunnerNode getCurrentElement() {
		TreePath tp=tree.getSelectionPath();
		if (tp==null) return null;
		return (AntRunnerNode)tp.getLastPathComponent();
	}
	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
		
	}
	
	public void reloadTree() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload();		
	}
	
}
