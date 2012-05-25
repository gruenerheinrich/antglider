package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerPanel;



public class AddFileAction extends AntRunnerAction {
	public AddFileAction() {
		super("Add Ant Buildfile...",AntRunner.getResourceImageIcon("add_co.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a single buildfile to current panel");
		this.putValue(LONG_DESCRIPTION, "Add a single buildfile to current panel");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(this.antrunner.getLastFile());
		FileFilter filter=new FileNameExtensionFilter("Ant BuildFiles (*.xml)", "xml"); 
		fc.setFileFilter(filter);
		int ret=fc.showOpenDialog(this.antrunner);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            this.antrunner.setLastFile(file);
            try {
        		AntRunnerPanel ap=this.antrunner.getCurrentPanel();
        		ap.addBuildFile(file);
			} catch (Exception e1) {
				//ERROR OPENING
				JOptionPane.showMessageDialog(this.antrunner, "Error adding File","AntRunner",JOptionPane.ERROR_MESSAGE);
			}
		}        
		
		
	}
}
