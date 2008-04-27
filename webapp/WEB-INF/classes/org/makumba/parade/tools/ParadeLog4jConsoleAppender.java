package org.makumba.parade.tools;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;

public class ParadeLog4jConsoleAppender extends ConsoleAppender {

    @Override
    public void append(LoggingEvent event) {
        PerThreadPrintStream.setEnabled(false);
        if (event != null)
            TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", event);
        /*
         * String message = TriggerFilter.prefix.get() + " " + event.getLevel().toString() + " " +
         * event.getRenderedMessage() + " " +
         * event.getLocationInformation().getClassName()+":"+event.getLocationInformation().getLineNumber() + " - " +
         * event.getRenderedMessage();
         */
        super.append(event);
        PerThreadPrintStream.setEnabled(true);
    }
}
