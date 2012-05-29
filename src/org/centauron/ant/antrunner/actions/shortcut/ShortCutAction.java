package org.centauron.ant.antrunner.actions.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.centauron.ant.antrunner.AntRunnerNode;
import org.centauron.ant.antrunner.AntRunnerPanel;
import org.centauron.ant.antrunner.actions.AntRunnerAction;

public class ShortCutAction extends AntRunnerAction {

	private AntRunnerNode m_el;
	private File m_iconfile;
	public ShortCutAction() {
		super("");
	}

	public ShortCutAction(Icon icon, String title) {
		super(title,icon);
	}
	


	public void setNode(AntRunnerNode el) {
		m_el=el;
	}
	public AntRunnerNode getNode() {
		return m_el;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			m_el.selectNode();
			this.antrunner.startAntRunnerNode(m_el);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public File getIconFile() {
		return m_iconfile;
	}

	public void setIconFile(File f) {
		m_iconfile=f;
		
	}	
}
