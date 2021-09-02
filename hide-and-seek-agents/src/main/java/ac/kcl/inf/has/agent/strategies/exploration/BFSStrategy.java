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

public class BFSStrategy implements ExplorationStrategy{

    private GraphController graphController;
    private LocalGraph localGraph;
    private Vertex currPos;

    private List<Vertex> visitingQueue;
    private List<Edge> currentPath;

    public BFSStrategy(){
        visitingQueue = new ArrayList<>();
        currentPath = new ArrayList<>();
    }

    @Override
    public Vertex nextNode() {

        if(graphController == null || localGraph == null){
            throw new RuntimeException("graphController or localGraph is null");
        }

        // Update local graph
        localGraph.updateGraph(currPos,graphController.getConnectedVertex(currPos));

        List<Edge> edges = getConnectedEdges();

        for (Edge edge: edges){
            Vertex v = edge.getAnotherSide(currPos);
            if(!visitingQueue.contains(v)){
                visitingQueue.add(v);
            }
        }

        if(visitingQueue.isEmpty()){
            currPos = localGraph.randomSelectConnectedVertex(currPos);
            return currPos;
        }

        if(currentPath.isEmpty()){

            Vertex nextVisited = visitingQueue.remove(0);

            while (currPos.equals(nextVisited)){
                nextVisited = visitingQueue.remove(0);
            }

            if(nextVisited == null){
                currPos = localGraph.randomSelectConnectedVertex(currPos);
                return currPos;
            }

            DijkstraShortestPath<Vertex, Edge> dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
            GraphPath<Vertex,Edge> path = dsp.getPath(currPos, nextVisited);

            if ( path == null || path.getEdgeList().isEmpty()) {
                System.out.println("Path is empty");
                currPos = localGraph.randomSelectConnectedVertex(currPos);
                return currPos;
            }

            currentPath = path.getEdgeList();

        }

        currPos = currentPath.remove(0).getAnotherSide(currPos);

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
        this.currentPath.clear();
        this.visitingQueue.clear();
    }

}
