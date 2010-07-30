package org.makumba.aether.percolation;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import org.jfree.data.xy.DefaultXYDataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.olikurt.parser.Function;
import de.olikurt.parser.Variable;

/**
 * A dataset producer for Aether progression curves
 * 
 * @author Manuel Gay
 * 
 */
public class ProgressionCurveDatasetProducer implements DatasetProducer, Serializable {

    private static final long serialVersionUID = 1L;

    private final String curve;

    private final int scale;

    public static final int MONTH_IN_HOURS = 24 * 7 * 4;

    public static final int WEEK_IN_HOURS = 24 * 7;

    public static final int DAY_IN_HOURS = 24;

    /**
     * Produces some random data.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object produceDataset(Map params) throws DatasetProduceException {
        DefaultXYDataset dataset = new DefaultXYDataset();

        Variable v = new Variable('t');

        Function f = new Function(curve);
        Vector<Variable> vec = new Vector<Variable>();
        vec.add(v);

        double[][] data = new double[2][scale];

        for (int i = 0; i < scale; i++) {
            v.setValue(i);
            data[0][i] = i;
            data[1][i] = f.calculate(vec);
        }

        dataset.addSeries("curve", data);
        return dataset;
    }

    /**
     * Returns a unique ID for this DatasetProducer
     */
    public String getProducerId() {
        return "PageViewCountData DatasetProducer";
    }

    public ProgressionCurveDatasetProducer(String curve, int scale) {
        this.curve = curve;
        this.scale = scale;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasExpired(Map arg0, Date arg1) {
        return true;
    }
}
