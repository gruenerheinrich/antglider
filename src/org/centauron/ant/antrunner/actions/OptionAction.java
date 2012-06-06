package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.centauron.ant.antrunner.AntRunnerOptions;

public class OptionAction extends AntRunnerAction {
	private AntRunnerOptions ad;
	public OptionAction() {
		super("Options");
		this.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			if (ad==null) {
				ad=new AntRunnerOptions(this.antrunner);
			}
			ad.setLocation(this.antrunner.getX()+ (this.antrunner.getWidth()-ad.getWidth())/2 , this.antrunner.getY());
			ad.setVisible(true);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
