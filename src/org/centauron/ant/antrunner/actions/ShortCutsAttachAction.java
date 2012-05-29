package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerConsole;

public class ShortCutsAttachAction extends AntRunnerAction {
	
	public ShortCutsAttachAction() {
		super("Show Shortcuts");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.antrunner.setShortCutMode(AntRunner.SHORTCUTMODE_DISPLAY);
		} catch (Exception e1) {

		}
	}

}
