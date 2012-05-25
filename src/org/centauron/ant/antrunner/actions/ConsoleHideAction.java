package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerConsole;

public class ConsoleHideAction extends AntRunnerAction {
	
	public ConsoleHideAction() {
		super("Hide Console");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.console.setModus(AntRunnerConsole.MODE_HIDE);
	}

}
