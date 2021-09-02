package ac.kcl.inf.has.env.controller;

import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.GameGraph;
import ac.kcl.inf.has.env.graph.Vertex;
import ac.kcl.inf.has.env.graph.generation.GraphGenerateStrategies;
import ac.kcl.inf.has.env.graph.generation.PhysicalGraphGenerator;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class GraphController {

    private GameGraph gameGraph;

    private Set<Vertex> hiddenLocations;
    private Set<Vertex> foundedHiddenObjects;

    private int vertexWeightBound;
    private int edgeWeightBound;

    private boolean knowNodeWeightInAdvance = false;

    private Random random;

    private GraphController() {}

    public static GraphController build() {
        return new GraphController();
    }

    public GraphController newGraph(GraphGenerateStrategies strategies, int vertex,
                                    int vertexWeightBound, int edgeWeightBound) {
        GameGraph gameGraph;
        ConnectivityInspector<Vertex,Edge> con;
        this.random = new Random(System.currentTimeMillis());
        while (true){
            gameGraph = new GameGraph(vertexWeightBound);
            PhysicalGraphGenerator.generateGraph(gameGraph, strategies, vertex, edgeWeightBound);
            con = new ConnectivityInspector<>(gameGraph);
            if(con.isConnected()){
                break;
            }
            this.vertexWeightBound = vertexWeightBound;
            this.edgeWeightBound = edgeWeightBound;
//            System.out.println("Graph is not connected, try to generate it again.");
        }
        this.gameGraph = gameGraph;
        return this;
    }

    public GraphController knowNodeWeightInAdvance(boolean canKnow){
        this.knowNodeWeightInAdvance = canKnow;
        return this;
    }

//    public GameGraph getGameGraph() {
//        return gameGraph;
//    }

    public int getVertexCount(){
        return gameGraph.vertexSet().size();
    }

    public void newRound(){
        this.initEnvironment();
    }

    private void initEnvironment(){
        this.hiddenLocations = new HashSet<>();
        this.foundedHiddenObjects = new HashSet<>();
    }

    public void hideObject(Vertex vertex){
        if(vertex != null){
            this.hiddenLocations.add(vertex);
        }
    }

    public boolean allFound(){
        if(this.foundedHiddenObjects.size() == this.hiddenLocations.size()){
            return true;
        }
        return false;
    }

    public boolean discoverNode(Vertex node){
        boolean contains = false;
        if(hiddenLocations.size() > 0){
            for (Vertex v: hiddenLocations){
                if(v.getIndex() == node.getIndex()){
                    contains = true;
                    break;
                }
            }
        }
        if(contains){
            foundedHiddenObjects.add(node);
        }
        return contains;
    }

    public Set<Pair<Vertex, Double>> getConnectedVertex(Vertex vertex){
        Set<Pair<Vertex, Double>> nodeSet = new HashSet<>();
        Set<Edge> edges = this.gameGraph.edgesOf(vertex);
        for (Edge edge: edges){
            Vertex target = edge.getAnotherSide(vertex);
            if(target == null){
                continue;
            }
            double weight = -1;
            if(knowNodeWeightInAdvance){
                weight = target.getWeight();
            }
            Vertex clonedTarget = new Vertex(target.getIndex(), weight);
            nodeSet.add(new Pair<>(clonedTarget,edge.getWeight()));
        }
        return nodeSet;
    }

    public double edgeWeight(Vertex source, Vertex target){

        Vertex mappedSource = mapVertex(source);
        Vertex mappedTarget = mapVertex(target);

        if(mappedSource == null || mappedTarget == null){
            return -1;
        }

        Edge edge = this.gameGraph.getEdge(mappedSource, mappedTarget);
        if(edge == null){
            edge = this.gameGraph.getEdge(mappedTarget, mappedSource);
        }
        return edge == null ? - 1: edge.getWeight();
    }


    public double requestCostAtNode(Vertex current){
        Vertex v = mapVertex(current);
        if(v == null){
            return -1;
        }
        return v.getWeight();
    }

    public Vertex randomStartPoint(){
        List<Vertex> vertices = new ArrayList<>(gameGraph.vertexSet());
        int size = vertices.size();
        int index = Math.round(random.nextFloat() * size);
        index = Math.max(Math.min(size - 1,index),0);
        Vertex v = vertices.get(index);
        Vertex ret = new Vertex(v.getIndex(), v.getWeight());
        return ret;
    }

    public List<Vertex> travelToNewStaringPoint(Vertex last, Vertex newLocation){
        DijkstraShortestPath<Vertex, Edge> dsp = new DijkstraShortestPath<>(gameGraph);
        GraphPath<Vertex,Edge> path = dsp.getPath(last, newLocation);
        List<Vertex> pathList = new ArrayList<>();
        for (Vertex vertex : path.getVertexList()){
            pathList.add(new Vertex(vertex.getIndex(),vertex.getWeight()));
        }
        return pathList;
    }

    private Vertex mapVertex(Vertex agentsVertex){
        for (Vertex v : gameGraph.vertexSet()) {
            if (v.getIndex() == agentsVertex.getIndex()) {
                return v;
            }
        }
        return null;
    }

    public List<Vertex> generateRandomHideLocation(int count){
        return generateRandomHideLocation(count, new HashSet<>());
    }

    public List<Vertex> generateRandomHideLocation(int count, Set<Vertex> excludeSet){
        if(excludeSet == null){
            throw new IllegalArgumentException("Exclude set is null");
        }
        if(count < 1){
            throw new IllegalArgumentException("Count less than 1");
        }
        Random random = new Random(System.currentTimeMillis());
        List<Vertex> vertices = new ArrayList<>(gameGraph.vertexSet());
        HashSet<Vertex> ret = new HashSet<>();
        Vertex tmp = null;
        while (ret.size() < count){
            int next = (int) (random.nextDouble() * vertices.size());
            tmp = vertices.get(next);
            if(excludeSet.contains(tmp) || ret.contains(tmp)){
                continue;
            }
            ret.add(tmp);
            vertices.remove(next);
        }
        return new ArrayList<>(ret);
    }

    public int degreeOf(Vertex vertex){
        vertex = mapVertex(vertex);
        return gameGraph.degreeOf(vertex);
    }

    public int getEdgeWeightBound() {
        return edgeWeightBound;
    }

    public int getVertexWeightBound() {
        return vertexWeightBound;
    }
}
