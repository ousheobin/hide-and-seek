package ac.kcl.inf.has.agent.strategies.seeker;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.Strategy;
import ac.kcl.inf.has.env.controller.GraphController;

public abstract class AbstractSeekingStrategy implements SeekingStrategy {

    protected GraphController graphController;
    protected LocalGraph localGraph;

    @Override
    public void setGraphController(GraphController controller) {
        this.graphController = controller;
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        this.localGraph = localGraph;
    }

}
