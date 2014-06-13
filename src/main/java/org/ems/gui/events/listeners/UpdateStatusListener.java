package org.ems.gui.events.listeners;

import org.ems.gui.events.AbstractEventListener;
import org.ems.gui.events.Event;
import org.ems.gui.GeoAppWindow;

import java.util.Map;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public class UpdateStatusListener extends AbstractEventListener{
    public static String PARAM_STATUS_VALUE="status";
    private GeoAppWindow window;
    public UpdateStatusListener(GeoAppWindow window) {
        super(Event.EVENT_UPDATE_STATUS);
        this.window=window;
    }

    public void handleEvent(Map params) {
        window.setStatus((String)params.get(PARAM_STATUS_VALUE));
    }
}
