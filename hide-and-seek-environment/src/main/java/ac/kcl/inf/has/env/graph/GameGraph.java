package ac.kcl.inf.has.env.graph;

import ac.kcl.inf.has.env.graph.generation.EdgeFactory;
import ac.kcl.inf.has.env.graph.generation.VertexFactory;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GameGraph extends DefaultUndirectedWeightedGraph<Vertex, Edge> {

    public GameGraph() {
        super(new VertexFactory(0),new EdgeFactory());
    }

    public GameGraph(int vertexWeightUpperBound) {
        super(new VertexFactory(vertexWeightUpperBound), new EdgeFactory());
    }

}
