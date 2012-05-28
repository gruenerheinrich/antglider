package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerBatchView;
import org.centauron.ant.antrunner.AntRunnerConsole;
import org.centauron.ant.antrunner.AntRunnerNode;

public class BatchAddSelectedAction extends AntRunnerAction {
	
	public BatchAddSelectedAction() {
		super("Add Element to Batch",AntRunner.getResourceImageIcon("script_go.png"));
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		AntRunnerNode node=this.antrunner.getCurrentPanel().getCurrentElement();
		AntRunnerNode newnode=(AntRunnerNode)node.clone();
		this.antrunner.batchview.addElement(newnode);
	}

	public void checkEnabled() {
		if (this.antrunner.batchview==null) return;
		if (this.antrunner.getCurrentPanel()==null) return;
		if (this.antrunner.batchview.getModus()!=AntRunnerBatchView.MODE_HIDE) {
			if (this.antrunner.getCurrentPanel().getCurrentElement()!=null) {
				if (this.antrunner.getCurrentPanel().getCurrentElement().isBatchAddable()) {
					super.setEnabled(true);
					return;
				}
			}
		}
		super.setEnabled(false);
	}
	
}
