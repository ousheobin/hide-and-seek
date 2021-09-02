package ac.kcl.inf.has.simulation.config;

public class SimulationConfigEntry {

    private GraphConfig graphConfig;

    private int rounds;

    private AgentConfig seekerConfig;
    private AgentConfig hiderConfig;

    public GraphConfig getGraphConfig() {
        return graphConfig;
    }

    public void setGraphConfig(GraphConfig graphConfig) {
        this.graphConfig = graphConfig;
    }

    public AgentConfig getSeekerConfig() {
        return seekerConfig;
    }

    public void setSeekerConfig(AgentConfig seekerConfig) {
        this.seekerConfig = seekerConfig;
    }

    public AgentConfig getHiderConfig() {
        return hiderConfig;
    }

    public void setHiderConfig(AgentConfig hiderConfig) {
        this.hiderConfig = hiderConfig;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "SimulationConfigEntry{" + "graphConfig=" + graphConfig + '}';
    }
}
