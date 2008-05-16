// $Id$
package org.nightlabs.eclipse.ui.fckeditor.file.image;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.nightlabs.eclipse.ui.fckeditor.file.ContentFileBasePage;
import org.nightlabs.eclipse.ui.fckeditor.file.IContentFileWizard;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class ContentImageFileWizard extends Wizard implements IContentFileWizard
{
	private ContentFileBasePage basePage;
	private ContentImageFileCropPage cropPage;

	/**
	 * Create a new ContentImageFileWizard instance.
	 */
	public ContentImageFileWizard()
	{
		basePage = new ContentFileBasePage();
		addPage(basePage);
		cropPage = new ContentImageFileCropPage();
		addPage(cropPage);
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					monitor.beginTask("Preparing image", 6);
					basePage.performFinish(new SubProgressMonitor(monitor, 1));
					cropPage.performFinish(new SubProgressMonitor(monitor, 5));
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			MessageDialog.openError(getShell(), "Error", String.format("Error preparing image: %s", e.getLocalizedMessage()));
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.fckeditor.file.IContentFileWizard#getData()
	 */
	@Override
	public byte[] getData()
	{
		return cropPage.getBinaryImageData();
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.fckeditor.file.IContentFileWizard#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return basePage.getUserFileDescription();
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.fckeditor.file.IContentFileWizard#getMimeType()
	 */
	@Override
	public String getMimeType()
	{
		return cropPage.getMimeType();
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.fckeditor.file.IContentFileWizard#getName()
	 */
	@Override
	public String getName()
	{
		return basePage.getUserFileName();
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.fckeditor.file.IContentFileWizard#setSourceFile(java.io.File, java.lang.String)
	 */
	@Override
	public void setSourceFile(File file, String mimeType)
	{
		basePage.setSourceFile(file);
		cropPage.setSourceFile(file);
	}
}
