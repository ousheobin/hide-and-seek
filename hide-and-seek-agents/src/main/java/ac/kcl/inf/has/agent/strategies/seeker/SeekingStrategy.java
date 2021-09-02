package ac.kcl.inf.has.agent.strategies.seeker;

import ac.kcl.inf.has.agent.strategies.Strategy;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.env.graph.Vertex;

public interface SeekingStrategy extends Strategy {

    SeekerAction nextAction();

    void updateDiscoverInfo(Vertex vertex, boolean found);

}
