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
import org.centauron.utility.Utility;

public class SaveAsBatchFileAction extends AntRunnerAction {
	
	public SaveAsBatchFileAction() {
		super("Save File As...",AntRunner.getResourceImageIcon("script_save.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Save current config file.");
		this.putValue(LONG_DESCRIPTION, "Save current config file.");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(this.antrunner.getLastFile());
		FileFilter filter=new FileNameExtensionFilter("Ant BuildFiles(*.xml)", "xml"); 		
		fc.setFileFilter(filter);					
		int ret=fc.showSaveDialog(this.antrunner);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
            	if (!Utility.hasFileExtension(file)) {
            		file=new File(file.getAbsolutePath()+".xml");
            	}
            	this.antrunner.batchview.saveFile(file);
			} catch (Exception e1) {
				e1.printStackTrace();
				//ERROR OPENING
				JOptionPane.showMessageDialog(this.antrunner, "Error Saving File","AntRunner",JOptionPane.ERROR_MESSAGE);
			}
		}        
	}

}
