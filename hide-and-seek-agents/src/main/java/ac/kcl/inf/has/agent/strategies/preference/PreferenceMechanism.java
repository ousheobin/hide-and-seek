package ac.kcl.inf.has.agent.strategies.preference;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.List;

public abstract class PreferenceMechanism {

    protected LocalGraph localGraph;
    protected GraphController graphController;

    public PreferenceMechanism(LocalGraph localGraph, GraphController graphController) {
        this.localGraph = localGraph;
        this.graphController = graphController;
    }

    public abstract List<Vertex> getPreferenceTargets(int hideNumber);

}
