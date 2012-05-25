package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerConsole;

public class BatchFrameAction extends AntRunnerAction {
	
	public BatchFrameAction() {
		super("Batch View Frame");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.antrunner.batchview.setModus(AntRunnerConsole.MODE_FRAME);
		} catch (Exception e1) {

		}
	}

}
