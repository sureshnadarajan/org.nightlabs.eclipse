/* *****************************************************************************
 * org.nightlabs.base.ui - NightLabs Eclipse utilities                            *
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
package org.nightlabs.base.ui.table;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;

/**
 * This sorter can be used with a column of a {@link TableViewer} or {@link TreeViewer}
 * in combination with a {@link TableSortSelectionListener} to provide column sorting for the viewer.
 * <p>
 * The sorter is instantiated for a specific column-index it is responsible for.
 * It will use the viewers label-provider to obtain a Comparable for each element of the column
 * and sort using that comparable. 
 * This sorter can operate on the following label-providers: 
 * {@link IColumnComparableProvider}, {@link ITableLabelProvider} or {@link IBaseLabelProvider}.
 * </p>
 * 
 * @author Daniel.Mazurek [at] NightLabs [dot] de
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 */
public class GenericInvertViewerSorter
extends InvertableSorter<Object>
{
	public GenericInvertViewerSorter(int columnIndex) {
		super();
		this.columnIndex = columnIndex;
	}

	private int columnIndex = 0;

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
//		return superCompare(viewer, e1, e2);
		return compare(viewer, e1, e2,columnIndex);
	}

	@Override
	public int _compare(Viewer viewer, Object e1, Object e2) {
		return 0;
	}

	@Override
	public int getSortDirection() {
		return SWT.UP;
	}

	@Override
	public InvertableSorter getInverseSorter() {
		return inverse;
	}

	private InvertableSorter inverse = new InvertableSorter<Object>(){
		@Override
		public int getSortDirection() {
			return SWT.DOWN;
		}

		@Override
		public InvertableSorter getInverseSorter() {
			return GenericInvertViewerSorter.this;
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
//			return (-1) * superCompare(viewer, e1, e2);
			return (-1) * GenericInvertViewerSorter.this.compare(viewer, e1, e2, columnIndex);
		}

		@Override
		protected int _compare(Viewer viewer, Object e1, Object e2) {
			return 0;
		}
	};

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public int compare(Viewer viewer, Object e1, Object e2, int columnIndex)
	{
		int cat1 = category(e1);
		int cat2 = category(e2);

		if (cat1 != cat2) {
			return cat1 - cat2;
		}

		Comparable comp1;
		Comparable comp2;

		if (viewer == null || !(viewer instanceof ContentViewer)) {
			comp1 = e1.toString();
			comp2 = e2.toString();
		} else {
			IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
			if (prov instanceof IColumnComparableProvider) {
				IColumnComparableProvider cprov = (IColumnComparableProvider) prov;
				comp1 = cprov.getColumnComparable(e1, columnIndex);
				comp2 = cprov.getColumnComparable(e2, columnIndex);
			} 
			else if (prov instanceof ITableLabelProvider) {
				ITableLabelProvider lprov = (ITableLabelProvider) prov;
				comp1 = lprov.getColumnText(e1, columnIndex);
				comp2 = lprov.getColumnText(e2, columnIndex);
			}
			else if (prov instanceof ILabelProvider) {
				ILabelProvider lprov = (ILabelProvider) prov;
				comp1 = lprov.getText(e1);
				comp2 = lprov.getText(e2);
			}
			else {
				comp1 = e1.toString();
				comp2 = e2.toString();
			}
		}
		if (comp1 == null) {
			return comp2 != null ? -1: 0;
		}
		if (comp2 == null) {
			return comp1 != null ? 1 : 0;
		}
		
		return comp1.compareTo(comp2);
	}

}
