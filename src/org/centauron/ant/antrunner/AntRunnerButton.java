package org.centauron.ant.antrunner;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;

public class AntRunnerButton extends JButton	 {
	private AntRunnerNode node;

	public AntRunnerButton(AntRunnerNode el,Icon icon) {
		super(icon);
		this.insets().set(3, 3, 3, 3);
		node=el;
		this.setToolTipText("<html>" + node.getBuildFile().getPath() + "<br>"+ node.getTargetName()+"</html>");
	}
}
