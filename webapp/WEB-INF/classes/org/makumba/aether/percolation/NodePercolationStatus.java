package org.makumba.aether.percolation;

import org.makumba.aether.model.PercolationStep;

public class NodePercolationStatus {

    private String key;

    private int energy;

    private int level;

    private PercolationStep previousStep;

    public String getKey() {
        return key;
    }

    public int getEnergy() {
        return energy;
    }

    public int getLevel() {
        return level;
    }

    public PercolationStep getPreviousStep() {
        return previousStep;
    }

    public NodePercolationStatus(String key, int energy, int level, PercolationStep previousStep) {
        super();
        this.key = key;
        this.energy = energy;
        this.level = level;
        this.previousStep = previousStep;
    }

}
