package ac.kcl.inf.has.agent.strategies.hider.single;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.hider.AbstractHidingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HideAllNode extends AbstractHidingStrategy {

    private List<Vertex> hideLocations;
    private Set<Vertex> hidden;
    private Set<Vertex> visited;
    private ExplorationStrategy explorationStrategy;
    private HiderAction action;

    public HideAllNode(int hideNumber){
        super(hideNumber);
        this.hideLocations = new ArrayList<>();
        this.hidden = new HashSet<>();
        this.visited = new HashSet<>();
        this.explorationStrategy = new BacktrackGreedyStrategy();
        this.action = new HiderAction();
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph){
        super.setLocalGraph(localGraph);
        this.explorationStrategy.setLocalGraph(localGraph);
    }

    @Override
    public void setGraphController(GraphController controller) {
        super.setGraphController(controller);
        this.explorationStrategy.setGraphController(controller);
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        this.hideLocations = graphController.generateRandomHideLocation(hideNumber);
        explorationStrategy.startNode(currPos);
        this.hidden.clear();
        this.visited.clear();
    }

    @Override
    public HiderAction nextAction() {
        if( !visited.contains(currPos) && hideLocations.contains(currPos) && !hidden.contains(currPos)){
            hidden.add(currPos);
            action.setType(HiderActionType.HIDE);
            action.setPayload(null);
            return action;
        }else{
            if(!visited.contains(currPos)){
                visited.add(currPos);
            }

            currPos = explorationStrategy.nextNode();
            action.setPayload(currPos);
            action.setType(HiderActionType.MOVE_TO);
            return action;
        }
    }

    @Override
    public String getName() {
        return "hHideAllPlace";
    }

}
