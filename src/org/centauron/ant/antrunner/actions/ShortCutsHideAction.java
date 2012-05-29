package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerConsole;

public class ShortCutsHideAction extends AntRunnerAction {
	
	public ShortCutsHideAction() {
		super("Hide Shortcuts");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.antrunner.setShortCutMode(AntRunner.SHORTCUTMODE_HIDE);
		} catch (Exception e1) {

		}
	}

}
