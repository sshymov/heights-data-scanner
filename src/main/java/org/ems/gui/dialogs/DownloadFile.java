package org.ems.gui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 8:50:00 PM
 */
public class DownloadFile extends Dialog {
    public DownloadFile(Shell shell) {
        super(shell);
    }

    protected Control createContents(Composite composite) {
        Text helloText = new Text(composite, SWT.CENTER);
helloText.setText("Hello SWT and JFace!");
composite.pack();
return composite;
    }
}
