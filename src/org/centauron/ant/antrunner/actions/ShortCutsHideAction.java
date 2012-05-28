package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerConsole;

public class ShortCutsHideAction extends AntRunnerAction {
	
	public ShortCutsHideAction() {
		super("Hide Batch View");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.antrunner.batchview.setModus(AntRunnerConsole.MODE_HIDE);
		} catch (Exception e1) {

		}
	}

}
