package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerPanel;

public class AddDirAction extends AntRunnerAction {
	
	public AddDirAction() {
		super("Add Dir...",AntRunner.getResourceImageIcon("folder_add.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Adding a dir and subdirs to current panel");
		this.putValue(LONG_DESCRIPTION, "Adding a dir and subdirs to current panel");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(this.antrunner.getLastFile());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ret=fc.showOpenDialog(this.antrunner);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            this.antrunner.setLastFile(file);
            try {
        		AntRunnerPanel ap=this.antrunner.getCurrentPanel();
        		ap.addDir(file,"*.xml");
			} catch (Exception e1) {
				//ERROR OPENING
				JOptionPane.showMessageDialog(this.antrunner, "Error adding File",AntRunner.applicatonName,JOptionPane.ERROR_MESSAGE);
			}
		}  	
		
	}

}
