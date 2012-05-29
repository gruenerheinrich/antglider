package org.centauron.utility;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

public class PopupMenuAdapter extends MouseAdapter {
	private JPopupMenu m_menu;

	public PopupMenuAdapter(JPopupMenu menu) {
		m_menu=menu;
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON3) {
			m_menu.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}
	public static void attachPopup(JComponent c,JPopupMenu panelpopup) {
		c.addMouseListener(new PopupMenuAdapter(panelpopup));
	}	
}
