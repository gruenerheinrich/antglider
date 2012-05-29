package org.centauron.ant.antrunner;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolTip;

import org.centauron.ant.antrunner.actions.shortcut.ShortCutAction;
import org.centauron.utility.PopupMenuAdapter;

public class AntRunnerButton extends JButton	 {

	private JPopupMenu shortcutpopup;

	public AntRunnerButton(ShortCutAction ac) throws Exception {
		super(ac);
		this.insets().set(3, 3, 3, 3);
		this.setToolTipText("<html>" + getNode().getBuildFile().getPath() + "<br>"+ getNode().getTargetName()+"</html>");

		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				try {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
					setCurrent();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		PopupMenuAdapter.attachPopup(this,getPopup());
	}
	
	public void setCurrent() throws Exception {
		getNode().getAntRunner().setCurrentShortCut(this);
	}
	private AntRunnerActionFactory getFactory() {
		return getNode().getAntRunner().getFactory();
	}
	private JPopupMenu getPopup() throws Exception {
		if (shortcutpopup==null) {
			shortcutpopup=new JPopupMenu();
			shortcutpopup.add(getFactory().getActionForName("shortcut.EditShortCutAction"));
			shortcutpopup.add(getFactory().getActionForName("shortcut.RemoveShortCutAction"));
			shortcutpopup.addSeparator();
			shortcutpopup.add(getFactory().getActionForName("shortcut.MoveBackwardShortCutAction"));
			shortcutpopup.add(getFactory().getActionForName("shortcut.MoveForwardShortCutAction"));
		}
		return shortcutpopup;
	}

	public AntRunnerNode getNode() {
		return ((ShortCutAction)this.getAction()).getNode();
	}
	public JToolTip createToolTip() {
        //SELECT MY NODE
		getNode().getAntRunner().selectAndDisplayNode(getNode());
        return super.createToolTip();
    }

	public File getIconFile() {
		return ((ShortCutAction)this.getAction()).getIconFile();
	}

	public void setIconName(String iconText) {
	}



}
