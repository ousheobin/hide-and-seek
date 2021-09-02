package ac.kcl.inf.has.agent.knowledge;

import ac.kcl.inf.has.env.common.Pair;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.GameGraph;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.*;

public class LocalGraph {

    private GameGraph localGraph;

    public LocalGraph(){
        this.newGraph();
    }

    public void newGraph(){
        this.localGraph = new GameGraph();
    }

    public boolean containVertex(Vertex vertex){
        return this.localGraph.containsVertex(vertex);
    }

    public void addVertex(Vertex vertex){
        Vertex mappedVertex = mapVertex(vertex);
        if(mappedVertex == null){
            this.localGraph.addVertex(vertex);
        }
    }

    public void updateGraph(Vertex curr, Set<Pair<Vertex, Double>> connectedSet){
        for (Pair<Vertex,Double> pair: connectedSet){
            addEdge(curr,pair.getObject1(),pair.getObject2());
        }
    }

    public void addEdge(Vertex source, Vertex target,double weight){
        if(source == null || target == null || weight < 0){
            return;
        }
        Vertex mappedSource = mapVertex(source);
        Vertex mappedTarget = mapVertex(target);
        if(mappedSource == null){
            addVertex(source);
            mappedSource = source;
        }
        if(mappedTarget == null){
            addVertex(target);
            mappedTarget = target;
        }
        if(!localGraph.containsEdge(mappedSource, mappedTarget) &&
                !this.localGraph.containsEdge(mappedTarget,mappedSource) ){
            Edge edge = this.localGraph.addEdge(mappedSource, mappedTarget);
            if(edge != null){
                localGraph.setEdgeWeight(edge, weight);
            }
        }
    }

    public void updateVertex(Vertex target){
        Vertex mapped = mapVertex(target);
        if(mapped != null && mapped != target){
            mapped.setWeight(target.getWeight());
        }
    }

    public GameGraph getLocalGraph() {
        return localGraph;
    }

    public Vertex mapVertex(Vertex v){
        if(v == null ){
            return null;
        }
        Set<Vertex> vertexSet = this.localGraph.vertexSet();
        for (Vertex vertex : vertexSet){
            if(vertex.getIndex() == v.getIndex()){
                return vertex;
            }
        }
        return null;
    }

    public Set<Edge> connectEdges(Vertex vertex){
        return this.localGraph.edgesOf(vertex);
    }

    public Vertex randomSelectConnectedVertex(Vertex vertex){
        List<Edge> edges = new ArrayList<>(this.localGraph.edgesOf(vertex));
        Collections.shuffle(edges);
        if(edges.isEmpty()){
            return null;
        }
        return edges.get((int)(Math.random() * edges.size())).getAnotherSide(vertex);
    }

    public int vertexCnt(){
        return localGraph.vertexSet().size();
    }
}
