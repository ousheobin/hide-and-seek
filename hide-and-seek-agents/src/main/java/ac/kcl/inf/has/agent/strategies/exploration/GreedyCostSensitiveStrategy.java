package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.env.graph.Edge;

import java.util.ArrayList;
import java.util.List;

public class GreedyCostSensitiveStrategy extends GreedyStrategy {

    public List<Edge> getEdges(){
        List<Edge> edges = new ArrayList<>(localGraph.getLocalGraph().edgesOf(currPos));
        edges.sort((e1, e2)-> {
            double w1 = Math.max(0.000001,e1.getAnotherSide(currPos).getWeight()) / e1.getWeight();
            double w2 = Math.max(0.000001,e2.getAnotherSide(currPos).getWeight()) / e2.getWeight();
            return -1 * Double.compare(w1,w2);
        });
        return edges;
    }

}
