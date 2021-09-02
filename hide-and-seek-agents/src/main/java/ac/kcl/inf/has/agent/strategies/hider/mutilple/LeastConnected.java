package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.agent.strategies.preference.LeastConnectedPreference;
import ac.kcl.inf.has.agent.strategies.preference.PreferenceMechanism;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.List;

public class LeastConnected extends PreferenceHidingStrategy {

    private PreferenceMechanism mechanism;

    public LeastConnected(int hideNumber, double graphPortion) {
        super(hideNumber, graphPortion);
    }

    @Override
    public String getName() {
        return "hLeastConnected";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        if(mechanism == null){
            mechanism = new LeastConnectedPreference(localGraph,graphController,hiddenSet);
        }
        return mechanism.getPreferenceTargets(hideNumber);
    }

    @Override
    protected boolean hideAtCurrNode() {
        localGraph.updateGraph(currPos, graphController.getConnectedVertex(currPos));
        if ( localGraph.connectEdges(currPos).size() == 1 && !hiddenSet.contains(currPos)) {
            addTargetNode(currPos);
        }
        return super.hideAtCurrNode();
    }

}
