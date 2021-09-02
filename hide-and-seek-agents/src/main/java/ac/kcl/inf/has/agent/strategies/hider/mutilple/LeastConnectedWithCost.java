package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.agent.strategies.preference.LeastConnectedPreference;
import ac.kcl.inf.has.agent.strategies.preference.LeastConnectedWithCostPreference;
import ac.kcl.inf.has.agent.strategies.preference.PreferenceMechanism;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.List;

public class LeastConnectedWithCost extends PreferenceHidingStrategy {

    private PreferenceMechanism mechanism;

    public LeastConnectedWithCost(int hideNumber, double graphPortion) {
        super(hideNumber, graphPortion);
    }

    @Override
    public String getName() {
        return "hLeastConnectedWithCost";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        if(mechanism == null){
            mechanism = new LeastConnectedWithCostPreference(localGraph,graphController);
        }
        return mechanism.getPreferenceTargets(hideNumber);
    }

}
