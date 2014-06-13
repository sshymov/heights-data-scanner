package org.ems.gui;

import org.eclipse.jface.action.*;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.ems.gui.components.GeoImageComponent;
import org.ems.gui.events.listeners.UpdateStatusListener;
import org.ems.gui.actions.OpenHGTAction;
import org.ems.model.hgt.HGT;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 8:55:16 PM
 */
public class GeoAppWindow extends ApplicationWindow {

    private HGT hgt; //currently opened hgt
    private ScrolledComposite scrolledComposite;

    public GeoAppWindow() {
        super(null);
        addCoolBar(SWT.NONE);
        addMenuBar();
        
        //addToolBar(SWT.HORIZONTAL);
        addStatusLine();

        registerEvents();
    }

    private void registerEvents() {
        new UpdateStatusListener(this).register();
    }



    protected MenuManager createMenuManager() {
        MenuManager mainMenu = new MenuManager(null);

        MenuManager fileMenu = new MenuManager("&File");
        mainMenu.add(fileMenu);
        fileMenu.add(new OpenHGTAction(this));

        fileMenu.add(new Action("Save As &Image") {
            public void run() {
                setStatus("File->Save As Image");
            }
        });

        fileMenu.add(new Action("Save For &Google Earth") {
            public void run() {
                setStatus("File->Save For &Google Earth");
            }
        });

        MenuManager viewMenu = new MenuManager("&View");
        mainMenu.add(viewMenu);
        viewMenu.add(new Action("&Zoom 1:1") {
            public void run() {
                setStatus("View->Zoom 1:1");
            }
        });

        MenuManager actionMenu = new MenuManager("&Action");
        mainMenu.add(actionMenu);
        actionMenu.add(new Action("&Filter by threshold") {
            public void run() {
                setStatus("Action->Filter by threshold");
            }
        });

        return mainMenu;


    }

    protected CoolBarManager createCoolBarManager(int i) {
        CoolBarManager coolBarManager = new CoolBarManager();
        ToolBarManager fileToolBar=new ToolBarManager();
        coolBarManager.add( fileToolBar );
        ToolBarManager viewToolBar=new ToolBarManager();
        coolBarManager.add( viewToolBar );
        ToolBarManager actionToolBar=new ToolBarManager();
        coolBarManager.add( actionToolBar );

        Action helloAction=new Action( "&Hello" ) {

            public void run() {
                setStatus("Hello world!!");
            }

        };

        fileToolBar.add(helloAction);

        viewToolBar.add(helloAction);

        actionToolBar.add(helloAction);

        return coolBarManager;
    }

    protected Control createContents(Composite composite) {
        
        getShell().setText("My window");
        composite.setSize(200,200);

        SashForm sash_form = new SashForm(getShell(), SWT.HORIZONTAL | SWT.NULL);
        scrolledComposite = new ScrolledComposite(sash_form, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

        Table table = createTable(sash_form);



        return composite;
    }



    private Table createTable(Composite shell) {
        Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	table.setLinesVisible (true);
	table.setHeaderVisible (true);
	GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	data.heightHint = 200;
	table.setLayoutData(data);
	String[] titles = {"Coordinate", "Directions"};
	for (int i=0; i<titles.length; i++) {
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText (titles [i]);
	}
//	int count = 128;
//	for (int i=0; i<count; i++) {
//		TableItem item = new TableItem (table, SWT.NONE);
//		item.setText (0, "Coordinate");
//		item.setText (1, "Directions");
//		item.setText (2, "!");
//		item.setText (3, "this stuff behaves the way I expect");
//		item.setText (4, "almost everywhere");
//		item.setText (5, "some.folder");
//		item.setText (6, "line " + i + " in nowhere");
//	}
	for (int i=0; i<titles.length; i++) {
		table.getColumn (i).pack ();
	}
        return table;
    }

    public void updateContents(String filename) {
        HGT hgt;
        System.out.println(filename);
        try {
            hgt=HGT.create(filename);
        } catch(Exception e) {
            new MessageDialog(getShell(),"Can't open file",null,
                    e.getMessage(),MessageDialog.WARNING,
                    new String[]{"Ok"},0).open();
            
            return;
        }
        if (hgt!=null) {

            GeoImageComponent map=new GeoImageComponent(scrolledComposite,hgt);
            scrolledComposite.setContent(map);


            //Text helloText = new Text(composite, SWT.CENTER);
            //helloText.setText("Hello SWT and JFace!");

            this.hgt=hgt;
            getShell().pack();
        }
        //composite.pack();

    }

}
