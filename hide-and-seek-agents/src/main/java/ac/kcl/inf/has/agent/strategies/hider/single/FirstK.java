package ac.kcl.inf.has.agent.strategies.hider.single;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.RandomStrategy;
import ac.kcl.inf.has.agent.strategies.hider.AbstractHidingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class FirstK extends AbstractHidingStrategy {

    private Set<Vertex> hiddenLocations;

    public FirstK(int hideNumber) {
        super(hideNumber);
        hiddenLocations = new HashSet<>();
    }

    @Override
    public String getName() {
        return "hFirstK";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        hiddenLocations.clear();
    }

    @Override
    public HiderAction nextAction() {
        if(hiddenLocations.contains(currPos)){

            localGraph.updateGraph(currPos, graphController.getConnectedVertex(currPos));

            List<Edge> connectedEdge = new ArrayList<>(localGraph.connectEdges(currPos));
            Collections.shuffle(connectedEdge);
            Vertex next = null;

            for (Edge edge:connectedEdge){
                Vertex tmp = edge.getAnotherSide(currPos);
                if(!hiddenLocations.contains(tmp)){
                    next = tmp;
                    break;
                }
            }

            if(next == null){
                currPos = localGraph.randomSelectConnectedVertex(currPos);
            }else {
                currPos = next;
            }

            action.setType(HiderActionType.MOVE_TO);
            action.setPayload(currPos);
        }else{
            hiddenLocations.add(currPos);
            action.setType(HiderActionType.HIDE);
            action.setPayload(null);
        }
        return action;
    }
}
