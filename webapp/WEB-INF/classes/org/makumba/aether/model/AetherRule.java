package org.makumba.aether.model;

public interface AetherRule {

    public abstract boolean isActive();

    public abstract void setActive(boolean active);

}