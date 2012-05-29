package org.centauron.ant.antrunner;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import org.centauron.ant.antrunner.actions.AntRunnerAction;
import org.centauron.ant.antrunner.actions.shortcut.ShortCutAction;

public class AntRunnerActionFactory {
	private  Hashtable<String,AntRunnerAction> m_constructed=new Hashtable();
	private AntRunner antrunner;
	public AntRunnerActionFactory(AntRunner ar) {
		this.antrunner=ar;
	}
	public  AntRunnerAction getActionForName(String nn) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AntRunnerAction ret=m_constructed.get(nn);
		if (ret!=null) return ret;
		Class c=Class.forName("org.centauron.ant.antrunner.actions."+nn);
		ret=(AntRunnerAction)c.getConstructor().newInstance();
		ret.antrunner=this.antrunner;
		m_constructed.put(nn,ret);
		return ret;
	}
	public JButton getSmallButtonForAction(String string) throws Exception {
		JButton butt=new JButton(this.getActionForName(string));
		butt.setText("");
		butt.getInsets().right=2;
		butt.getInsets().left=2;
		return butt;
	}	
	
	public ShortCutAction getShortCutAction(AntRunnerNode el,Icon icon, String title) {
		ShortCutAction ret=new ShortCutAction(icon,title);
		ret.antrunner=this.antrunner;
		ret.setNode(el);
		return ret;
	}
}
