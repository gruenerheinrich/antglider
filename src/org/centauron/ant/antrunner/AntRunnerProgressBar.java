package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class AntRunnerProgressBar extends JPanel  {
	private static final double TIME_10MINUTES = 600;
	private static final double TIME_1MINUTE = 60;
	private AntRunnerNode m_info;
	private boolean m_running;
	private JProgressBar pro;
	public AntRunnerProgressBar(AntRunnerNode info) {
		super();
		m_info=info;
		this.setLayout(new BorderLayout());
		pro=new JProgressBar(0,100);
		pro.setString(formatDuration(info.getCurrentDuration()));
		pro.setStringPainted(true);
		if (info.isInOverTime()) {
			pro.setForeground(new Color(80,0,0));
		}
		pro.setValue(info.getCurrentPercent());
		this.add("Center",pro);
		this.setSize(100,20);
	}
	private String formatDuration(double currentDuration) {
		int intDuration=(int)currentDuration;
		if (currentDuration>TIME_10MINUTES) {
			int minutes=(int)Math.floor(currentDuration/60);
			int hours=(int)Math.floor(minutes/60);
			minutes=minutes-60*hours;
			return Integer.toString(hours)+":"+Integer.toString(minutes)+" h";
			
		} else {
			if (currentDuration>TIME_1MINUTE) {
				int minutes=(int)Math.floor(currentDuration/60);
				int sec=intDuration-minutes*60;
				return Integer.toString(minutes)+" min "+Integer.toString(sec)+" sec";
			} else {
				return Integer.toString(intDuration)+" sec";
			}
		}
	}
	public void stop() {
		m_running=false;
	}
}
