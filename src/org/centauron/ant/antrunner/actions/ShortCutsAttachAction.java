package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerConsole;

public class ShortCutsAttachAction extends AntRunnerAction {
	
	public ShortCutsAttachAction() {
		super("Attach Batch View");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.antrunner.batchview.setModus(AntRunnerConsole.MODE_ATTACH);
		} catch (Exception e1) {

		}
	}

}
