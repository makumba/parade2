package org.makumba.parade.tools;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class ParadeConsoleHandler extends ConsoleHandler {
    
    
    @Override
    public void publish(LogRecord record) {
        //TriggerFilter.redirectToServlet("perThreadEnabler", "perThreadEnabled", new Boolean(false));
        PerThreadPrintStream.setEnabled(false);
        Object[] params = {PerThreadPrintStream.get()};
        record.setParameters(params);
        super.publish(record);
        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", record);
        //TriggerFilter.redirectToServlet("perThreadEnabler", "perThreadEnabled", new Boolean(true));
        PerThreadPrintStream.setEnabled(true);
    }
    
    public ParadeConsoleHandler() {
        super();
        setOutputStream(System.out);
    }

}
