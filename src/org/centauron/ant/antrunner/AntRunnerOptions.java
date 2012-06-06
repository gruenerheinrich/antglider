package org.centauron.ant.antrunner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;

import org.centauron.utility.Utility;
import org.centauron.utility.VerticalFlowLayout;

public class AntRunnerOptions extends JDialog implements ActionListener {
	private AntRunner antrunner;

	public AntRunnerOptions(AntRunner ar) throws Exception {
		this.antrunner=ar;	
		JPanel all=new JPanel(new BorderLayout(5,5));
		JPanel jp=new JPanel(new VerticalFlowLayout());

		JPanel holder=new JPanel(new BorderLayout());
		holder.setPreferredSize(new Dimension(400,400));
		
		jp.add(this.getConsoleOptions());
		jp.add(this.getSeparator());
		jp.add(this.getBatchOptions());
		jp.add(this.getSeparator());
		jp.add(this.getShortCutsOptions());
		jp.add(this.getSeparator());
		jp.add(this.getFileSaveOptions());
		
		jp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		holder.add("Center",new JScrollPane(jp));
		all.add("Center",holder);
		JPanel blist=new JPanel(new FlowLayout(FlowLayout.RIGHT,5,5));
		blist.add(this.getButton("Close"));
		
		all.add("South",blist);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add("Center",all);
		this.pack();
		this.setTitle("Optionen");
		this.setIconImage(this.antrunner.getIconImage());
		this.setResizable(false);
	}
	
	private Component getFileSaveOptions() throws Exception {
		JPanel saveopts=new JPanel(new BorderLayout());
		JPanel inpanel=new JPanel(new GridLayout(4,0,3,3));
		saveopts.add("North",getInfoLabel("File Path"));
		
		ButtonGroup savegroup = new ButtonGroup();
		JRadioButton item = new JRadioButton(getFactory().getActionForName("saveoption.AlwaysRelativeAction"));
		savegroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("saveoption.AlwaysAbsoluteAction"));
		savegroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("saveoption.AbsoluteWhenNonSubAction"));
		savegroup.add(item);
		inpanel.add(item);

		item = new JRadioButton(getFactory().getActionForName("saveoption.AskWhenNonSubAction"));
		savegroup.add(item);
		inpanel.add(item);

		
		Utility.setButtonGroupIndex(savegroup,this.antrunner.getSaveModus());
		
		inpanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3,3));
		
		saveopts.add("Center",inpanel);
		saveopts.setBorder(BorderFactory.createEmptyBorder(15, 15, 15,15));
		return saveopts;		
	}

	private JSeparator getSeparator() {
		JSeparator p=new JSeparator();
		return p;
	}

	private JPanel getConsoleOptions() throws Exception {
		
		JPanel consoleopts=new JPanel(new BorderLayout());
		JPanel inpanel=new JPanel(new GridLayout(3,0,3,3));
		consoleopts.add("North",getInfoLabel("Console"));
		
		ButtonGroup consolegroup = new ButtonGroup();
		JRadioButton item = new JRadioButton(getFactory().getActionForName("ConsoleHideAction"));
		consolegroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("ConsoleAttachAction"));
		consolegroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("ConsoleFrameAction"));
		consolegroup.add(item);
		inpanel.add(item);
		
		
		Utility.setButtonGroupIndex(consolegroup,this.antrunner.console.getModus());
		
		
		inpanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3,3));
		
		consoleopts.add("Center",inpanel);
		consoleopts.setBorder(BorderFactory.createEmptyBorder(15, 15, 15,15));
		return consoleopts;
	}

	private JPanel getBatchOptions() throws Exception {
		
		JPanel batchopts=new JPanel(new BorderLayout());
		JPanel inpanel=new JPanel(new GridLayout(3,0,3,3));
		batchopts.add("North",getInfoLabel("Batch View"));
		
		ButtonGroup batchgroup = new ButtonGroup();
		JRadioButton item = new JRadioButton(getFactory().getActionForName("BatchHideAction"));
		batchgroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("BatchAttachAction"));
		batchgroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("BatchFrameAction"));
		batchgroup.add(item);
		inpanel.add(item);
		
		Utility.setButtonGroupIndex(batchgroup,this.antrunner.batchview.getModus());
		
		
		inpanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3,3));
		
		batchopts.add("Center",inpanel);
		batchopts.setBorder(BorderFactory.createEmptyBorder(15, 15, 15,15));
		return batchopts;
	}	

	private JPanel getShortCutsOptions() throws Exception {
		
		JPanel shortopts=new JPanel(new BorderLayout());
		JPanel inpanel=new JPanel(new GridLayout(2,0,3,3));
		shortopts.add("North",getInfoLabel("Short Cuts"));		
		ButtonGroup shortgroup = new ButtonGroup();
		JRadioButton item = new JRadioButton(getFactory().getActionForName("ShortCutsHideAction"));
		shortgroup.add(item);
		inpanel.add(item);
		
		item = new JRadioButton(getFactory().getActionForName("ShortCutsAttachAction"));
		shortgroup.add(item);
		inpanel.add(item);
		
		Utility.setButtonGroupIndex(shortgroup,this.antrunner.getShortCutMode());
		
		
		inpanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3,3));
		
		shortopts.add("Center",inpanel);
		shortopts.setBorder(BorderFactory.createEmptyBorder(15, 15, 15,15));
		return shortopts;
	}	
	
	private JButton getButton(String string) {
		JButton b=new JButton(string);
		b.addActionListener(this);
		return b;
	}
	private Component getInfoLabel(String string) {
		JLabel ll=new JLabel(string);
		ll.setFont(new Font("Tahoma",Font.BOLD,12));
		return ll;
	}

	private AntRunnerActionFactory getFactory() {
		return this.antrunner.getFactory();
	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
