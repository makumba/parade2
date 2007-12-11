package org.makumba.parade.tools;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class ParadeConsoleHandler extends ConsoleHandler {
    
    
    @Override
    public void publish(LogRecord record) {
        PerThreadPrintStream.setEnabled(false);
        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", record);
        Object[] params = {TriggerFilter.prefix.get()};
        record.setParameters(params);
        super.publish(record);
        PerThreadPrintStream.setEnabled(true);
    }
    
    public ParadeConsoleHandler() {
        super();
        setOutputStream(System.out);
    }

}
