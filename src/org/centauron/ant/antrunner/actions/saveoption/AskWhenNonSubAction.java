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

public class AskWhenNonSubAction extends AntRunnerAction {
	
	public AskWhenNonSubAction() {
		super("Relative Filepath");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Save relative filepaths when file resides under config or batch-file. Otherwise Ask.");
		this.putValue(LONG_DESCRIPTION, "Save relative filepaths when file resides under config or batch-file. Otherwise Ask.");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.setSaveModus(AntRunner.SAVEMODUS_ASKWHENNONSUB);
	}

}
