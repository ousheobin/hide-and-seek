package ac.kcl.inf.has.env.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.concurrent.atomic.AtomicInteger;

public class Edge extends DefaultWeightedEdge implements Comparable<Edge> {

    public Edge() {

    }

    @Override
    public double getWeight() {
        return super.getWeight();
    }

    public int compareTo(Edge edge) {
        if(edge == null){
            return 1;
        }
        if(edge == this){
            return 0;
        }
        return Double.compare(this.getWeight(), edge.getWeight());
    }

    public Vertex getAnotherSide(Vertex v){
        if(getTarget().equals(v)){
            return getSource();
        }else if(getSource().equals(v)){
            return getTarget();
        }
        throw new IllegalArgumentException("Not found " + v + " in " + this);
    }

    public Vertex getSource() {
        return (Vertex) super.getSource();
    }

    public Vertex getTarget() {
        return (Vertex) super.getTarget();
    }

    public boolean partOfEdgs(Vertex v){
        return (getSource().equals(v)) || (getTarget().equals(v));
    }

    @Override
    public String toString() {
        return "(v" + getSource().getIndex() + ", v"+ getTarget().getIndex() +", "+ getWeight() +")";
    }
}
