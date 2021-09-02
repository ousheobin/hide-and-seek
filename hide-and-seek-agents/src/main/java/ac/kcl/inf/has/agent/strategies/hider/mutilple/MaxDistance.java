package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.agent.strategies.preference.MaxDistancePreference;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.List;

public class MaxDistance extends PreferenceHidingStrategy {

    public MaxDistance(int hideNumber, double graphPortion) {
        super(hideNumber, graphPortion);
    }

    @Override
    public String getName() {
        return "hMaxDistance";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        MaxDistancePreference preference = new MaxDistancePreference(localGraph, graphController);
        return preference.getPreferenceTargets(hideNumber);
    }

}
