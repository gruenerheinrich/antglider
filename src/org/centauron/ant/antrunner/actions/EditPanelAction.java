package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.centauron.ant.antrunner.TabForm;



public class EditPanelAction extends AntRunnerAction {
	public EditPanelAction() {
		super("Edit Tab...");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a new Tab");
		this.putValue(LONG_DESCRIPTION, "Add a new Tab");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			if (this.antrunner.tabForm==null) {
					this.antrunner.tabForm=new TabForm(this.antrunner);
			}
			int idx=this.antrunner.leftpanelHolder.getSelectedIndex();
			this.antrunner.tabForm.setEditMode(true);
			this.antrunner.tabForm.setTitleText(this.antrunner.leftpanelHolder.getTitleAt(idx));
			this.antrunner.tabForm.setIconText("");
			this.antrunner.tabForm.setLocation(this.antrunner.getBounds().x +50,this.antrunner.getBounds().y+50);
			this.antrunner.tabForm.setModal(true);
			this.antrunner.tabForm.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
