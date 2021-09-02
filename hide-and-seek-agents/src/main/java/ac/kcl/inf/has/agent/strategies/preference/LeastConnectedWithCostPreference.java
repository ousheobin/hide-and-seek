package ac.kcl.inf.has.agent.strategies.preference;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeastConnectedWithCostPreference extends PreferenceMechanism {

    private int lastEdgeCount = -1;
    private List<Vertex> leastConnectedNodes;

    public LeastConnectedWithCostPreference(LocalGraph localGraph, GraphController graphController) {
        super(localGraph, graphController);
        leastConnectedNodes = new ArrayList<>();
    }

    @Override
    public List<Vertex> getPreferenceTargets(int hideNumber) {
        if ( localGraph.getLocalGraph().edgeSet().size() == lastEdgeCount ){
            return new ArrayList<>(leastConnectedNodes);
        }

        leastConnectedNodes.clear();

        int n;
        List<Pair<Vertex, Double>> nodes = new ArrayList<>();

        for (Vertex potentialNode : localGraph.getLocalGraph().vertexSet()){
            n = localGraph.connectEdges(potentialNode).size();
            nodes.add(new Pair<>(potentialNode, n / potentialNode.getWeight()));
        }

        nodes.sort(Comparator.comparingDouble(Pair::getObject2));

        if(nodes.isEmpty() || nodes.size() < hideNumber){
            leastConnectedNodes = new ArrayList<>(graphController.generateRandomHideLocation(hideNumber));
        }else{
            leastConnectedNodes = nodes.subList(0,hideNumber).stream().map(Pair::getObject1).collect(Collectors.toList());
            Collections.shuffle(leastConnectedNodes);
        }

        return new ArrayList<>(leastConnectedNodes);
    }

}
