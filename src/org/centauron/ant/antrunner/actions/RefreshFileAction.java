package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.centauron.ant.antrunner.AntRunnerNode;
import org.centauron.ant.antrunner.AntUtils;
import org.centauron.ant.antrunner.BuildFileInfo;

public class RefreshFileAction extends AntRunnerAction {
	
	public RefreshFileAction() {
		super("Refresh");
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Open a config file");
		this.putValue(LONG_DESCRIPTION, "Open a config file");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//RELOAD ME TO GET TARGT LIST
		try {
			AntRunnerNode nn=this.antrunner.getCurrentPanel().getCurrentElement();
			nn.removeAllChildren();
			File file=nn.getBuildFile();
			BuildFileInfo bi=AntUtils.getBuildFileInfo(file);
			this.antrunner.getCurrentPanel().insertTargetChildren(nn, bi, file);
			this.antrunner.getCurrentPanel().reloadTree();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
