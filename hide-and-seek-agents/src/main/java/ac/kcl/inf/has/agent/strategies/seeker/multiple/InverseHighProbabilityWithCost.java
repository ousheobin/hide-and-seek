package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;
import java.util.stream.Collectors;

public class InverseHighProbabilityWithCost extends HighProbability {
    
    @Override
    public String getName() {
        return "sInverseHighProbability";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currentPos = startLocation;
        estimatedHideNumber = Math.max(1, foundSet.size());

        currentPath.clear();
        discoveredSet.clear();
        likelyNodes = selectNodes();

        explorationStrategy.startNode(startLocation);
    }

    @Override
    protected List<Vertex> selectNodes() {

        List<Vertex> potentialNodes = new ArrayList<>();

        if(localGraph.getLocalGraph().vertexSet().isEmpty()){
            // First round, discover all nodes with the exploration strategy.
            return potentialNodes;
        }

        if (foundSet.size() >= ( graphController.getVertexCount() )){
            foundSet.clear();
        }

        for ( Vertex vertex : localGraph.getLocalGraph().vertexSet() ) {
            if ( ! foundSet.contains(vertex) ) {
                potentialNodes.add(vertex);
            }
        }

        potentialNodes = orderNodes(Integer.MAX_VALUE, potentialNodes);

        if ( potentialNodes.size() >= estimatedHideNumber ){
            potentialNodes = new ArrayList<Vertex>(potentialNodes.subList(0, estimatedHideNumber));
        }

        return potentialNodes;
    }

    protected List<Vertex> orderNodes(int numberOfNodes, List<Vertex> potentialNodes) {

        List<Vertex> ret = potentialNodes.stream().map((vertex)->{
            DijkstraShortestPath<Vertex, Edge> dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph() );
            GraphPath<Vertex,Edge> graphPath = dsp.getPath(currentPos, vertex);
            if(graphPath.getEdgeList() == null || graphPath.getEdgeList().isEmpty()){
                return new Pair<>(vertex, Math.max(0,vertex.getWeight()));
            }else{
                return new Pair<>(vertex, graphPath.getWeight() + Math.max(0,vertex.getWeight()));
            }
        }).sorted(Comparator.comparingDouble(Pair::getObject2)).map(Pair::getObject1).collect(Collectors.toList());

        return ret.subList(0, Math.min(numberOfNodes, ret.size()));

    }

}
