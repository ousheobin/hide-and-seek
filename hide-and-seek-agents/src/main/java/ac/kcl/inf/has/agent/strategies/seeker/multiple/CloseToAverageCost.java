package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.strategies.seeker.PreferenceSeekingStrategy;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class CloseToAverageCost extends PreferenceSeekingStrategy {

    private double averageCost = 0;
    private Map<Vertex, Integer> frequency;

    public CloseToAverageCost(double graphPortion) {
        super(graphPortion);
        frequency = new HashMap<>();
    }

    @Override
    public String getName() {
        return "CloseToAverageCost";
    }

    @Override
    public List<Vertex> computeTargetNodes() {
        List<Vertex> targets = new ArrayList<>();
        if(frequency.isEmpty()){
            return targets;
        }

        int totalScale = 0;
        for (Vertex v: frequency.keySet()){
            if(v.getWeight() == -1){
                continue;
            }
            totalScale += frequency.get(v);
        }

        double avg = 0;
        for (Vertex v: frequency.keySet()){
            if(v.getWeight() == -1){
                continue;
            }
            avg += ((double) frequency.get(v) / totalScale) * v.getWeight();
        }

        List<Pair<Vertex, Double>> distanceToAvg = new ArrayList<>();
        for (Vertex v: frequency.keySet()) {
            if (v.getWeight() == -1) {
                continue;
            }
            distanceToAvg.add(new Pair<>(v, Math.abs(v.getWeight() - avg)));
        }
        distanceToAvg.sort(Comparator.comparingDouble(Pair::getObject2));
        for (Pair<Vertex, Double> pair : distanceToAvg){
            targets.add(pair.getObject1());
            if(targets.size() >= estimatedHideNumber){
                break;
            }
        }
        return targets;
    }

}
