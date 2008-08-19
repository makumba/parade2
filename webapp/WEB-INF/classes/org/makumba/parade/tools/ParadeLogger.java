package org.makumba.parade.tools;

import java.util.logging.Logger;

public class ParadeLogger {

    public static Logger getParadeLogger(String className) {
        return Logger.getLogger("org.parade." + className);
    }

}
