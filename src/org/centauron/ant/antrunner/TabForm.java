package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TabForm extends JDialog {
	private AntRunner antrunner;
	private JTextField titleText;
	private JTextField iconText;
	private boolean m_editmode;
	private JPanel pane;
	private JPanel buttonpane;
	public TabForm(AntRunner ar) throws Exception {
		this.antrunner=ar;
		pane=new JPanel(new BorderLayout());
		
		this.setTitle("Adding a new Tab");
		this.setResizable(false);
		JPanel innerpane=new JPanel(new GridLayout(2,1,5,5));
		innerpane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		JPanel p1=new JPanel(new BorderLayout(5,5));
		JLabel label = new JLabel("Title:");
		p1.add("West",label);
		titleText = new JTextField();
		titleText.setBorder(BorderFactory.createEtchedBorder());
		p1.add("Center",titleText);
		innerpane.add(p1);

		JPanel p2=new JPanel(new BorderLayout(5,5));
		label = new JLabel("Icon:");
		p2.add("West",label);
		iconText = new JTextField();
		iconText.setBorder(BorderFactory.createEtchedBorder());
		p2.add("Center",iconText);
		innerpane.add(p2);
		
			
		pane.add("Center",innerpane);
		insertButtonPanelByEditType();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add("Center",pane);
		this.pack();
		this.setSize(300, 140);
	}
	
	private void insertButtonPanelByEditType() throws Exception {
		if (buttonpane!=null) {
			pane.remove(buttonpane);
		}
		buttonpane=new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		if (this.m_editmode) {
			buttonpane.add(new JButton(this.antrunner.getFactory().getActionForName("TabFormEditAction")));
		} else {
			buttonpane.add(new JButton(this.antrunner.getFactory().getActionForName("TabFormAddAction")));
		}
		buttonpane.add(new JButton(this.antrunner.getFactory().getActionForName("TabFormCancelAction")));		
		pane.add("South",buttonpane);
	
	}

	public String getIconText() {
		return iconText.getText();
	}
	public String getTitleText() {
		return titleText.getText();
	}
	public void setTitleText(String string) {
		titleText.setText(string);
	}

	public void setIconText(String string) {
		iconText.setText(string);
	}

	public void setEditMode(boolean b) throws Exception {
		m_editmode=b;
		if (b) {
			this.setTitle("Edit Tab");
		} else {
			this.setTitle("Adding a new Tab");
		}
		insertButtonPanelByEditType();
	}
}
