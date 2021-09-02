package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class RandomStrategy implements ExplorationStrategy{

    private GraphController graphController;
    private LocalGraph localGraph;
    private Vertex currPos;

    private Set<Vertex> visitedNodes;

    public RandomStrategy(){
        this.visitedNodes = new HashSet<>();
    }

    @Override
    public Vertex nextNode() {
        if(graphController == null || localGraph == null){
            throw new RuntimeException("graphController or localGraph is null");
        }

        // Update local graph
        localGraph.updateGraph(currPos,graphController.getConnectedVertex(currPos));

        this.visitedNodes.add(currPos);

        List<Edge> edges = new ArrayList<>(localGraph.getLocalGraph().edgesOf(currPos));

        Vertex nextNode = null;
        edges.removeIf(edge -> this.visitedNodes.contains(edge.getAnotherSide(currPos)));

        if(!edges.isEmpty()){
            nextNode = edges.get((int) (Math.random() * edges.size()) ).getAnotherSide(currPos);
        }

        if(nextNode == null){
            nextNode = localGraph.randomSelectConnectedVertex(currPos);
        }

        currPos = nextNode;

        return currPos;
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
        this.visitedNodes.clear();
    }

}
