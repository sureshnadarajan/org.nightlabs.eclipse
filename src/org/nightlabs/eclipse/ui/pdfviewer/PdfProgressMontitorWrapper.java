/**
 * 
 */
package org.nightlabs.eclipse.ui.pdfviewer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.nightlabs.eclipse.ui.pdfrenderer.IPdfProgressMonitor;

/**
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 *
 */
public class PdfProgressMontitorWrapper implements IPdfProgressMonitor {

	private IProgressMonitor progressMonitor;
	
	/**
	 * 
	 */
	public PdfProgressMontitorWrapper(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.pdfrenderer.IPdfProgressMonitor#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(String taskName, int work) {
		if (progressMonitor != null) {
			progressMonitor.beginTask(taskName, work);
		}
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.pdfrenderer.IPdfProgressMonitor#done()
	 */
	@Override
	public void done() {
		if (progressMonitor != null) {
			progressMonitor.done();
		}
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.eclipse.ui.pdfrenderer.IPdfProgressMonitor#worked(int)
	 */
	@Override
	public void worked(int work) {
		if (progressMonitor != null) {
			progressMonitor.worked(work);
		}
	}

}
