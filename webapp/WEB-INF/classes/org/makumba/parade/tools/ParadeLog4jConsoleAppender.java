package org.makumba.parade.tools;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;

public class ParadeLog4jConsoleAppender extends ConsoleAppender {
    
    @Override
    public void append(LoggingEvent event) {
        //TriggerFilter.redirectToServlet("perThreadEnabler", "perThreadEnabled", new Boolean(false));
        PerThreadPrintStream.setEnabled(false);
        super.append(event);
        if(event != null)
            TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", event);
        //TriggerFilter.redirectToServlet("perThreadEnabler", "perThreadEnabled", new Boolean(true));
        PerThreadPrintStream.setEnabled(true);
    }
}
