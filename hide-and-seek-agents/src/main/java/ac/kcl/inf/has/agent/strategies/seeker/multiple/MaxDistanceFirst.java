package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.preference.MaxDistancePreference;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.PreferenceSeekingStrategy;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.List;

public class MaxDistanceFirst extends PreferenceSeekingStrategy {

    public MaxDistanceFirst(double graphPortion) {
        super(graphPortion);
    }

    @Override
    public String getName() {
        return "sMaxDistanceFirst";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        MaxDistancePreference preference = new MaxDistancePreference(localGraph,graphController);
        return preference.getPreferenceTargets(estimatedHideNumber);
    }
}
