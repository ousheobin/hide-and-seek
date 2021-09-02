package ac.kcl.inf.has.agent.strategies.preference;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class LeastConnectedPreference extends PreferenceMechanism{

    private int lastEdgeCount = -1;
    private List<Vertex> leastConnectedNodes;
    private final Set<Vertex> usedSet;

    public LeastConnectedPreference(LocalGraph localGraph, GraphController graphController, Set<Vertex> usedSet) {
        super(localGraph, graphController);
        this.leastConnectedNodes = new ArrayList<>();
        this.usedSet = usedSet;
    }

    @Override
    public List<Vertex> getPreferenceTargets(int hideNumber) {
        if ( localGraph.getLocalGraph().edgeSet().size() == lastEdgeCount ){
            return new ArrayList<>(leastConnectedNodes);
        }

        leastConnectedNodes.clear();

        Map<Integer, List<Vertex>> nodesToConnections = new HashMap<>();
        int n;

        for (Vertex potentialNode : localGraph.getLocalGraph().vertexSet()){
            n = localGraph.connectEdges(potentialNode).size();
            List<Vertex> vertices = nodesToConnections.getOrDefault(n,new ArrayList<>());
            vertices.add(potentialNode);
            nodesToConnections.put(n,vertices);
        }

        Set<Vertex> cumulativeConnectedNodes = new HashSet<>();
        int[] connectedCnts = new int[nodesToConnections.keySet().size()];
        int index = 0;
        for (int connectedCnt : nodesToConnections.keySet()){
            connectedCnts[index ++ ] = connectedCnt;
        }

        Arrays.sort(connectedCnts);
        for (index = 0 ; index < connectedCnts.length ; index ++){
            n = connectedCnts[index];
            if( n > 0 ){
                for ( Vertex connectedNode : nodesToConnections.get(n) ) {
                    if ( !usedSet.contains(connectedNode) ) {
                        cumulativeConnectedNodes.add(connectedNode);
                    }
                }
            }

            if ( cumulativeConnectedNodes.size() >= ( hideNumber - usedSet.size() )){
                leastConnectedNodes.addAll(cumulativeConnectedNodes);
                break;
            }
        }

        if(leastConnectedNodes.isEmpty()){
            leastConnectedNodes.addAll(graphController.generateRandomHideLocation(hideNumber));
        }

        lastEdgeCount = localGraph.getLocalGraph().edgeSet().size();
        Collections.shuffle(leastConnectedNodes);
        return new ArrayList<>(leastConnectedNodes);
    }

}
