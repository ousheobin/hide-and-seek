package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.HashSet;
import java.util.Set;

public abstract class DiscoverAllNodes extends AbstractSeekingStrategy {

    private Set<Vertex> discoveredVertex;
    protected ExplorationStrategy explorationStrategy;

    protected Vertex currentNode;
    private SeekerAction action;

    public DiscoverAllNodes(){
        discoveredVertex = new HashSet<>();
        action = new SeekerAction();
        this.initExplorationStrategy();
    }

    public abstract void initExplorationStrategy();

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        super.setLocalGraph(localGraph);
        explorationStrategy.setLocalGraph(localGraph);
    }

    @Override
    public void setGraphController(GraphController controller) {
        super.setGraphController(controller);
        explorationStrategy.setGraphController(graphController);
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        discoveredVertex.clear();
        explorationStrategy.startNode(startLocation);
        currentNode = startLocation;
    }

    @Override
    public SeekerAction nextAction() {
        if(discoveredVertex.contains(currentNode)){
            currentNode = explorationStrategy.nextNode();
            action.setType(SeekerActionType.MOVE_TO);
            action.setPayload(currentNode);
            return action;
        }else{
            discoveredVertex.add(currentNode);
            action.setType(SeekerActionType.DISCOVER);
            action.setPayload(null);
            return action;
        }
    }

    @Override
    public void updateDiscoverInfo(Vertex vertex, boolean found) {
        //Note: Do nothing here.
    }

    protected Set<Vertex> getDiscoveredVertex() {
        return discoveredVertex;
    }
}
