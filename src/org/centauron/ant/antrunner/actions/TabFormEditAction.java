package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;



public class TabFormEditAction extends AntRunnerAction {
	public TabFormEditAction() {
		super("Edit");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a new Tab");
		this.putValue(LONG_DESCRIPTION, "Add a new Tab");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String iconname=this.antrunner.tabForm.getIconText();
			String panelname=this.antrunner.tabForm.getTitleText();
			int index=this.antrunner.leftpanelHolder.getSelectedIndex();
			this.antrunner.leftpanelHolder.setTitleAt(index, panelname);
			this.antrunner.tabForm.setVisible(false);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this.antrunner, "Error creating Tab.","AntRunner",JOptionPane.ERROR_MESSAGE);			
		}
	}
}
