package org.centauron.ant.antrunner.actions.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.centauron.ant.antrunner.TitleAndIconSelection;
import org.centauron.ant.antrunner.actions.AntRunnerAction;



public class RemoveShortCutAction extends AntRunnerAction {
	public RemoveShortCutAction() {
		super("Remove ShortCut");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a new Tab");
		this.putValue(LONG_DESCRIPTION, "Add a new Tab");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			this.antrunner.removeCurrentShortCut();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
