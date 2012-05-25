package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerConsole;

public class BatchMoveForwardAction extends AntRunnerAction {
	
	public BatchMoveForwardAction() {
		super("Move Forward",AntRunner.getResourceImageIcon("arrow_down.png"));
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.batchview.moveCurrentElement(1);
	}

}
