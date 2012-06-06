package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;


public class AntRunnerConsole extends JPanel implements BuildListener {
	public static final int MODE_HIDE = 0;
	public static final int MODE_ATTACH = 1;
	public static final int MODE_FRAME = 2;
	private AntRunner m_runner;
	private JTextArea m_textarea;
	public JFrame m_frame;
	private int m_modus;
	public AntRunnerConsole(AntRunner runner) throws Exception {
		super(new BorderLayout(5,5));
		m_runner=runner;
		m_frame=new JFrame("Console");
		m_frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub				
			}			
		});
		m_frame.setIconImage(AntRunner.getResourceImageIcon("console.png").getImage());
		m_frame.getContentPane().setLayout(new BorderLayout(5,5));
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
		JButton cleanConsole=new JButton(m_runner.getFactory().getActionForName("ConsoleCleanAction"));
		//buttonPanel.add(cleanConsole);
		this.add("North",buttonPanel);
		m_textarea=new JTextArea();
		m_textarea.setEditable(false);
		m_textarea.setAutoscrolls(true);
		//this.add("Center",m_textarea);
		setScrollPolicy(true);
		this.add("Center",new JScrollPane(m_textarea));
	}
	
	

	
	public void buildFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void buildStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void messageLogged(BuildEvent arg0) {
		
		this.setMessage(arg0.getMessage());
	}

	public void targetFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void targetStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void taskFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void taskStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}





	public void setModus(int mode) {
		m_modus=mode;
		this.m_runner.rightpanelHolder.remove(this);
		//VALIDATE
		m_frame.setVisible(false);
		m_frame.remove(this);
		if (mode==AntRunnerConsole.MODE_HIDE) {
			//NOTHING
		}
		if (mode==AntRunnerConsole.MODE_ATTACH) {
			this.m_runner.rightpanelHolder.addTab("Console", AntRunner.getResourceImageIcon("console.png"),this);
		}		
		if (mode==AntRunnerConsole.MODE_FRAME) {
			m_frame.getContentPane().add("Center",this);
			m_frame.setVisible(true);
		}			
		this.m_runner.validateRightPanel();		
	}


	public void setScrollPolicy(boolean b) {
		DefaultCaret caret = (DefaultCaret)m_textarea.getCaret();  
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  		
	}


	public void setMessage(String string) {
		// TODO Auto-generated method stub
		m_textarea.append(string+System.getProperty("line.separator"));
	}




	public int getModus() {
		return m_modus;
	}

}
