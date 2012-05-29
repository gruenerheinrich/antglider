package org.centauron.ant.antrunner.actions.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerPanel;
import org.centauron.ant.antrunner.TitleAndIconSelection;
import org.centauron.ant.antrunner.actions.AntRunnerAction;

public class AddShortCutAction extends AntRunnerAction {
	
	public AddShortCutAction() {
		super("Add As Shortcut",AntRunner.getResourceImageIcon("shortcut_add.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Adding a dir and subdirs to current panel");
		this.putValue(LONG_DESCRIPTION, "Adding a dir and subdirs to current panel");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			TitleAndIconSelection tis=this.antrunner.getTabForm();
			tis.setMode(TitleAndIconSelection.MODE_ADDSHORTCUT);
			tis.setVisible(true);
			if (tis.getResult()==TitleAndIconSelection.RESULT_OK) {
				this.antrunner.addShortCut(this.antrunner.getCurrentPanel().getCurrentElement(),tis.getIconText(),tis.getTitleText());		
				this.antrunner.revalidate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}			
	}

}
