package ac.kcl.inf.has.agent.strategies.preference;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.utils.GraphUtils;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.alg.shortestpath.GraphMeasurer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MaxDistancePreference extends PreferenceMechanism{

    public MaxDistancePreference(LocalGraph localGraph, GraphController graphController) {
        super(localGraph, graphController);
    }

    @Override
    public List<Vertex> getPreferenceTargets(int hideNumber) {

        GraphMeasurer<Vertex, Edge> graphMeasurer;
        List<Vertex> targets = new ArrayList<>();

        graphMeasurer = new GraphMeasurer<>(localGraph.getLocalGraph());
        List<GraphPath<Vertex, Edge>> allPaths = GraphUtils.extractAllPossiblePaths(localGraph.getLocalGraph());

        FloydWarshallShortestPaths<Vertex, Edge> fwsp = new FloydWarshallShortestPaths<>(localGraph.getLocalGraph());
        GraphPath<Vertex, Edge> path;

        double maxDiameter = graphMeasurer.getDiameter();

        for (GraphPath<Vertex, Edge> graphPath : allPaths) {
            if (graphPath.getWeight() >= maxDiameter) {
                if(!targets.contains(graphPath.getStartVertex())){
                    targets.add(graphPath.getStartVertex());
                }
                if(!targets.contains(graphPath.getEndVertex())){
                    targets.add(graphPath.getEndVertex());
                }
            }
        }


        while (targets.size() < hideNumber && maxDiameter > -1) {
            loop:
            for (Vertex potentialNode : localGraph.getLocalGraph().vertexSet()) {
                if (targets.contains(potentialNode)) {
                    continue;
                }
                for (Vertex existingNode : targets) {
                    path = fwsp.getPath(potentialNode, existingNode);
                    if (path.getWeight() >= maxDiameter) {
                        if(!targets.contains(path.getStartVertex())){
                            targets.add(path.getStartVertex());
                        }

                        if(!targets.contains(path.getEndVertex())){
                            targets.add(path.getEndVertex());
                        }

                        continue loop;
                    }
                }
            }
            if (targets.size() >= hideNumber) {
                break;
            }
            maxDiameter -= 1;
        }

        if (targets.size() < hideNumber) {
            targets.addAll(graphController.generateRandomHideLocation(hideNumber - targets.size(), new HashSet<>(targets)));
        }

        return targets;
    }

}
