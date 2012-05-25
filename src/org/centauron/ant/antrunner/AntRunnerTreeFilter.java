package org.centauron.ant.antrunner;

import java.awt.TextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.Enumeration;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.centauron.utility.Utility;



public class AntRunnerTreeFilter extends JTextField {
	private AntRunnerPanel m_antrunnerpanel;
	private DefaultTreeModel filterModel;
	private DefaultMutableTreeNode filterRoot;
	private Enumeration expansionState;
	private boolean m_filteractive;
	public AntRunnerTreeFilter(AntRunnerPanel panel) {
		super(20);
		m_antrunnerpanel=panel;
		this.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				setSelectionStart(0);
				setSelectionEnd(getText().length());
			}
		});
		this.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				runSearch();	
			}
			public void insertUpdate(DocumentEvent e) {
				runSearch();			
			}
			public void removeUpdate(DocumentEvent e) {
				runSearch();
			}
			
		});
		filterRoot=new DefaultMutableTreeNode("FilterRoot");
		filterModel=new DefaultTreeModel(filterRoot) {
			public void reload() {
				//FILTER FIRST
				filter();
				super.reload();
			}
		};
	}
	
	public void filter() {
		filterRoot.removeAllChildren();
		DefaultMutableTreeNode orig=this.m_antrunnerpanel.rootNode;
		
		boolean b=filter_addMatchingChildren(filterRoot,orig);
	}
	
	private boolean filter_addMatchingChildren(DefaultMutableTreeNode to,DefaultMutableTreeNode orig) {
		for (int i=0;i<orig.getChildCount();i++) {
			DefaultMutableTreeNode ch=(DefaultMutableTreeNode)orig.getChildAt(i);
			DefaultMutableTreeNode nch=(DefaultMutableTreeNode)ch.clone();
			filter_addMatchingChildren(nch,ch);
			//ENTWEDER EIN KIND ODER SELBER OK
			if (nch.getChildCount()>0 || filter_isMatching(nch)) {
				to.add(nch);
			}
		}
		return true;
	}

	private boolean filter_isMatching(DefaultMutableTreeNode nch) {
		String cap=(String)nch.getUserObject();
		return Utility.wildCardMatch(cap.toUpperCase(),this.getText().toUpperCase());
	}

	public void runSearch() {
		DefaultTreeModel dm=(DefaultTreeModel)this.m_antrunnerpanel.tree.getModel();
		if (this.getText().length()==0) {
			this.m_antrunnerpanel.tree.setModel(this.m_antrunnerpanel.fullModel);
			Utility.loadExpansionState(this.m_antrunnerpanel.tree, expansionState);
			m_filteractive=false;
		} else {
			if (!m_filteractive) {
				expansionState=Utility.saveExpansionState(this.m_antrunnerpanel.tree);
			}
			m_filteractive=true;
			filterModel.reload();
			this.m_antrunnerpanel.tree.setModel(filterModel);
			for (int i = 0; i < this.m_antrunnerpanel.tree.getRowCount(); i++) {
				this.m_antrunnerpanel.tree.expandRow(i);
			}
		}
	}




	
}
