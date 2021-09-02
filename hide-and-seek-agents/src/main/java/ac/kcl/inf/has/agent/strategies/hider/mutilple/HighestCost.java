package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.agent.strategies.preference.HighestCostPreference;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.HashSet;
import java.util.List;

public class HighestCost extends PreferenceHidingStrategy {

    public HighestCost(int hideNumber, double graphPortion) {
        super(hideNumber, graphPortion);
    }

    @Override
    public String getName() {
        return "sHighestCost";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        HighestCostPreference preference = new HighestCostPreference(localGraph,graphController, new HashSet<>(),true);
        return preference.getPreferenceTargets(hideNumber);
    }

}
