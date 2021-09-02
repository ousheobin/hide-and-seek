package ac.kcl.inf.has.env.graph.generation;

import ac.kcl.inf.has.env.graph.GameGraph;
import org.jgrapht.generate.EmptyGraphGenerator;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.generate.RingGraphGenerator;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PhysicalGraphGeneratorTest {

    @Test
    public void testGenerateGraph() {
        GameGraph graph = new GameGraph();
        PhysicalGraphGenerator.generateGraph(graph, GraphGenerateStrategies.RANDOM, 10,0);
        System.out.println(graph);
    }

    @Test
    public void testGetGenerator() {
        Assert.assertTrue(
                PhysicalGraphGenerator.getGenerator(GraphGenerateStrategies.EMPTY, 100)
                        instanceof EmptyGraphGenerator);
        Assert.assertTrue(
                PhysicalGraphGenerator.getGenerator(GraphGenerateStrategies.RANDOM, 100)
                        instanceof GnmRandomGraphGenerator);
        Assert.assertTrue(
                PhysicalGraphGenerator.getGenerator(GraphGenerateStrategies.RING, 100)
                        instanceof RingGraphGenerator);
        Assert.assertTrue(
                PhysicalGraphGenerator.getGenerator(GraphGenerateStrategies.SCALE_FREE, 100)
                        instanceof ScaleFreeGraphGenerator);
    }
}
