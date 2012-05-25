package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SaveConfigFileAction extends AntRunnerAction {
	
	public SaveConfigFileAction() {
		super("Save File");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Save current config file.");
		this.putValue(LONG_DESCRIPTION, "Save current config file.");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			this.antrunner.saveConfigFile(null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this.antrunner, "Error saving File.","AntRunner",JOptionPane.ERROR_MESSAGE);
		}
	}

}
