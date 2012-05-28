package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerConsole;

public class NodeMoveForwardAction extends AntRunnerAction {
	
	public NodeMoveForwardAction() {
		super("Move Forward",AntRunner.getResourceImageIcon("arrow_down.png"));
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.getCurrentPanel().moveCurrentElement(1);
	}

}
