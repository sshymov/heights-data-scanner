import java.io.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;

public class Table extends ApplicationWindow
{
  public Table()
  {
    super(null);
    addStatusLine();
  }

  protected Control createContents(Composite parent)
  {
    getShell().setText("JFace File Explorer");
    SashForm sash_form = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);

      Button b1=new Button(sash_form,SWT.None);
      Button b2=new Button(sash_form,SWT.None);


    return sash_form;
  }

  public static void main(String[] args)
  {
    Table w = new Table();
    w.setBlockOnOpen(true);
    w.open();
    Display.getCurrent().dispose();
  }
}