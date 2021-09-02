package ac.kcl.inf.has.agent.strategies.hider;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.Strategy;
import ac.kcl.inf.has.agent.strategies.action.Action;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

public interface HidingStrategy extends Strategy {

    HiderAction nextAction();

}
