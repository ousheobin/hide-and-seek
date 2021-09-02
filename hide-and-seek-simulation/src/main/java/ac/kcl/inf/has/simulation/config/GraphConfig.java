package ac.kcl.inf.has.simulation.config;

import ac.kcl.inf.has.env.graph.generation.GraphGenerateStrategies;

public class GraphConfig {

    private GraphGenerateStrategies generateStrategy;
    private int vertex = 2;
    private int hideNumber = 1;
    private boolean canKnowCostInAdvance;

    public GraphGenerateStrategies getGenerateStrategy() {
        return generateStrategy;
    }

    public void setGenerateStrategy(GraphGenerateStrategies generateStrategy) {
        this.generateStrategy = generateStrategy;
    }

    public int getVertex() {
        return vertex;
    }

    public void setVertex(int vertex) {
        this.vertex = vertex;
    }

    public boolean isCanKnowCostInAdvance() {
        return canKnowCostInAdvance;
    }

    public void setCanKnowCostInAdvance(boolean canKnowCostInAdvance) {
        this.canKnowCostInAdvance = canKnowCostInAdvance;
    }

    public void setHideNumber(int hideNumber) {
        if(hideNumber > vertex){
            throw new IllegalArgumentException("Hide number cannot larger than vertex number");
        }
        this.hideNumber = hideNumber;
    }

    public int getHideNumber() {
        return hideNumber;
    }

    public String formatStr(){
        return generateStrategy.toString();
    }

    @Override
    public String toString() {
        return "GraphConfig{" + "generateStrategy=" + generateStrategy + ", vertex=" + vertex + '}';
    }
}
