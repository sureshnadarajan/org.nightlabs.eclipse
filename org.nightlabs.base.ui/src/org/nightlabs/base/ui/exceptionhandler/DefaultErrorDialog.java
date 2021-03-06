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

package org.nightlabs.base.ui.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.nightlabs.base.ui.exceptionhandler.errorreport.ErrorReport;
import org.nightlabs.base.ui.exceptionhandler.errorreport.ErrorReportWizardDialog;
import org.nightlabs.base.ui.resource.Messages;
import org.nightlabs.base.ui.util.RCPUtil;
import org.nightlabs.eclipse.compatibility.CompatibleDialogConstants;

/**
 * The default error dialog implementation. Error dialogs should be opened
 * by calling one of the <code>showError</code> methods in {@link ErrorDialogFactory}.
 * This error dialog implementation is capable of handling multiple errors in
 * one dialog. It registers itsself in {@link ErrorDialogFactory} to do so.
 *
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 * @author Tobias Langner <!-- tobias[dot]langner[at]nightlabs[dot]de -->
 */
public class DefaultErrorDialog extends MessageDialog implements IErrorDialog
{
	private static final Logger logger = Logger.getLogger(DefaultErrorDialog.class);

	private static final int CUSTOM_ELEMENTS_WIDTH_HINT = 300;

	private static final int ERROR_TABLE_HEIGHT_HINT = 180;



	private static final int STACK_TRACE_LINE_COUNT = 15;

	protected static final int SEND_ERROR_REPORT_ID = IDialogConstants.CLIENT_ID + 1;

	private ExceptionHandlerParam exceptHandlerParam;



	/**
	 * Dialog title (a localized string).
	 */
	private String title;

	/**
	 * Collection of all errors currently displayed/displayable.
	 */
	private List<ErrorItem> errorList = new ArrayList<ErrorItem>();
	private ErrorTable errorTable;
	private Text stackTraceText;
	private Button detailsButton;

	public DefaultErrorDialog()
	{
		super(
				RCPUtil.getActiveShell(),
				JFaceResources.getString("Problem_Occurred"), //$NON-NLS-1$
				null,
				JFaceResources.getString("Problem_Occurred"), //$NON-NLS-1$
				ERROR,
				// FIXME: Build problem: IDialogConstants.OK_LABEL is static in RCP but non-static in RAP
//				new String[] { CompatibleDialogConstants.get().OK_LABEL },
				new String[] { "OK" },
				0);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	public void showError(String dialogTitle, String message,  ExceptionHandlerParam exceptionHandlerParam)
	{
		this.title = dialogTitle == null ? JFaceResources.getString("Problem_Occurred") : dialogTitle; //$NON-NLS-1$
		this.exceptHandlerParam = exceptionHandlerParam;

		ErrorItem errorItem = creatErrorItem(dialogTitle, message, exceptHandlerParam.getThrownException(), exceptHandlerParam.getTriggerException());
		errorList.add(errorItem);
		if(errorTable != null) {
			errorTable.refresh();
		}
		if(errorList.size() > 1)
			showErrorTable(true);
		setErrorItem(errorItem);
	}

	protected ErrorItem creatErrorItem(String dialogTitle, String message, Throwable thrownException, Throwable triggerException)
	{
		return new ErrorItem(message, thrownException, triggerException);
	}

	@Override
	public int open()
	{
		ErrorDialogFactory.addDialog(this);
		return super.open();
	}

	@Override
	public boolean close()
	{
		ErrorDialogFactory.removeDialog(this);
		return super.close();
	}

	@Override
	protected void configureShell(Shell shell)
	{
		super.configureShell(shell);
		if(this.title != null)
			shell.setText(this.title);

	}

	@Override
	protected void buttonPressed(int buttonId)
	{
		switch(buttonId) {
		case IDialogConstants.DETAILS_ID:
			boolean show = ((GridData)stackTraceText.getLayoutData()).heightHint == 0;
			logger.debug("showing stack trace: "+show); //$NON-NLS-1$
			showStackTrace(show);
			break;
		case SEND_ERROR_REPORT_ID:
//			ErrorItem error = errorTable.getSelectedItem();
			ErrorReport errorReport = null;
			for (ErrorItem error : errorList) {
				if (errorReport == null)
					errorReport = new ErrorReport(error.getThrownException(), error.getTriggerException());
				else
					errorReport.addThrowablePair(error.getThrownException(), error.getTriggerException());
			}

			if (errorReport == null) {
				Exception xxx = new IllegalStateException("There was no error in the errorList!!! How can this happen?!"); //$NON-NLS-1$
				errorReport = new ErrorReport(xxx, xxx);
			}

			if(this.exceptHandlerParam.getErrorScreenShot() != null)
				errorReport.setErrorScreenshot(this.exceptHandlerParam.getErrorScreenShot());

			ErrorReportWizardDialog dlg = new ErrorReportWizardDialog(errorReport);
			okPressed();
			dlg.open();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

	protected void setErrorItem(ErrorItem errorItem)
	{
		String message = errorItem.getMessage();
		String exMsg = errorItem.getThrownException().toString();
		this.message = (message == null || "".equals(message)) ? exMsg : message; //$NON-NLS-1$
		if(messageLabel != null && !messageLabel.isDisposed())
			messageLabel.setText(this.message.replace("&", "&&")); //$NON-NLS-1$ //$NON-NLS-2$
		if(errorTable != null && !errorTable.isDisposed())
			errorTable.setSelectedItem(errorItem);
		if(stackTraceText != null && !stackTraceText.isDisposed())
			stackTraceText.setText(ErrorReport.getExceptionStackTraceAsString(errorItem.getThrownException()));
	}

	boolean errorTableVisible = false;

	private void showErrorTable(boolean newVisible)
	{
		if(errorTable != null) {
			GridData errorTableGD = ((GridData)errorTable.getLayoutData());
			if(errorTableVisible == newVisible)
				return;
			Point windowSize = getShell().getSize();
			Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);

			errorTableGD.heightHint = newVisible ? ERROR_TABLE_HEIGHT_HINT : 0;
			errorTable.setVisible(true);

			Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
			getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));
		}
		errorTableVisible = newVisible;
	}

	private void showStackTrace(boolean visible)
	{
		Point windowSize = getShell().getSize();
		Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		GridData stackTraceGD = ((GridData)stackTraceText.getLayoutData());
		if(visible) {
			stackTraceGD.heightHint = stackTraceText.getLineHeight() * STACK_TRACE_LINE_COUNT;
			// FIXME: Build-Problem: .SHOW_DETAILS_LABEL is static in RCP but nun-static in RAP, can't build only against one platform!!
//			detailsButton.setText(CompatibleDialogConstants.get().HIDE_DETAILS_LABEL);
			detailsButton.setText("Hide details");
			stackTraceText.setVisible(true);
		} else {
			stackTraceGD.heightHint = 0;
			// FIXME: Build-Problem: .SHOW_DETAILS_LABEL is static in RCP but nun-static in RAP, can't build only against one platform!!
//			detailsButton.setText(CompatibleDialogConstants.get().SHOW_DETAILS_LABEL);
			detailsButton.setText("Show details");
			stackTraceText.setVisible(false);
		}
		Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));
	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		createErrorTable(parent);
		createStackTraceText(parent);
		return parent;
	}

	private Control createErrorTable(Composite parent)
	{
		errorTable = new ErrorTable(parent, SWT.NONE);
		errorTable.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent event) {
				errorTable = null;
			}
		});

		applyDialogFont(errorTable);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = CUSTOM_ELEMENTS_WIDTH_HINT;
		if(errorTableVisible) {
			gd.heightHint = ERROR_TABLE_HEIGHT_HINT;
			errorTable.setVisible(true);
		} else {
			gd.heightHint = 0;
			errorTable.setVisible(false);
		}
		errorTable.setLayoutData(gd);
		errorTable.setInput(errorList);
		errorTable.addSelectionChangedListener(
				new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event)
					{
						setErrorItem(errorTable.getSelectedItem());
					}
				}
		);
		return errorTable;
	}

	protected Control createStackTraceText(Composite parent)
	{
		stackTraceText = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		applyDialogFont(stackTraceText);
		if(errorTable != null && errorTable.getSelectedItem() != null)
			stackTraceText.setText(ErrorReport.getExceptionStackTraceAsString(errorTable.getSelectedItem().getThrownException()));
		else
			stackTraceText.setText(""); //$NON-NLS-1$
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
				| GridData.GRAB_VERTICAL);
		data.widthHint = CUSTOM_ELEMENTS_WIDTH_HINT;
		data.heightHint = 0; //stackTraceText.getLineHeight() * TEXT_LINE_COUNT;
		stackTraceText.setVisible(false);
		//data.horizontalSpan = 2;
		stackTraceText.setLayoutData(data);
		return stackTraceText;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		super.createButtonsForButtonBar(parent);
		createButton(parent, SEND_ERROR_REPORT_ID, Messages.getString("org.nightlabs.base.ui.exceptionhandler.DefaultErrorDialog.button.sendErrorReport.text"), false); //$NON-NLS-1$
		// FIXME: Build-Problem: .SHOW_DETAILS_LABEL is static in RCP but nun-static in RAP, can't build only against one platform!!
//		detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, CompatibleDialogConstants.get().SHOW_DETAILS_LABEL, false);
		detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, "Show details", false);
	}
}