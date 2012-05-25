package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerConsole;

public class BatchRemoveSelectedAction extends AntRunnerAction {
	
	public BatchRemoveSelectedAction() {
		super("Remove Element from Batch",AntRunner.getResourceImageIcon("script_delete.png"));
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.batchview.removeCurrentElement();
	}

}
