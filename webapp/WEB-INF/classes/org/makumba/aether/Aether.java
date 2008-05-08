package org.makumba.aether;

/**
 * The access point to the Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public class Aether {

    private static Aether instance;

    public static Aether getAether(AetherContext ctx) {
        if (instance == null) {
            instance = new Aether(ctx);
        }
        return instance;
    }

    private AetherContext ctx;

    private Aether(AetherContext ctx) {
        this.ctx = ctx;
    }
    
    private void startup() {
        
    }
    
    private void computeAllRelations() {
        
    }

}
