package org.centauron.ant.antrunner.actions.saveoption;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerPanel;
import org.centauron.ant.antrunner.actions.AntRunnerAction;

public class AlwaysAbsoluteAction extends AntRunnerAction {
	
	public AlwaysAbsoluteAction() {
		super("Ask when not sub-dir");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Always save absolute filepaths");
		this.putValue(LONG_DESCRIPTION, "Always save absolute filepaths");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.setSaveModus(AntRunner.SAVEMODUS_ALWAYSABSOLUTE);
	}

}
