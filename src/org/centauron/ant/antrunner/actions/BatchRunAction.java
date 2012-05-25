package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerNode;
import org.centauron.ant.antrunner.AntUtils;

public class BatchRunAction extends AntRunnerAction {
	private boolean m_running=false;
	public BatchRunAction() {
		super("Run Batch",AntRunner.getResourceImageIcon("script_run.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Run this Target");
		this.putValue(LONG_DESCRIPTION, "Run this Target");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Thread t=new Thread() {
			public void run() {
				m_running=true;
				setEnabled(false);				
				DefaultListModel ml=antrunner.batchview.getListModel();
				Vector<AntRunnerNode> ll=new Vector();
				for (int i=0;i<ml.size();i++) {
					ll.add((AntRunnerNode)ml.get(i));
				}
				Enumeration<AntRunnerNode> e=ll.elements();
				while (e.hasMoreElements() && m_running) {
					AntRunnerNode ar=e.nextElement();
					AntUtils.startForNode(ar,true);
				}
				setEnabled(true);
			}
		};
		t.start();
	}
	public void setRunning(boolean b) {
		m_running=false;
	}

}
