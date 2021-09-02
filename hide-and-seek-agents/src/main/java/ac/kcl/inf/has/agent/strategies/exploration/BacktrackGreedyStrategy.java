package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BacktrackGreedyStrategy implements ExplorationStrategy{

    protected GraphController graphController;

    protected Set<Vertex> unvisitedNodes;
    protected Set<Vertex> visitedNodes;
    protected List<Edge> currentPath;

    protected Vertex currentPos;
    protected LocalGraph localGraph;

    public BacktrackGreedyStrategy(){
        unvisitedNodes = new HashSet<>();
        visitedNodes = new HashSet<>();
        currentPath = new ArrayList<>();
    }

    @Override
    public Vertex nextNode() {

        if(graphController == null || localGraph == null){
            throw new RuntimeException("graphController or localGraph is null");
        }

        // Update local graph
        internalUpdate();

        if(currentPath.isEmpty()){

            if(unvisitedNodes.isEmpty()){
                currentPos = localGraph.randomSelectConnectedVertex(currentPos);
                return currentPos;
            }

            Vertex closestNode = selectNextNode();

            DijkstraShortestPath dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
            GraphPath<Vertex,Edge> path = dsp.getPath(currentPos, closestNode);

            if ( path != null && path.getEdgeList().isEmpty()) {
                currentPos = localGraph.randomSelectConnectedVertex(currentPos);
                return currentPos;
            }

            if(path != null){
                currentPath = path.getEdgeList();
            }

        }

        currentPos = currentPath.remove(0).getAnotherSide(currentPos);

        return currentPos;
    }

    public Vertex selectNextNode(){
        Vertex closestNode = (Vertex) unvisitedNodes.toArray()[0];
        double shortestDistance = Double.MAX_VALUE;

        DijkstraShortestPath<Vertex, Edge> dsp;
        GraphPath<Vertex,Edge> path;

        for ( Vertex unvisitedNode : unvisitedNodes ) {

            dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
            path = dsp.getPath(currentPos, unvisitedNode);

            if ( path.getWeight() < shortestDistance ) {

                closestNode = unvisitedNode;
                shortestDistance = path.getWeight();

            }

        }
        return closestNode;
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
    public void startNode(Vertex vertex) {
        currentPos = vertex;
        currentPath.clear();
        visitedNodes.clear();
        unvisitedNodes.clear();
        unvisitedNodes.addAll(localGraph.getLocalGraph().vertexSet());
    }

    public Set<Vertex> getUnvisitedNodes() {
        return unvisitedNodes;
    }

    public Set<Vertex> getVisitedNodes() {
        return visitedNodes;
    }

    public Vertex getCurrentPos() {
        return currentPos;
    }

    @Override
    public void updateCurrentPos(Vertex currentPos) {
        currentPath.clear();
        this.currentPos = currentPos;
        internalUpdate();
    }

    private void internalUpdate(){
        localGraph.updateGraph(currentPos,graphController.getConnectedVertex(currentPos));

        unvisitedNodes.remove(currentPos);

        visitedNodes.add(currentPos);

        for (Edge edge: localGraph.connectEdges(currentPos)){
            Vertex vertex = edge.getAnotherSide(currentPos);
            if(!visitedNodes.contains(vertex)){
                unvisitedNodes.add(vertex);
            }
        }
    }
}
