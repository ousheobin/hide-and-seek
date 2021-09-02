package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.strategies.preference.HighestCostPreference;
import ac.kcl.inf.has.agent.strategies.preference.PreferenceMechanism;
import ac.kcl.inf.has.agent.strategies.seeker.PreferenceSeekingStrategy;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.HashSet;
import java.util.List;

public class HighestCostFirst extends PreferenceSeekingStrategy {

    private PreferenceMechanism mechanism;

    public HighestCostFirst(double graphPortion) {
        super(graphPortion);
    }

    @Override
    public String getName() {
        return "sHighestCostFirst";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        if(mechanism == null){
            mechanism = new HighestCostPreference(localGraph, graphController, new HashSet<>());
        }
        return mechanism.getPreferenceTargets(estimatedHideNumber);
    }

}
