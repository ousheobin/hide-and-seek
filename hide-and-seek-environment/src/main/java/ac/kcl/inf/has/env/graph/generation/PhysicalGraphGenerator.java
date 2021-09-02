package ac.kcl.inf.has.env.graph.generation;

import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import ac.kcl.inf.has.env.math.RandomUtil;
import org.jgrapht.Graph;
import org.jgrapht.generate.*;

public class PhysicalGraphGenerator {

    public static void generateGraph( Graph<Vertex, Edge> graph, GraphGenerateStrategies strategy,
                                      int size, int edgeWeightBound) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        GraphGenerator<Vertex, Edge, Vertex> generator = getGenerator(strategy, size);
        if (generator == null) {
            throw new RuntimeException("Failed to init a new generator");
        }
        generator.generateGraph(graph);
        RandomUtil randomUtil = new RandomUtil();
        for (Edge edge : graph.edgeSet()) {
            graph.setEdgeWeight(edge, randomUtil.getNextRandom(edgeWeightBound));
        }
    }

    protected static GraphGenerator<Vertex, Edge, Vertex> getGenerator(
            GraphGenerateStrategies strategy, int size) {

        int vertexCnt = Math.max(size, 2);
        int edgeCnt = vertexCnt * 3;
        edgeCnt = Math.min(edgeCnt, (vertexCnt * (vertexCnt - 1)) / 2);

        GraphGenerator<Vertex, Edge, Vertex> generator = null;
        switch (strategy) {
            case RING:
                generator = new RingGraphGenerator<Vertex, Edge>(vertexCnt);
                break;
            case EMPTY:
                generator = new EmptyGraphGenerator<Vertex, Edge>(vertexCnt);
                break;
            case RANDOM:
                generator = new GnmRandomGraphGenerator<Vertex, Edge>(vertexCnt, edgeCnt);
                break;
            case SCALE_FREE:
                generator = new ScaleFreeGraphGenerator(vertexCnt);
        }
        return generator;
    }
}
