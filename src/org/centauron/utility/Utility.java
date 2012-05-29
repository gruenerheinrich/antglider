package org.centauron.utility;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.centauron.ant.antrunner.AntRunnerBatchView;
import org.centauron.ant.antrunner.AntRunnerNode;

public class Utility {
	public static boolean wildCardMatch(String text, String pattern)
    {
        // Create the cards by splitting using a RegEx. If more speed 
        // is desired, a simpler character based splitting can be done.
        String [] cards = pattern.split("\\*");
 
        // Iterate over the cards.
        for (String card : cards)
        {
            int idx = text.indexOf(card);
 
            // Card not detected in the text.
            if(idx == -1)
            {
                return false;
            }
 
            // Move ahead, towards the right of the text.
            text = text.substring(idx + card.length());
        }
 
        return true;
    }
	public static Enumeration saveExpansionState(JTree tree) {

        return tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));

    }


    public static void loadExpansionState(JTree tree, Enumeration enumeration) {

        if (enumeration != null) {

            while (enumeration.hasMoreElements()) {

                TreePath treePath = (TreePath) enumeration.nextElement();

                tree.expandPath(treePath);

            }

        }

    }
	public static boolean isContainerChild(JComponent mainpane,JComponent child) {
		for (int i=0;i<mainpane.getComponentCount();i++) {
			if (mainpane.getComponent(i).equals(child)) return true;
		}
		return false;
	}
	public static void setButtonGroupIndex(ButtonGroup consolegroup,int idx) {
		int i=0;
		Enumeration<AbstractButton> e=consolegroup.getElements();
		while (e.hasMoreElements()) {
			AbstractButton b=e.nextElement();
			if (i==idx) {
				consolegroup.setSelected(b.getModel(), true);				
			}
			++i;
		}
	}
	public static int getTabIndex(JTabbedPane tabby,Component child) {
		for (int i=0;i<tabby.getTabCount();i++) {
			if (tabby.getComponentAt(i).equals(child)) {
				return i;
			}
		}
		return -1;
	}
	public static int getComponentIndex(JComponent tabby,Component child) {
		for (int i=0;i<tabby.getComponentCount();i++) {
			if (tabby.getComponent(i).equals(child)) {
				return i;
			}
		}
		return -1;
	}	
	public static int getListModelIndex(DefaultListModel model, Object o) {
		for (int i=0;i<model.getSize();i++) {
			if (model.get(i).equals(o)) return i;
		}
		return -1;
	}
	public static boolean hasFileExtension(File file) {
		return (file.getAbsolutePath().lastIndexOf(".")!=-1);

	}
	public static Vector<Component> getVectorFromContainer(Container panel) {
		Vector<Component> v=new Vector();
		for (int i=0;i<panel.getComponentCount();i++) {
			v.add(panel.getComponent(i));
		}
		return v;
	}
	public static void putVectorToConainer(Container panel,Vector<Component> v) {
		for (Component c:v) {
			panel.add(c);
		}
	}
}
