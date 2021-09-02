package ac.kcl.inf.has.agent.strategies.hider;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

public abstract class AbstractHidingStrategy implements HidingStrategy{

    protected int hideNumber;
    protected Vertex currPos;
    protected HiderAction action;

    protected GraphController graphController;
    protected LocalGraph localGraph;

    public AbstractHidingStrategy(int hideNumber){
        this.hideNumber = hideNumber;
        this.action = new HiderAction();
    }

    public void setHideNumber(int hideNumber) {
        this.hideNumber = hideNumber;
    }

    public int getHideNumber() {
        return hideNumber;
    }

    @Override
    public void setGraphController(GraphController controller) {
        this.graphController = controller;
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        this.localGraph = localGraph;
    }

}
