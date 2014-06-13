package org.ems.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.SWT;
import org.ems.gui.GeoAppWindow;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public class OpenHGTAction extends Action {
    private GeoAppWindow win;

    public OpenHGTAction(GeoAppWindow win) {
        super("&Open");
        this.win=win;
    }

    public void run() {
        FileDialog fileDialog=new FileDialog(win.getShell(), SWT.OPEN);
        fileDialog.setFilterNames (new String [] {"HGT Files", "All Files (*.*)"});
        fileDialog.setFilterExtensions (new String [] {"*.hgt", "*.*"}); //Windows wild cards
        String val=fileDialog.open();
        if (val!=null)
            win.updateContents(fileDialog.getFilterPath()+"/"+fileDialog.getFileName());        
    }
}
