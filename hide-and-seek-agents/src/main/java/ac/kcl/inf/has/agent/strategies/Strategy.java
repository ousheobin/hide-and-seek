package ac.kcl.inf.has.agent.strategies;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.player.Agent;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

public interface Strategy {

    String getName();

    void setGraphController(GraphController graphController);

    void setLocalGraph(LocalGraph localGraph);

    void notifyForNewRound(Vertex startLocation);

}
