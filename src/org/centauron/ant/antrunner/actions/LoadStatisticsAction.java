package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;

public class LoadStatisticsAction extends AntRunnerAction {
	
	public LoadStatisticsAction() {
		super("Load Statistics");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Adding a dir and subdirs to current panel");
		this.putValue(LONG_DESCRIPTION, "Adding a dir and subdirs to current panel");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
