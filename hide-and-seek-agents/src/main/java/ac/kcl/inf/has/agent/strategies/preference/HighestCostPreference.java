package ac.kcl.inf.has.agent.strategies.preference;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.utils.GraphUtils;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.alg.shortestpath.GraphMeasurer;

import java.util.*;

public class HighestCostPreference extends PreferenceMechanism{

    private int lastEdgeCount = -1;
    private final List<Vertex> leastConnectedNodes;
    private final Set<Vertex> usedSet;

    private boolean considerRouteCost;

    public HighestCostPreference(LocalGraph localGraph, GraphController graphController,
                                 Set<Vertex> usedSet, boolean considerRouteCost) {
        super(localGraph, graphController);
        this.leastConnectedNodes = new ArrayList<>();
        this.usedSet = usedSet;
        this.considerRouteCost = considerRouteCost;
    }

    public HighestCostPreference(LocalGraph localGraph, GraphController graphController, Set<Vertex> usedSet) {
        this(localGraph, graphController, usedSet, false);
    }

    @Override
    public List<Vertex> getPreferenceTargets(int hideNumber) {
        List<Vertex> targets = new ArrayList<>();
        List<Vertex> potentials = new ArrayList<>();

        for (Vertex vertex : localGraph.getLocalGraph().vertexSet()){
            if(vertex.getWeight() > -1 && !usedSet.contains(vertex)){
                potentials.add(vertex);
            }
        }

        if(potentials.isEmpty()){
            return targets;
        }

        if(!considerRouteCost){
            potentials.sort((v1, v2)-> -1 * Double.compare(v1.getWeight(),v2.getWeight()));
            targets.addAll(potentials.subList(0,Math.min(hideNumber, potentials.size())));
        }else {

            if(potentials.size() <= hideNumber){
                targets.addAll(potentials);
                return targets;
            }

            FloydWarshallShortestPaths<Vertex, Edge> fwsp = new FloydWarshallShortestPaths<>(localGraph.getLocalGraph());
            GraphPath<Vertex, Edge> path;

            double maxWeight = Double.MIN_VALUE;
            Vertex maxVertex = null;
            for (Vertex vertex : potentials){
                if(vertex.getWeight() > maxWeight){
                    maxWeight = vertex.getWeight();
                    maxVertex = vertex;
                }
            }

            if(maxVertex != null){
                targets.add(maxVertex);
            }else {
                return targets;
            }

            GraphPath<Vertex, Edge> maxPath;

            outerLoop:
            while (targets.size() < hideNumber){
                for (Vertex potential : potentials){
                    if(targets.contains(potential)){
                        continue;
                    }
                    maxPath = null;
                    maxWeight = Double.MIN_VALUE;
                    for (Vertex existing : targets){
                        path = fwsp.getPath(potential, existing);
                        double weight = (existing.getWeight() + potential.getWeight()) / path.getWeight();
                        if(weight >= maxWeight){
                            maxPath = path;
                            maxWeight = weight;
                        }
                    }

                    if(maxPath != null){
                        if(!targets.contains(maxPath.getStartVertex())){
                            targets.add(maxPath.getStartVertex());
                        }
                        if(!targets.contains(maxPath.getEndVertex())){
                            targets.add(maxPath.getEndVertex());
                        }
                        continue outerLoop;
                    }
                }
            }

        }


        return targets;
    }

}
