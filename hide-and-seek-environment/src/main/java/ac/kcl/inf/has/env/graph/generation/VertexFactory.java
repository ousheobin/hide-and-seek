package ac.kcl.inf.has.env.graph.generation;

import ac.kcl.inf.has.env.graph.Vertex;
import ac.kcl.inf.has.env.math.RandomUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class VertexFactory implements Supplier<Vertex> {


    private AtomicInteger vertexIndex = new AtomicInteger(0);
    private int weightUpperBound;
    private RandomUtil randomUtil;

    public VertexFactory(int weightUpperBound){
        randomUtil = new RandomUtil();
        this.weightUpperBound = weightUpperBound;
    }

    @Override
    public Vertex get() {
        Vertex vertex =  new Vertex(vertexIndex.getAndIncrement());
        vertex.setWeight(randomUtil.getNextRandom(weightUpperBound));
        return vertex;
    }
}
