package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TitleAndIconSelection extends JDialog implements ActionListener {
	public static final int MODE_ADDPANEL = 1;
	public static final int MODE_EDITPANEL = 2;
	public static final int MODE_ADDSHORTCUT = 3;
	public static final int MODE_EDITSHORTCUT = 4;
	public static final int RESULT_OK = 0;
	public static final int RESULT_CANCEL = 1;
	private AntRunner antrunner;
	private JTextField titleText;
	private JTextField iconText;
	private boolean m_editmode;
	private JPanel pane;
	private JPanel buttonpane;
	private int m_result;
	public TitleAndIconSelection(AntRunner ar) throws Exception {
		this.antrunner=ar;
		pane=new JPanel(new BorderLayout());
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
		p2.add("East",getButton("..."));
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
			buttonpane.add(getButton("Edit"));
		} else {
			buttonpane.add(getButton("Add"));
		}
		buttonpane.add(getButton("Cancel"));		
		pane.add("South",buttonpane);
	
	}

	private JButton getButton(String string) {
		JButton b=new JButton(string);
		b.addActionListener(this);
		return b;
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
	
	public void setMode(int mode) throws Exception {
		switch (mode) {
			case TitleAndIconSelection.MODE_ADDPANEL:
				this.m_editmode=false;
				this.setTitle("Add new Tab");
				this.insertButtonPanelByEditType();
				this.setTitleText("");
				this.setIconText("");				
				break;
			case TitleAndIconSelection.MODE_EDITPANEL:
				this.m_editmode=true;
				this.setTitle("Edit Tab");
				this.insertButtonPanelByEditType();
				break;
			case TitleAndIconSelection.MODE_ADDSHORTCUT:
				this.m_editmode=false;
				this.setTitle("Add new Shortcut");
				this.insertButtonPanelByEditType();
				this.setTitleText("");
				this.setIconText("");					
				break;
			case TitleAndIconSelection.MODE_EDITSHORTCUT:
				this.m_editmode=true;
				this.setTitle("Edit Shortcut");
				this.insertButtonPanelByEditType();
				break;				
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text=((JButton)e.getSource()).getText();
		if (text.equalsIgnoreCase("Add") || text.equalsIgnoreCase("Edit")) {
			m_result=TitleAndIconSelection.RESULT_OK;
			this.setVisible(false);
		}
		if (text.equalsIgnoreCase("Cancel")) {
			m_result=TitleAndIconSelection.RESULT_CANCEL;
			this.setVisible(false);
		}		
		if (text.equalsIgnoreCase("...")) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(this.antrunner.getLastFile());
			FileFilter filter=new FileNameExtensionFilter("Image Files (*.*)", "*"); 
			fc.setFileFilter(filter);
			int ret=fc.showOpenDialog(this.antrunner);
			if (ret == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            this.antrunner.setLastFile(file);
	            this.setIconText(file.getPath());
			}        
			
		}
	}

	public int getResult() {
		return m_result;
	}
}
