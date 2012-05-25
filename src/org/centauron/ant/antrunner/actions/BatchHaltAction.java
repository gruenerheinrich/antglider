package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerNode;
import org.centauron.ant.antrunner.AntUtils;

public class BatchHaltAction extends AntRunnerAction {
	
	public BatchHaltAction() {
		super("Stop Batch",AntRunner.getResourceImageIcon("stop.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Run this Target");
		this.putValue(LONG_DESCRIPTION, "Run this Target");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			((BatchRunAction)antrunner.getFactory().getActionForName("BatchRunAction")).setRunning(false);
		} catch (Exception e1) {
		}
	}

}
