package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.hider.AbstractHidingStrategy;
import ac.kcl.inf.has.agent.strategies.hider.single.RandomSet;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class UniqueRandomSet extends RandomSet {

    public UniqueRandomSet(int hideNumber) {
        super(hideNumber);
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        visited.clear();
        unvisited.clear();
        currentPath = new ArrayList<>();
        if(hidden.size() >= localGraph.vertexCnt() - hideNumber){
            hidden.clear();
        }
        hideLocations = generateNewRandomSet(hideNumber);
    }

    @Override
    protected List<Vertex> generateNewRandomSet(int size) {
        return graphController.generateRandomHideLocation(size, hidden);
    }

}
