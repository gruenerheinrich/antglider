package org.centauron.ant.antrunner;

import java.awt.Component;

import javax.swing.JButton;

public class AntRunnerButton extends JButton	 {
	public AntRunnerButton(AntRunnerElement el) {
		super(el.getShortCaption());
	}
}
