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
package org.nightlabs.base.ui.app;


/**
 * Listener which can be used to be notified about certain lifetime cycles on a application.
 * Implementations of this interface can be registered via the {@link WorkbenchListenerRegistry}.
 * The listeners will be triggered when your application extends {@link AbstractWorkbenchAdvisor}.  
 *  
 * @author Daniel.Mazurek [at] NightLabs [dot] de
 */
public interface IWorkbenchListener
{
	void openWindows();
	void postShutdown();
	void postStartup();
	void preShutdown();
	void preStartup();
	
//	void postWindowClose();
//	void postWindowCreate();
//	void postWindowOpen();
//	void postWindowRestore();
//	void preWindowOpen();
}
