package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerPanel;



public class RemoveNodeAction extends AntRunnerAction {
	public RemoveNodeAction() {
		super("Remove Element",AntRunner.getResourceImageIcon("remove_co.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a single buildfile to current panel");
		this.putValue(LONG_DESCRIPTION, "Add a single buildfile to current panel");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//ASK??
		this.antrunner.getCurrentPanel().getRootNode().remove(this.antrunner.getCurrentPanel().getCurrentElement());
		DefaultTreeModel model = (DefaultTreeModel) this.antrunner.getCurrentPanel().tree.getModel();
		this.antrunner.configurationChanged();
		model.reload();
	}
}
