package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerNode;

public class BatchRunSingleAction extends AntRunnerAction {
	
	public BatchRunSingleAction() {
		super("Run Current",AntRunner.getResourceImageIcon("run_tool.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Run this Target");
		this.putValue(LONG_DESCRIPTION, "Run this Target");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		AntRunnerNode ar=this.antrunner.batchview.getCurrentElement();
		if (ar!=null) {
			this.antrunner.startAntRunnerNode(ar);
		}
	}

}
