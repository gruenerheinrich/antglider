package org.centauron.ant.antrunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.swing.Action;

import org.centauron.ant.antrunner.actions.AntRunnerAction;

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
}
