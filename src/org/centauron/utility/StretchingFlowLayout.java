package org.centauron.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

public class StretchingFlowLayout extends FlowLayout {
	
	public StretchingFlowLayout(int left, int i, int j) {
		super(left,i,j);
	}
	public StretchingFlowLayout(int left) {
		super(left,0,0);
	}
	/**
	 *  Description of the Method
	 *
	 *@param  target  Description of Parameter
	 */
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int nmembers = target.getComponentCount();
			int x = 0;

			Dimension preferredSize = preferredLayoutSize(target);
			Dimension targetSize = target.getSize();

			x =this.getVgap();
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					m.setSize(d.width, targetSize.height-2*this.getVgap());
					m.setLocation(x,this.getVgap());
					x += d.getWidth()+this.getHgap();

				}  else {
						break;
				}
			}
		}
	}

}
