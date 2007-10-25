/* *****************************************************************************
 * NightLabs Editor2D - Graphical editor framework                             *
 * Copyright (C) 2004-2005 NightLabs - http://NightLabs.org                    *
 *                                                                             *
 * This library is free software; you can redistribute it and/or               *
 * modify it under the terms of the GNU Lesser General Public                  *
 * License as published by the Free Software Foundation; either                *
 * version 2.1 of the License, or (at your option) any later version.          *
 *                                                                             *
 * This library is distributed in the hope that it will be useful,             *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU           *
 * Lesser General Public License for more details.                             *
 *                                                                             *
 * You should have received a copy of the GNU Lesser General Public            *
 * License along with this library; if not, write to the                       *
 *     Free Software Foundation, Inc.,                                         *
 *     51 Franklin St, Fifth Floor,                                            *
 *     Boston, MA  02110-1301  USA                                             *
 *                                                                             *
 * Or get it online :                                                          *
 *     http://www.gnu.org/copyleft/lesser.html                                 *
 *                                                                             *
 *                                                                             *
 ******************************************************************************/
package org.nightlabs.editor2d.ui.figures;

import java.awt.Graphics2D;

import org.nightlabs.editor2d.PageDrawComponent;

/**
 * <p> Author: Daniel.Mazurek[AT]NightLabs[DOT]de </p>
 */
public class PageFreeformFigure 
//extends ContainerDrawComponentFigure 
extends ContainerFreeformLayer
{

	public PageFreeformFigure() {
		super();
	}

	protected PageDrawComponent getPageDrawComponent() {
		return (PageDrawComponent) drawComponent;
	}
	
	/**
	 * Overridden to paint bounds of page and children
	 */
	@Override
	public void paint(Graphics2D graphics) {
		DrawComponentFigure.paintJ2D(graphics, drawComponent, renderer);
	}  
	
//	/**
//	 * Overridden to paint bounds of page and children
//	 */
//	public void paint(Graphics graphics) 
//	{
//		DrawComponentFigure.checkDraw2D(graphics, drawComponent, renderer);
//	}	
}
