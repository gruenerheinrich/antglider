package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.centauron.ant.antrunner.AntRunner;

public class NewBatchFileAction extends AntRunnerAction {
	
	public NewBatchFileAction() {
		super("New File...");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Open a config file");
		this.putValue(LONG_DESCRIPTION, "Open a config file");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.antrunner.batchview.newFile();
	}

}
