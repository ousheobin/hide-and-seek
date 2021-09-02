package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class HighProbabilityWithCost extends HighProbability {

    protected List<Vertex> selectNodes(){
        List<Vertex> selectedNodes = new ArrayList<>();
        if(frequency.isEmpty()){
            // The first round, just discover follow the exploration strategy.
            return selectedNodes;
        }

        int totalScale = 0;
        Map<Vertex, Double> grade = new HashMap<>();
        for (Vertex v: frequency.keySet()){
            totalScale += frequency.get(v);
        }

        for (Vertex v: frequency.keySet()){
            grade.put(v, ((double)frequency.get(v) / totalScale) / Math.max(0.01, v.getWeight()));
        }

        double maxGrade;
        List<Vertex> potentialNodes = new ArrayList<>();
        for (int i = 0 ; i < estimatedHideNumber ; i ++){
            maxGrade = Double.MIN_VALUE;
            potentialNodes.clear();

            for (Vertex v : grade.keySet()){
                if(grade.get(v) > maxGrade){
                    maxGrade = grade.get(v);
                    potentialNodes.clear();
                    potentialNodes.add(v);
                } else if( Double.compare(maxGrade, grade.get(v)) == 0){
                    potentialNodes.add(v);
                }
            }

            Vertex selected = null;
            if(!potentialNodes.isEmpty()){
                selected = potentialNodes.get((int) (Math.random() * potentialNodes.size()));
            }

            selectedNodes.add(selected);
            grade.remove(selected);

        }
        return selectedNodes;
    }

    @Override
    public String getName() {
        return "sHighProbability";
    }

}
