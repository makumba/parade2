package org.makumba.parade.view;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * Servlet that provides JSON data for the ticker tape javascript.
 * 
 * The dataQueue is populated by the DatabaseLogServlet for now.
 * 
 * @author Manuel Gay
 * 
 */
public class TickerTapeServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static List<TickerTapeData> dataQueue = new LinkedList<TickerTapeData>();

    private static int counter = 0;

    private static int addedItems = 0;

    @Override
    public void init() {
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {

        if (dataQueue.size() == 0)
            return;

        PrintWriter out = resp.getWriter();

        if (dataQueue.size() > 2) {
            // wipe out old events, keep only 3 last ones
            List<TickerTapeData> newQueue = new LinkedList<TickerTapeData>();
            newQueue.add(dataQueue.get(dataQueue.size() - 3));
            newQueue.add(dataQueue.get(dataQueue.size() - 2));
            newQueue.add(dataQueue.get(dataQueue.size() - 1));

            dataQueue = newQueue;
        }

        // we'll display 3 events at a time
        // when doing a GET the javascript sends us a lastId parameter
        // so we can decide what dataset to send

        Iterator<TickerTapeData> iter = dataQueue.iterator();

        out.println("[");

        while (iter.hasNext()) {
            printItem(iter.next(), iter.hasNext(), out);
            if (counter == 3) {
                counter = 0;
            } else
                counter++;
        }

        out.println("]");

    }

    public static void addItem(TickerTapeData data) {
        if (data.getLinkText().length() == 0)
            return;
        dataQueue.add(data);
        addedItems++;
    }

    private void printItem(TickerTapeData data, boolean hasNext, PrintWriter out) {
        out.println("{");
        out.println("\"Id\":" + counter + ",");
        out.println("\"LinkText\":\"" + data.getLinkText() + "\",");
        out.println("\"Url\":\"" + data.getUrl() + "\",");
        out.println("\"Title\":\"" + data.getTitle() + "\"");
        if (hasNext)
            out.println("},");
        else
            out.println("}");
    }
}