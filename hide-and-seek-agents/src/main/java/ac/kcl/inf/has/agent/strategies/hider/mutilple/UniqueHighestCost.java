package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.agent.strategies.preference.HighestCostPreference;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueHighestCost extends PreferenceHidingStrategy {

    protected Set<Vertex> usedSet;

    public UniqueHighestCost(int hideNumber, double graphPortion) {
        super(hideNumber, graphPortion);
        usedSet = new HashSet<>();
    }

    @Override
    public String getName() {
        return "sHighestCost";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        super.notifyForNewRound(startLocation);
        if(usedSet.size() >= localGraph.vertexCnt()){
            usedSet.clear();
        }
    }

    @Override
    protected boolean hideAtCurrNode() {
        boolean hide = super.hideAtCurrNode();
        if(hide){
            usedSet.add(currPos);
        }
        return hide;
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        HighestCostPreference preference = new HighestCostPreference(localGraph, graphController, usedSet);
        return preference.getPreferenceTargets(hideNumber);
    }

}
