package ac.kcl.inf.has.agent.strategies.hider.single;

import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.hider.AbstractHidingStrategy;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class HighCostGreedy extends AbstractHidingStrategy {

    private Set<Vertex> hideSet;

    public HighCostGreedy(int hideNumber) {
        super(hideNumber);
        hideSet = new HashSet<>();
    }

    @Override
    public String getName() {
        return "hHighCostGreedy";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        hideSet.clear();
    }

    @Override
    public HiderAction nextAction() {
        if(hideSet.contains(currPos)){

            localGraph.updateGraph(currPos, graphController.getConnectedVertex(currPos));

            List<Edge> connectedEdge = new ArrayList<>(localGraph.connectEdges(currPos));
            Vertex next = null;
            double maxGrade = -1;

            for (Edge edge:connectedEdge){
                Vertex tmp = edge.getAnotherSide(currPos);
                double grade = tmp.getWeight() / edge.getWeight();
                if(!hideSet.contains(tmp) && grade > maxGrade){
                    next = tmp;
                    maxGrade = grade;
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
            hideSet.add(currPos);
            action.setType(HiderActionType.HIDE);
            action.setPayload(null);
        }
        return action;
    }

}
