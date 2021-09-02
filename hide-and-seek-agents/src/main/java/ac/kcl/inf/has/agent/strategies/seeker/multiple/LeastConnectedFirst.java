package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.preference.LeastConnectedPreference;
import ac.kcl.inf.has.agent.strategies.preference.PreferenceMechanism;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.PreferenceSeekingStrategy;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;
import java.util.function.ToIntFunction;

public class LeastConnectedFirst extends AbstractSeekingStrategy {

    private Set<Vertex> visitedSet;
    private Set<Vertex> discoveredSet;

    private Vertex currPos;
    private SeekerAction action;

    public LeastConnectedFirst(){
        visitedSet = new HashSet<>();
        discoveredSet = new HashSet<>();
        action = new SeekerAction();
    }

    @Override
    public String getName() {
        return "sLeastConnectedFirst";

    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        visitedSet.clear();
        discoveredSet.clear();
     }

    @Override
    public SeekerAction nextAction() {
        if(!discoveredSet.contains(currPos)){
            discoveredSet.add(currPos);
            action.setPayload(null);
            action.setType(SeekerActionType.DISCOVER);
            return action;
        }

        action.setType(SeekerActionType.MOVE_TO);
        visitedSet.add(currPos);
        localGraph.updateGraph(currPos, graphController.getConnectedVertex(currPos));

        List<Pair<Edge, Integer>> connectivity = new ArrayList<>();
        for (Edge edge : localGraph.connectEdges(currPos)){
            if(visitedSet.contains(edge.getAnotherSide(currPos))){
                continue;
            }
            int connectivityOfEdge = localGraph.connectEdges(edge.getAnotherSide(currPos)).size();
            connectivity.add(new Pair<>(edge, connectivityOfEdge));
        }

        Vertex nextHop = null;
        if(!connectivity.isEmpty()){
            connectivity.sort(
                    Comparator.comparingInt((ToIntFunction<Pair<Edge, Integer>>) Pair::getObject2)
                            .thenComparingDouble(e -> e.getObject1().getWeight())
            );
            nextHop = connectivity.get(0).getObject1().getAnotherSide(currPos);
        }else {
            nextHop = localGraph.randomSelectConnectedVertex(currPos);
        }

        currPos = nextHop;
        action.setPayload(currPos);
        return action;
    }

    @Override
    public void updateDiscoverInfo(Vertex vertex, boolean found) {

    }

}
