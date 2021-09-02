package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;

import java.util.*;
import java.util.stream.Collectors;

public class CostSensitiveFirstStrategy implements ExplorationStrategy{

    protected GraphController graphController;
    protected LocalGraph localGraph;

    protected Vertex currentPos;
    protected Set<Vertex> visitedNode;
    protected Set<Vertex> pendingSet;

    protected List<Edge> currentPath;

    public CostSensitiveFirstStrategy(){
        visitedNode = new HashSet<>();
        pendingSet = new HashSet<>();
    }

    @Override
    public Vertex nextNode() {
        visitedNode.add(currentPos);
        pendingSet.remove(currentPos);

        localGraph.updateGraph(currentPos,graphController.getConnectedVertex(currentPos));

        Set<Edge> connectEdges = localGraph.connectEdges(currentPos);

        boolean addNewNodes = false;
        for (Edge edge : connectEdges){
            if(!visitedNode.contains(edge.getAnotherSide(currentPos))){
                pendingSet.add(edge.getAnotherSide(currentPos));
                addNewNodes = true;
            }
        }

        if(!addNewNodes && !currentPath.isEmpty()){
            currentPos = currentPath.remove(0).getAnotherSide(currentPos);
            return currentPos;
        }

        FloydWarshallShortestPaths<Vertex,Edge> fws = new FloydWarshallShortestPaths<>(localGraph.getLocalGraph());
        double minWeight = Double.MAX_VALUE;
        for (Vertex vertex : pendingSet){
            GraphPath<Vertex, Edge> path = fws.getPath(currentPos, vertex);
            if(path.getEdgeList() == null || path.getEdgeList().isEmpty()){
                continue;
            }
            List<Vertex> unvisitedListInPath = path.getVertexList()
                    .stream()
                    .filter(v-> !visitedNode.contains(v))
                    .collect(Collectors.toList());
            double totalWeight = path.getWeight() + unvisitedListInPath
                    .stream()
                    .mapToDouble(Vertex::getWeight)
                    .map(v -> Math.max(0,v))
                    .sum();
            double avgWeight = totalWeight / (unvisitedListInPath.size() + 1);
            if(avgWeight <= minWeight){
                minWeight = avgWeight;
                currentPath = path.getEdgeList();
            }
        }

        if(currentPath == null){
            currentPos =  localGraph.randomSelectConnectedVertex(currentPos);
        }else {
            currentPath = new ArrayList<>(currentPath);
            currentPos = currentPath.remove(0).getAnotherSide(currentPos);
        }

        return currentPos;
    }

    @Override
    public void startNode(Vertex vertex) {
        currentPos = vertex;
        visitedNode.clear();
        pendingSet.clear();
    }

    @Override
    public void setGraphController(GraphController graphController) {
        this.graphController = graphController;
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        this.localGraph = localGraph;
    }

    @Override
    public void updateCurrentPos(Vertex currentPos) {
        localGraph.updateGraph(currentPos,graphController.getConnectedVertex(currentPos));
        this.currentPos = currentPos;
        visitedNode.add(currentPos);
        pendingSet.remove(currentPos);
    }
}
