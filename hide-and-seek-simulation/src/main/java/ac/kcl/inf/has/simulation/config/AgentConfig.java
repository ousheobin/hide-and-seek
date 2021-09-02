package ac.kcl.inf.has.simulation.config;

import java.util.ArrayList;
import java.util.List;

public class AgentConfig {

    private static final String EMPTY = "";

    private String type;
    private String strategy;
    private String explorationStrategy;
    private int deceptiveRounds;
    private double graphPortion;

    public AgentConfig(){
        type = "normal";
        strategy = EMPTY;
        explorationStrategy = "GreedyBacktrack";
        graphPortion = 1.0;
        deceptiveRounds = -1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public void setGraphPortion(double graphPortion) {
        this.graphPortion = graphPortion;
    }

    public double getGraphPortion() {
        return graphPortion;
    }

    public void setDeceptiveRounds(int deceptiveRounds) {
        this.deceptiveRounds = deceptiveRounds;
    }

    public int getDeceptiveRounds() {
        return deceptiveRounds;
    }

    @Override
    public String toString() {
        return "AgentConfig{" +
                "type='" + type + '\'' +
                ", strategy='" + strategy + '\'' +
                ", explorationStrategy='" + explorationStrategy + '\'' +
                ", graphPortion=" + graphPortion +
                '}';
    }
}
