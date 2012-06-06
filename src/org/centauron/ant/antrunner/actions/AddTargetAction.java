package org.centauron.ant.antrunner.actions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.centauron.ant.antrunner.AntRunner;
import org.centauron.ant.antrunner.AntRunnerPanel;
import org.centauron.ant.antrunner.AntUtils;
import org.centauron.ant.antrunner.BuildFileInfo;
import org.centauron.utility.VerticalFlowLayout;



public class AddTargetAction extends AntRunnerAction implements PropertyChangeListener {
	private JPanel targetlist;

	public AddTargetAction() {
		super("Add Ant Target...",AntRunner.getResourceImageIcon("add_target.png"));
		this.setEnabled(true);
		this.putValue(SHORT_DESCRIPTION, "Add a single buildfile to current panel");
		this.putValue(LONG_DESCRIPTION, "Add a single buildfile to current panel");
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		FileFilter filter=new FileNameExtensionFilter("Ant BuildFiles (*.xml)", "xml"); 
		fc.setFileFilter(filter);
		JPanel bob=new JPanel(new BorderLayout(5,5));
		bob.add("North",new JLabel("Select Targets to add"));
		targetlist=new JPanel(new VerticalFlowLayout());
		targetlist.setEnabled(false);
		bob.add("Center",new JScrollPane(targetlist));
		bob.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		bob.setPreferredSize(new Dimension(200,50));
		fc.setAccessory(bob);
		fc.addPropertyChangeListener(this);
		fc.setCurrentDirectory(this.antrunner.getLastFile());
		int ret=fc.showOpenDialog(this.antrunner);
		if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            this.antrunner.setLastFile(file);
            try {
        		BuildFileInfo bi=AntUtils.getBuildFileInfo(file);
            	AntRunnerPanel ap=this.antrunner.getCurrentPanel();
        		for (int i=0;i<targetlist.getComponentCount();i++) {
        			JCheckBox cb=(JCheckBox)targetlist.getComponent(i);
        			if (cb.isSelected()) {
        				ap.addTarget(file, bi.name,cb.getText());
        			}
        		}
			} catch (Exception e1) {
				//ERROR OPENING
				JOptionPane.showMessageDialog(this.antrunner, "Error adding File",AntRunner.applicatonName,JOptionPane.ERROR_MESSAGE);
			}
		}        
		
		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
			
			File newFile = (File) evt.getNewValue();
			targetlist.removeAll();
			if(newFile != null) {
				try {
					BuildFileInfo bi=AntUtils.getBuildFileInfo(newFile);
					for (String targs:bi.targets) {
						JCheckBox cb=new JCheckBox(targs);
						if (targs.equalsIgnoreCase(bi.defaultTarget)) {
							cb.setSelected(true);
						}
						targetlist.add(cb);
						targetlist.validate();

					}
					targetlist.setEnabled(true);
				} catch (Exception e) {
					targetlist.setEnabled(false);
				}
			} else {
				targetlist.setEnabled(false);
			}
		}
	}
}
