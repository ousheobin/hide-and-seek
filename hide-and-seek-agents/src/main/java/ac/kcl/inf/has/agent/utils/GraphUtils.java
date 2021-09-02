package ac.kcl.inf.has.agent.utils;

import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.GameGraph;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphUtils {

    /**
     * An O(n^2) algorithm to calculate all possible path from the game graph.
     * @param gameGraph the graph need to be processed
     * @return all paths
     */
    public static List<GraphPath<Vertex,Edge>> extractAllPossiblePaths(GameGraph gameGraph){

        List<GraphPath<Vertex,Edge>> paths = new ArrayList<>();
        FloydWarshallShortestPaths<Vertex, Edge> fwsp = new FloydWarshallShortestPaths<>(gameGraph);
        Set<Vertex> vertices = gameGraph.vertexSet();

        for (Vertex source : vertices){
            for (Vertex sink : vertices){
                if(source.equals(sink)){
                    continue;
                }
                GraphPath<Vertex, Edge> path = fwsp.getPath(source, sink);
                if (path != null) {
                    paths.add(path);
                }
            }
        }
        return paths;
    }

}
