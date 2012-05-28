package org.centauron.ant.antrunner.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.centauron.ant.antrunner.AntRunner;

public class AntRunnerAction extends AbstractAction {

	public AntRunner antrunner;
	public AntRunnerAction(String name) {
		super(name);
	}
	public AntRunnerAction(String name,Icon icon) {
		super(name,icon);
	}	
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO NOT FOUND 
	}
	
	public void setAntRunner(AntRunner a) {
		antrunner=a;
	}
	public void fire() {
	  ActionEvent e=new ActionEvent(this,0,this.ACTION_COMMAND_KEY);
	  this.actionPerformed(e);	
	}
	public void checkEnabled() {
			
	}

}
