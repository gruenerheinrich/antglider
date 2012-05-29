package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.centauron.ant.antrunner.AntRunner;

public class SaveAsConfigFileAction extends AntRunnerAction {
	
	public SaveAsConfigFileAction() {
		super("Save File As...");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Save current config file.");
		this.putValue(LONG_DESCRIPTION, "Save current config file.");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(this.antrunner.getLastFile());
		FileFilter filter=new FileNameExtensionFilter("AntRunner Config-Files(*.xml)", "xml"); 		
		fc.setFileFilter(filter);		
		int ret=fc.showSaveDialog(this.antrunner);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
				this.antrunner.saveConfigFile(file.getPath());
			} catch (Exception e1) {
				//ERROR OPENING
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this.antrunner, "Error saving File",AntRunner.applicatonName,JOptionPane.ERROR_MESSAGE);
			}
		}        
	}

}
