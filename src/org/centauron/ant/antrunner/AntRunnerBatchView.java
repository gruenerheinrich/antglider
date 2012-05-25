package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.centauron.utility.ArrayListTransferHandler;
import org.centauron.utility.PopupMenuAdapter;
import org.centauron.utility.Utility;
import org.centauron.utility.VerticalFlowLayout;


public class AntRunnerBatchView extends JPanel {
	public static final int MODE_HIDE = 0;
	public static final int MODE_ATTACH = 1;
	public static final int MODE_FRAME = 2;
	private AntRunner m_runner;
	private JList m_list;
	public JFrame m_frame;
	private int m_modus;
	private JMenuBar  themenubar;
	private File m_currentfile;
	private JPopupMenu popup;
	private DropTargetListener mydropper;
	private AntRunnerBatchParser myparser;
	public AntRunnerBatchView(AntRunner runner) throws Exception {
		super(new BorderLayout(5,5));
		m_runner=runner;
		myparser=new AntRunnerBatchParser(runner);
		m_frame=new JFrame(this.getCaptionTitle());
		m_frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub				
			}			
		});
		m_frame.setIconImage(AntRunner.getResourceImageIcon("script.png").getImage());
		m_frame.getContentPane().setLayout(new BorderLayout(5,5));
		
		initMenu();
		JPanel menupanel=new JPanel(new BorderLayout());
		menupanel.add("North",themenubar);

		this.add("North",menupanel);
		m_list=new JList(new DefaultListModel());
		//ACT AS SELECT/UNSELECT
		m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_list.setSelectionModel(new DefaultListSelectionModel() {
		    @Override
		    public void setSelectionInterval(int index0, int index1) {
		            if (isSelectedIndex(index0)) {
		                super.removeSelectionInterval(index0, index1);
		            } else {
		            	super.removeSelectionInterval(0,super.getMaxSelectionIndex());
		                super.setSelectionInterval(index0, index1);
		            }
		    }

		});
		m_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				try {
					listSelectionChanged();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	
		});
		m_list.setCellRenderer(new AntRunnerNodeRenderer());
		m_list.setAutoscrolls(true);
		m_list.setFixedCellHeight(-1);
		m_list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					if (e.getButton()==MouseEvent.BUTTON3) {
						int tp=m_list.locationToIndex(e.getPoint());
						if (tp==-1) {
							return;
						}
						if (tp!=m_list.getSelectedIndex()) {
							m_list.setSelectedIndex(tp);
						}
						popup.show((Component) e.getSource(), e.getX(), e.getY());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}				
		});
		m_list.setDropMode(DropMode.INSERT);

		this.add("Center",new JScrollPane(m_list));
		JPanel modifyPanel=new JPanel(new VerticalFlowLayout(VerticalFlowLayout.LEFT,VerticalFlowLayout.BOTTOM,2,2));			
		modifyPanel.add(this.getSmallButtonForAction("BatchAddSelectedAction"));
		modifyPanel.add(this.getSmallButtonForAction("BatchMoveBackwardAction"));
		modifyPanel.add(this.getSmallButtonForAction("BatchMoveForwardAction"));
		modifyPanel.add(this.getSmallButtonForAction("BatchRemoveSelectedAction"));
		
		JPanel actionPanel=new JPanel(new VerticalFlowLayout(VerticalFlowLayout.LEFT,VerticalFlowLayout.TOP,2,2));
		actionPanel.add(this.getSmallButtonForAction("BatchRunAction"));
		actionPanel.add(this.getSmallButtonForAction("BatchHaltAction"));

		JPanel allPanel=new JPanel(new BorderLayout());
		allPanel.add("North",actionPanel);
		allPanel.add("South",modifyPanel);
		this.add("East",allPanel);
		
		unArmActions();
	
		//BUILD MY DROPPER
		mydropper=new DropTargetListener() {

			public void dragEnter(DropTargetDragEvent dtde) {
			}
			public void dragExit(DropTargetEvent dte) {
			}
			public void dragOver(DropTargetDragEvent dtde) {
			}
			public void drop(DropTargetDropEvent e) {
				try {
					Point p=e.getLocation();
					int i=m_list.locationToIndex(p);
					Transferable tr = e.getTransferable();
					DataFlavor[] flavs=tr.getTransferDataFlavors();
					Object o=tr.getTransferData(flavs[0]);
					DefaultMutableTreeNode tn=((DefaultMutableTreeNode[])o)[0]; 
					AntRunnerNode ar=(AntRunnerNode)((AntRunnerNode)tn.getUserObject()).clone();
					ar.setMode(AntRunnerNode.MODE_BATCHTARGET);
					ar.setNotifier(this);
					if (i==-1) {
						getListModel().addElement(ar);
					} else {
						getListModel().insertElementAt(ar, i);
						
					}
					m_list.updateUI();
					e.dropComplete(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}			
		};
		DropTarget dropTarget = new DropTarget (m_list, mydropper);
	}
	
	
	private void listSelectionChanged() throws Exception  {
		if (m_list.getSelectedIndex()==-1) {
			this.getFactory().getActionForName("BatchMoveBackwardAction").setEnabled(false);
			this.getFactory().getActionForName("BatchMoveForwardAction").setEnabled(false);
			this.getFactory().getActionForName("BatchRemoveSelectedAction").setEnabled(false);			
		} else {
			this.getFactory().getActionForName("BatchRemoveSelectedAction").setEnabled(true);
			this.getFactory().getActionForName("BatchMoveBackwardAction").setEnabled( m_list.getSelectedIndex()!=0 );		
			this.getFactory().getActionForName("BatchMoveForwardAction").setEnabled( m_list.getSelectedIndex()!=(m_list.getModel().getSize()-1));
		}
	}
	
	private void unArmActions() throws Exception {
		this.getFactory().getActionForName("BatchAddSelectedAction").setEnabled(false);
		this.getFactory().getActionForName("BatchMoveBackwardAction").setEnabled(false);
		this.getFactory().getActionForName("BatchMoveForwardAction").setEnabled(false);
		this.getFactory().getActionForName("BatchRemoveSelectedAction").setEnabled(false);
	}




	private Component getSmallButtonForAction(String string) throws Exception {
		JButton butt=new JButton(this.getFactory().getActionForName(string));
		butt.setText("");
		butt.getInsets().right=2;
		butt.getInsets().left=2;
		return butt;
	}




	private void initMenu() throws Exception {
		themenubar=new JMenuBar();
		JMenu file=new JMenu("File");
		file.add(getFactory().getActionForName("NewBatchFileAction"));
		file.add(getFactory().getActionForName("OpenBatchFileAction"));
		file.add(getFactory().getActionForName("SaveBatchFileAction"));
		file.add(getFactory().getActionForName("SaveAsBatchFileAction"));
		themenubar.add(file);
		
		popup=new JPopupMenu();
		popup.add(getFactory().getActionForName("BatchRunSingleAction"));
		popup.add(getFactory().getActionForName("BatchMoveBackwardAction"));
		popup.add(getFactory().getActionForName("BatchMoveForwardAction"));
		popup.add(getFactory().getActionForName("BatchRemoveSelectedAction"));		
		
	}




	private AntRunnerActionFactory getFactory() {
		return this.m_runner.getFactory();
	}



	public void setModus(int mode) throws Exception {
		m_modus=mode;
		this.m_runner.rightpanelHolder.remove(this);
		//VALIDATE
		m_frame.setVisible(false);
		m_frame.remove(this);
		
		if (mode==AntRunnerBatchView.MODE_HIDE) {
			//NOTHING
			this.getFactory().getActionForName("BatchAddSelectedAction").setEnabled(false);
		}
		if (mode==AntRunnerBatchView.MODE_ATTACH) {
			this.getFactory().getActionForName("BatchAddSelectedAction").setEnabled(true);
			this.m_runner.rightpanelHolder.addTab(getCaptionTitle(), AntRunner.getResourceImageIcon("script.png"),this);
		}		
		if (mode==AntRunnerBatchView.MODE_FRAME) {
			this.getFactory().getActionForName("BatchAddSelectedAction").setEnabled(true);
			m_frame.getContentPane().add("Center",this);
			m_frame.setVisible(true);
		}			
		this.m_runner.validateRightPanel();		
	}


	private String getCaptionTitle() {
		String name="(new)";
		if (m_currentfile!=null) {
			name=m_currentfile.getName();
		}
		return "Batch - "+name;
	}
	
	private void updateCaptionTitle() {
		m_frame.setTitle(this.getCaptionTitle());
		int idx=Utility.getTabIndex(this.m_runner.rightpanelHolder,this);
		if (idx>-1) {
			this.m_runner.rightpanelHolder.setTitleAt(idx, this.getCaptionTitle());
		}
	}




	public int getModus() {
		return m_modus;
	}




	public void openFile(File f) throws Exception {
		Vector<AntRunnerNode> ano=myparser.getNodesFromFile(f,this);
		this.getListModel().removeAllElements();
		for (AntRunnerNode ar:ano) {
			this.getListModel().addElement(ar);
		}
		this.updateCaptionTitle();
	}



	public File getCurrentBatchView() {
		return this.m_currentfile;
	}

	public void saveFile(File f) throws Exception {
		// TODO Auto-generated method stub
		if (f==null) {
			f=this.m_currentfile;
		}
		myparser.saveNodesToFile(f, this.getAllElements());
		this.m_currentfile=f;
		this.updateCaptionTitle();
	}

	public Vector<AntRunnerNode> getAllElements() {
		Vector<AntRunnerNode> ll=new Vector();
		for (int i=0;i<this.getListModel().getSize();i++) {
			ll.add((AntRunnerNode)getListModel().get(i));
		}
		return ll;
	}


	public void addElement(AntRunnerNode node) {
		//IS LIST SELECTED
		node.setNotifier(this);
		node.setMode(AntRunnerNode.MODE_BATCHTARGET);
		int idx=this.getLastSelectedIdx();
		if (idx==-1) {
			getListModel().addElement(node);
			idx=getListModel().getSize()-1;
		} else {
			getListModel().add(idx, node);
		}
		m_list.setSelectedIndex(-1);
		m_list.setSelectedIndex(idx);
	}




	private int getLastSelectedIdx() {
		int[] idx=m_list.getSelectedIndices();
		if (idx.length==0) return -1;
		return idx[idx.length-1];
	}


	public void moveCurrentElement(int i) {
		int idx=m_list.getSelectedIndex();
		Object o=getListModel().get(idx);
		getListModel().remove(idx);
		getListModel().insertElementAt(o, idx+i);
		m_list.setSelectedIndex(idx+i);
	}


	public void removeCurrentElement() {
		int idx=m_list.getSelectedIndex();		
		if (idx>-1) {
			getListModel().remove(idx);
		}
		m_list.setSelectedIndex(-1);
	}


	public void newFile() {
		this.m_currentfile=null;
		
		getListModel().removeAllElements();
		this.updateCaptionTitle();
	}


	public AntRunnerNode getCurrentElement() {
		return (AntRunnerNode)m_list.getSelectedValue();
	}
	
	public DefaultListModel getListModel() {
		return (DefaultListModel)m_list.getModel();
	}


	public JList getList() {
		return m_list;
	}



}
