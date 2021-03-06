package org.centauron.ant.antrunner.actions.panel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.TitleAndIconSelection;
import org.centauron.ant.antrunner.actions.AntRunnerAction;



public class MoveForwardPanelAction extends AntRunnerAction {
	public MoveForwardPanelAction() {
		super("Move to Front",AntRunner.getResourceImageIcon("arrow_down.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a new Tab");
		this.putValue(LONG_DESCRIPTION, "Add a new Tab");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			this.antrunner.moveCurrentTab(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
