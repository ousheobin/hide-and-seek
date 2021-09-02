package ac.kcl.inf.has.env.graph;

import java.util.Objects;

public class Vertex implements Comparable<Vertex> {

    private int index;
    private double weight;

    public Vertex(int index) {
        this.index = index;
        weight = 0;
    }

    public Vertex(int index, double weight) {
        this.index = index;
        this.weight = weight;
    }

    public int getIndex() {
        return index;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public int compareTo(Vertex target) {
        if(target == null){
            return 1;
        }
        if(this == target || this.index == target.index){
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return index == vertex.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return "v" + index + "(w:" + weight + ')';
    }

}
