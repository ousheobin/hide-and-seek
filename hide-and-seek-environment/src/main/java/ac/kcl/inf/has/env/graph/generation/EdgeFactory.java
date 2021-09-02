package ac.kcl.inf.has.env.graph.generation;

import ac.kcl.inf.has.env.graph.Edge;

import java.util.function.Supplier;

public class EdgeFactory implements Supplier<Edge> {

    @Override
    public Edge get() {
        return new Edge();
    }

}
