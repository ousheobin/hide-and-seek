package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class GreedyStrategy implements ExplorationStrategy{

    protected GraphController graphController;
    protected LocalGraph localGraph;
    protected Vertex currPos;

    protected Set<Vertex> visitedNodes;

    public GreedyStrategy(){
        visitedNodes = new HashSet<>();
    }

    @Override
    public Vertex nextNode() {

        if(graphController == null || localGraph == null){
            throw new RuntimeException("graphController or localGraph is null");
        }

        // Update local graph
        localGraph.updateGraph(currPos,graphController.getConnectedVertex(currPos));

        List<Edge> nextEdges = getEdges();

        Vertex next = null;
        for (Edge edge: nextEdges){
            if(!visitedNodes.contains(edge.getAnotherSide(currPos))){
                next = edge.getAnotherSide(currPos);
                visitedNodes.add(next);
                break;
            }
        }

        if(next == null){
            next = localGraph.randomSelectConnectedVertex(currPos);
        }

        currPos = next;

        return currPos;
    }

    public List<Edge> getEdges(){
        List<Edge> edges = new ArrayList<>(localGraph.getLocalGraph().edgesOf(currPos));
        Collections.sort(edges);
        return edges;
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
        visitedNodes.add(currentPos);
        currPos = currentPos;
        localGraph.updateGraph(currPos,graphController.getConnectedVertex(currPos));
    }

    @Override
    public void startNode(Vertex vertex) {
        this.currPos = vertex;
        visitedNodes.clear();
        visitedNodes.add(currPos);
    }

}
