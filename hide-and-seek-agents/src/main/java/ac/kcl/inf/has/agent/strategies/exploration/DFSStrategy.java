package ac.kcl.inf.has.agent.strategies.exploration;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class DFSStrategy implements ExplorationStrategy {

    private GraphController graphController;
    private LocalGraph localGraph;
    private Vertex currPos;

    private Stack<Vertex> visitStack;
    private Set<Vertex> visitedSet;

    public DFSStrategy(){
        visitedSet = new HashSet<>();
        visitStack = new Stack<>();
    }

    @Override
    public Vertex nextNode() {

        // Update local graph
        localGraph.updateGraph(currPos,graphController.getConnectedVertex(currPos));

        List<Edge> localEdges = getConnectedEdges();
        Vertex nextVertex = null;
        Vertex tmp = null;
        for (Edge edge: localEdges){
            tmp = edge.getAnotherSide(currPos);
            if(!visitedSet.contains(tmp)){
                nextVertex = tmp;
                break;
            }
        }

        if(nextVertex == null){
            if(this.visitStack.size() > 1){
                nextVertex = this.visitStack.pop();
            }else{
                nextVertex = this.visitStack.peek();
            }
        }else{
            this.visitStack.push(currPos);
            this.visitedSet.add(nextVertex);
        }

        currPos = nextVertex;

        return currPos;
    }

    protected List<Edge> getConnectedEdges(){
        return new ArrayList<>(localGraph.connectEdges(currPos));
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void startNode(Vertex vertex) {
        this.currPos = vertex;

        this.visitedSet.clear();
        this.visitStack.clear();

        this.visitedSet.add(vertex);
    }

}
