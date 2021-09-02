package ac.kcl.inf.has.simulation;

import ac.kcl.inf.has.simulation.config.Config;
import ac.kcl.inf.has.simulation.controller.SimulationController;

import java.io.File;

public class Simulation {

    private static final String DEFAULT_CONFIG_FILE = "./resources/config.json";

    public static void main(String[] args) {
        System.out.println("[System] Start simulation");
        long start = System.currentTimeMillis();
        try {
            String configFile = DEFAULT_CONFIG_FILE;
            if (args.length >= 1){
                configFile = args[0];
            }
            Config.init(new File(configFile));
            SimulationController controller = SimulationController.getController();
            controller.configure(Config.getInstance()).doSimulate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println(
                "[System] End simulation. Using " + (System.currentTimeMillis() - start) + " ms");
    }
}
