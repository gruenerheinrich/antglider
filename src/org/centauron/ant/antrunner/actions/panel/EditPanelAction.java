package org.centauron.ant.antrunner.actions.panel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;

import org.centauron.ant.antrunner.AntRunnerPanel;
import org.centauron.ant.antrunner.TitleAndIconSelection;
import org.centauron.ant.antrunner.actions.AntRunnerAction;



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
			int idx=this.antrunner.leftpanelHolder.getSelectedIndex();
			TitleAndIconSelection tis=this.antrunner.getTabForm();
			tis.setMode(TitleAndIconSelection.MODE_EDITPANEL);
			tis.setTitleText(this.antrunner.leftpanelHolder.getTitleAt(idx));
			tis.setIconText("");
			tis.setVisible(true);
			if (tis.getResult()==TitleAndIconSelection.RESULT_OK) {
				this.antrunner.leftpanelHolder.setTitleAt(idx,tis.getTitleText());	
				AntRunnerPanel ap=(AntRunnerPanel)this.antrunner.leftpanelHolder.getComponentAt(idx);
				ap.setIconName(tis.getIconText());
				this.antrunner.leftpanelHolder.setIconAt(idx, ap.getIcon());
				this.antrunner.configurationChanged();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
