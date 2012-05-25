package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerConsole;

public class ConsoleFrameAction extends AntRunnerAction {
	
	public ConsoleFrameAction() {
		super("Console Frame");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.console.setModus(AntRunnerConsole.MODE_FRAME);
	}

}
