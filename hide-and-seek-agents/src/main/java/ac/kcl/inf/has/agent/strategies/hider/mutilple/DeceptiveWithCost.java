package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.hider.PreferenceHidingStrategy;
import ac.kcl.inf.has.agent.strategies.preference.HighestCostPreference;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class DeceptiveWithCost extends PreferenceHidingStrategy {

    private List<Vertex> deceptiveSet;
    private List<Vertex> highestNode;
    private Set<Vertex> highestUsed;
    private int currentRound;
    private int deceptiveRounds;

    public DeceptiveWithCost(int hideNumber, int deceptiveRounds, double graphPortion) {
        super(hideNumber, graphPortion);
        currentRound = 0;
        this.deceptiveRounds = deceptiveRounds;
        this.highestUsed = new HashSet<>();
    }

    @Override
    public String getName() {
        return "hDeceptiveWithCost";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        currentRound ++;
        if(deceptiveSet == null){
            List<Vertex> allNodes = new ArrayList<>(localGraph.getLocalGraph().vertexSet());
            allNodes.sort(Comparator.comparingDouble(Vertex::getWeight));
            deceptiveSet = new ArrayList<>(allNodes.subList(0, Math.min(allNodes.size(), hideNumber)));
        }
        if(currentRound < deceptiveRounds){
            return new ArrayList<>(deceptiveSet);
        }else{
            if(highestUsed.size() >= localGraph.vertexCnt() - hideNumber - deceptiveSet.size()){
                highestUsed.clear();
                highestUsed.addAll(deceptiveSet);
            }
            HighestCostPreference preference = new HighestCostPreference(localGraph,graphController,highestUsed,true);
            highestNode = preference.getPreferenceTargets(hideNumber);
            highestUsed.addAll(highestNode);
            return new ArrayList<>(highestNode);
        }
    }

}
