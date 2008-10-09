/* ********************************************************************
 * NightLabs PDF Viewer - http://www.nightlabs.org/projects/pdfviewer *
 * Copyright (C) 2004-2008 NightLabs GmbH - http://NightLabs.org      *
 *                                                                    *
 * This library is free software; you can redistribute it and/or      *
 * modify it under the terms of the GNU Lesser General Public         *
 * License as published by the Free Software Foundation; either       *
 * version 2.1 of the License, or (at your option) any later version. *
 *                                                                    *
 * This library is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  *
 * Lesser General Public License for more details.                    *
 *                                                                    *
 * You should have received a copy of the GNU Lesser General Public   *
 * License along with this library; if not, write to the              *
 *     Free Software Foundation, Inc.,                                *
 *     51 Franklin St, Fifth Floor,                                   *
 *     Boston, MA  02110-1301  USA                                    *
 *                                                                    *
 * Or get it online:                                                  *
 *     http://www.gnu.org/copyleft/lesser.html                        *
 **********************************************************************/
package org.nightlabs.eclipse.ui.pdfviewer.extension.action.zoom;

import org.nightlabs.l10n.NumberFormatter;

/**
 * @version $Revision$ - $Date$
 * @author marco schulze - marco at nightlabs dot de
 */
public class ZoomLevel
{
	public static final ZoomLevel ZOOM_TO_PAGE_WIDTH = new ZoomLevel("Page width");
	public static final ZoomLevel ZOOM_TO_PAGE_HEIGHT = new ZoomLevel("Page height");
	public static final ZoomLevel ZOOM_TO_PAGE = new ZoomLevel("Page (complete)");

	public ZoomLevel(int zoomFactorPerMill) {
		this.zoomFactorPerMill = zoomFactorPerMill;
	}

	private String label = null;

	public ZoomLevel(String label) {
		this.label = label;
	}

	private int zoomFactorPerMill = 0;

	public int getZoomFactorPerMill() {
		return zoomFactorPerMill;
	}

	public String getLabel() {
		return label;
	}

	public String getLabel(boolean auto) {
		if (!auto)
			return label;

		if (label == null)
			return NumberFormatter.formatFloat((double) zoomFactorPerMill / 10, 1) + "%";
		else
			return label;
	}
}
