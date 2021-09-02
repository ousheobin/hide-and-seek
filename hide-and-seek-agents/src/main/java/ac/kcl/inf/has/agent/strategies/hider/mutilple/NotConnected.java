package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.RandomStrategy;
import ac.kcl.inf.has.agent.strategies.hider.AbstractHidingStrategy;
import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotConnected extends AbstractHidingStrategy {

    private Set<Vertex> hiddenSet;
    private Set<Vertex> visitedSet;
    private ExplorationStrategy strategy;

    public NotConnected(int hideNumber) {
        super(hideNumber);
        hiddenSet = new HashSet<>();
        visitedSet = new HashSet<>();
        strategy = new RandomStrategy();
    }

    @Override
    public String getName() {
        return "hNotConnected";
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        super.setLocalGraph(localGraph);
        strategy.setLocalGraph(localGraph);
    }

    @Override
    public void setGraphController(GraphController controller) {
        super.setGraphController(controller);
        strategy.setGraphController(controller);
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        hiddenSet.clear();
        visitedSet.clear();
        strategy.startNode(startLocation);
    }

    @Override
    public HiderAction nextAction() {
        visitedSet.add(currPos);
        if(hideAtCurrentNode()){
            hiddenSet.add(currPos);
            action.setPayload(null);
            action.setType(HiderActionType.HIDE);
        }else {
            currPos = strategy.nextNode();
            action.setPayload(currPos);
            action.setType(HiderActionType.MOVE_TO);
        }
        return action;
    }

    private boolean hideAtCurrentNode(){
        if(hiddenSet.contains(currPos)){
            return false;
        }
        localGraph.updateGraph(currPos, graphController.getConnectedVertex(currPos));
        if ( visitedSet.size() == localGraph.getLocalGraph().vertexSet().size()){
            return true;
        }
        Vertex tmp;
        for ( Edge edge : localGraph.connectEdges(currPos) ) {
            tmp = edge.getAnotherSide(currPos);
            if (hiddenSet.contains(tmp)) {
                return false;
            }
        }
        return true;
    }

}
