/**
 * 
 */
package org.nightlabs.editor2d.viewer.tool;

import org.nightlabs.editor2d.viewer.event.MouseEvent;

/**
 * @author Daniel.Mazurek <at> NightLabs <dot> de
 *
 */
public class ZoomTool 
extends RectangleTool 
{
	public static final String ID = ZoomTool.class.getName();
	
	public ZoomTool() 
	{
		super();
		setID(ID);
		setShowRollOver(false);
		setShowTooltip(false);
	}

	@Override
	public void mouseReleased(MouseEvent me) 
	{
		super.mouseReleased(me);		
		if (rect != null) {
			if (leftPressed)
				getViewer().getZoomSupport().zoomTo(rect.getBounds());
		}
		leftPressed = false;
		rightPressed = false;
		rect = null;
	}		
			
}
