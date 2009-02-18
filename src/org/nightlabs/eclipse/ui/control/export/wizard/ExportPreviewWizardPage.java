/**
 *
 */
package org.nightlabs.eclipse.ui.control.export.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import org.nightlabs.eclipse.ui.control.export.FocusHistory;
import org.nightlabs.eclipse.ui.control.export.copy.WidgetCopyUtil;

/**
 * @author Chairat Kongarayawetchakun - chairat [AT] nightlabs [DOT] de
 *
 */
public class ExportPreviewWizardPage extends WizardPage {

	protected ExportPreviewWizardPage(String pageName) {
		super(pageName);
		setTitle("Preview");
		setDescription("Description");
	}

	@Override
	public void createControl(Composite parent) {
		 Composite container = new Composite(parent, SWT.NULL);
		 GridLayout gridLayout = new GridLayout();
		 container.setLayout(gridLayout);

		 Widget widget = FocusHistory.sharedInstance().getLastItem().getWidget();
		 if (widget instanceof Table) {
			 Table table = (Table)widget;

			 Table newTable = WidgetCopyUtil.createCopy(container, table);
			 newTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		 }

//		 new Label(container, SWT.NONE).setText(":" + FocusHistory.sharedInstance().getLastItem());
		 setControl(container);
	}


}
