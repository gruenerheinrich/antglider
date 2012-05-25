package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenConfigFileAction extends AntRunnerAction {
	
	public OpenConfigFileAction() {
		super("Open Config File...");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Open a config file");
		this.putValue(LONG_DESCRIPTION, "Open a config file");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(this.antrunner.getLastFile());
		FileFilter filter=new FileNameExtensionFilter("AntRunner Config-Files(*.xml)", "xml"); 		
		fc.setFileFilter(filter);
		int ret=fc.showOpenDialog(this.antrunner);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
				this.antrunner.initConfigFile(file.getAbsolutePath());
			} catch (Exception e1) {
				//ERROR OPENING
				JOptionPane.showMessageDialog(this.antrunner, "Error opening File","AntRunner",JOptionPane.ERROR_MESSAGE);
			}
		}        
	}

}
