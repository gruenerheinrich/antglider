package org.centauron.ant.antrunner.actions.panel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.TitleAndIconSelection;
import org.centauron.ant.antrunner.actions.AntRunnerAction;



public class AddPanelAction extends AntRunnerAction {
	public AddPanelAction() {
		super("Add Tab...",AntRunner.getResourceImageIcon("tab_add.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a new Tab");
		this.putValue(LONG_DESCRIPTION, "Add a new Tab");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			TitleAndIconSelection tis=this.antrunner.getTabForm();
			tis.setMode(TitleAndIconSelection.MODE_ADDPANEL);
			tis.setVisible(true);
			if (tis.getResult()==TitleAndIconSelection.RESULT_OK) {
				this.antrunner.addPanel(tis.getIconText(), tis.getTitleText());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
