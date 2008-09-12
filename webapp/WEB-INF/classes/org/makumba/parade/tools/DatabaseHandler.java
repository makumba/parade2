package org.makumba.parade.tools;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * java logging handler that sends a record to the database
 * @author Manuel Gay
 *
 */
public class DatabaseHandler extends Handler {
    
    @Override
    public void close() throws SecurityException {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void publish(LogRecord record) {
        Object[] params = { TriggerFilter.prefix.get() };
        record.setParameters(params);
        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", record);
    }

}
