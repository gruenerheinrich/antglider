package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerConsole;

public class ConsoleAttachAction extends AntRunnerAction {
	
	public ConsoleAttachAction() {
		super("Attach Console");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.console.setModus(AntRunnerConsole.MODE_ATTACH);
	}

}
