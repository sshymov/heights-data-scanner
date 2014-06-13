package org.ems.gui.components;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.events.*;
import org.eclipse.swt.SWT;
import org.ems.model.hgt.HGT;
import org.ems.gui.components.palette.PhysicalMapPolette;
import org.ems.gui.components.palette.GrayScalePalette;
import org.ems.gui.events.SimpleEvent;
import org.ems.gui.events.Event;
import org.ems.gui.events.listeners.UpdateStatusListener;

import java.io.InputStream;

/**
 * User: stas
 * Date: Sep 30, 2008
 */
public class GeoImageComponent  extends Composite
{
    //private HGT hgt; 
    public GeoImageComponent(Composite parent, final HGT hgt)
    {
        super(parent, SWT.NO_BACKGROUND|SWT.NO_FOCUS);
        //this.hgt=hgt;
        setSize(hgt.getHeader().getWidth(),hgt.getHeader().getHeight());
        //parent.setSize(320,190);
        final MapImageDataBuilder builder=new MapImageDataBuilder(hgt.getHeader().getWidth(),
                hgt.getHeader().getHeight()
                ,new PhysicalMapPolette());

        builder.drawPhysicalMap(hgt);


        this.addPaintListener(new PaintListener()
        {
            public void paintControl(PaintEvent pe)
            {
                GC gc = pe.gc;
                Image map = new Image(pe.display, builder.getMapData());
                gc.drawImage(map, 0, 0);
                map.dispose();
            }
        });

        this.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent event) {
                SimpleEvent simpleEvent=new SimpleEvent(Event.EVENT_UPDATE_STATUS);
                simpleEvent.getParameters().put(UpdateStatusListener.PARAM_STATUS_VALUE,
                        ""+event.x+"x"+event.y+"="+hgt.getHeightsMatrix()[event.y][event.x]);
                simpleEvent.fire();
            }
        });

        this.addMouseListener(new MouseListener() {
            public void mouseDoubleClick(MouseEvent event) {
            }

            public void mouseDown(MouseEvent event) {
            }

            public void mouseUp(MouseEvent event) {
            }
        });
    }
}
