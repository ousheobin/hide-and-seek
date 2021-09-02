package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.env.graph.Edge;

import java.util.Collections;
import java.util.List;

public class DFSGreedyStrategy extends DFSStrategy{

    @Override
    protected List<Edge> getConnectedEdges() {
        List<Edge> edges = super.getConnectedEdges();
        Collections.sort(edges);
        return edges;
    }

}
