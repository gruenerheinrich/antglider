package org.centauron.ant.antrunner;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;



import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class AntRunnerNodeRenderer extends DefaultTreeCellRenderer implements ListCellRenderer  {

	private JLabel label;
	
	public AntRunnerNodeRenderer() {

	}
	public JComponent getComponentFromAntRunnerNode(AntRunnerNode nn,boolean selected) {
		label=new JLabel();
		label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		
		label.setText((String)nn.getCaption());
		if (nn.getMode()==AntRunnerNode.MODE_STANDALONEBUILDFILE || nn.getMode()==AntRunnerNode.MODE_BUILDFILE) {
			label.setIcon(AntRunner.getResourceImageIcon("ant_buildfile.png"));
		}
		if (nn.getMode()==AntRunnerNode.MODE_TARGET) {
			label.setIcon(AntRunner.getResourceImageIcon("targetpublic_obj.png"));
		}
		if (nn.getMode()==AntRunnerNode.MODE_STANDALONETARGET) {
			label.setIcon(AntRunner.getResourceImageIcon("ant_view.png"));
		}			
		if (nn.getMode()==AntRunnerNode.MODE_DEFAULTTARGET) {
			label.setIcon(AntRunner.getResourceImageIcon("defaulttarget_obj.png"));
		}
		if (nn.getMode()==AntRunnerNode.MODE_DIR || nn.getMode()==AntRunnerNode.MODE_STANDALONEDIR) {
			label.setIcon(AntRunner.getResourceImageIcon("folder.png"));
		}
		if (nn.getMode()==AntRunnerNode.MODE_BATCHTARGET) {
			label.setIcon(AntRunner.getResourceImageIcon("targetinternal_obj.png"));
		}
		
		if (selected) {
			label.setOpaque(true);
			label.setBackground(new Color(100,200,200));
		} else {
			label.setOpaque(false);
			if (nn.getLastResult()==AntRunnerNode.RESULT_SUCCESS) {
				label.setForeground(new Color(10,255,10));
			}
			if (nn.getLastResult()==AntRunnerNode.RESULT_FAILED) {
				label.setForeground(new Color(255,10,10));
			}			
		}
		if (nn.isRunning()) {
			AntRunnerProgressBar pro=new AntRunnerProgressBar(nn);
			JPanel po=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
			po.setBackground(Color.white);
			po.add(label);
			po.add(pro);
			po.setSize(200,20);
			return po;
		} 
		return label;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,boolean selected, boolean expanded, boolean leaf, int row,boolean hasFocus) {
		// TODO Auto-generated method stub
		if (value instanceof AntRunnerNode) {
			//return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			Component p=getComponentFromAntRunnerNode((AntRunnerNode)value,selected);
			return p;
		} else {
			return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
		JComponent p=getComponentFromAntRunnerNode((AntRunnerNode)value,isSelected);
		if (index%2==1 && !isSelected) {
			p.setOpaque(true);
			p.setBackground(new Color(220,220,220));
		}
		return p;
	}

}
