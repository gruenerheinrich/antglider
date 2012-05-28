package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerConsole;

public class NodeMoveBackwardAction extends AntRunnerAction {
	
	public NodeMoveBackwardAction() {
		super("Move Backward",AntRunner.getResourceImageIcon("arrow_up.png"));
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.getCurrentPanel().moveCurrentElement(-1);
	}

}
