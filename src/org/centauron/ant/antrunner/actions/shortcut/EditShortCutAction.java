package org.centauron.ant.antrunner.actions.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;

import org.centauron.ant.antrunner.AntRunnerPanel;
import org.centauron.ant.antrunner.TitleAndIconSelection;
import org.centauron.ant.antrunner.actions.AntRunnerAction;



public class EditShortCutAction extends AntRunnerAction {
	public EditShortCutAction() {
		super("Edit Shortcut...");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a new Tab");
		this.putValue(LONG_DESCRIPTION, "Add a new Tab");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			int idx=this.antrunner.leftpanelHolder.getSelectedIndex();
			TitleAndIconSelection tis=this.antrunner.getTabForm();
			tis.setMode(TitleAndIconSelection.MODE_EDITPANEL);
			tis.setTitleText(this.antrunner.getCurrentShortCut().getText());
			tis.setIconText("");
			tis.setVisible(true);
			if (tis.getResult()==TitleAndIconSelection.RESULT_OK) {
				this.antrunner.getCurrentShortCut().setText(tis.getTitleText());	
				this.antrunner.getCurrentShortCut().setIconName(tis.getIconText());
				this.antrunner.configurationChanged();
				this.antrunner.invalidate();
				this.antrunner.validate();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
