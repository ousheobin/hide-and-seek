package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class InverseHighProbability extends HighProbability {
    
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

        Map<Integer, List<Vertex>> distanceAndNodes = new HashMap<>();
        List<Vertex> unreachableNodes = new ArrayList<>();
        List<Vertex> ret = new ArrayList<>();

        numberOfNodes = Math.min(numberOfNodes, potentialNodes.size());
        potentialNodes = potentialNodes.subList(0, numberOfNodes);

        DijkstraShortestPath<Vertex, Edge> dsp;
        GraphPath<Vertex,Edge> graphPath;

        for ( Vertex targetNode : potentialNodes ) {

            if ( !localGraph.containVertex(currentPos) || !localGraph.containVertex(targetNode) ) {
                unreachableNodes.add(targetNode);
                continue;
            }

            dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph() );
            graphPath = dsp.getPath(currentPos, targetNode);

            if ( graphPath.getEdgeList() == null || graphPath.getEdgeList().size() == 0 ) {
                unreachableNodes.add(targetNode);
            } else {
                int distance = graphPath.getEdgeList().size();
                List<Vertex> nodes = distanceAndNodes.getOrDefault(distance, new ArrayList<>());
                nodes.add(targetNode);
                distanceAndNodes.put(distance, nodes);
            }

        }

        int[] distanceArr = new int[distanceAndNodes.keySet().size()];
        int index = 0;
        for (Integer distance : distanceAndNodes.keySet()){
            distanceArr[index ++] = distance;
        }

        for (index = 0 ; index < distanceArr.length ; index ++ ){
            ret.addAll(distanceAndNodes.get(distanceArr[index]));
        }

        ret.addAll(unreachableNodes);

        return ret;

    }

}
