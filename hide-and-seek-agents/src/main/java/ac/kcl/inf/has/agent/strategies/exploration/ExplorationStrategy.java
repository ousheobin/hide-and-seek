package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

public interface ExplorationStrategy {

    Vertex nextNode();

    void startNode(Vertex vertex);

    void setGraphController(GraphController graphController);

    void setLocalGraph(LocalGraph localGraph);

    void updateCurrentPos(Vertex currentPos);

}
