package ac.kcl.inf.has.agent.strategies.hider;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PreferenceHidingStrategy extends AbstractHidingStrategy{

    protected List<Vertex> targetNodes;
    protected List<Edge> currentPath;
    protected Set<Vertex> visitedSet;
    protected Set<Vertex> hiddenSet;

    private int vertexNumber;
    private boolean targetsGenerated;

    private final double graphPortion;
    private final HiderAction action;
    private final ExplorationStrategy strategy;

    protected PreferenceHidingStrategy(int hideNumber, double graphPortion) {
        super(hideNumber);
        this.graphPortion = graphPortion;
        targetsGenerated = false;
        targetNodes = new ArrayList<>();
        currentPath = new ArrayList<>();
        visitedSet = new HashSet<>();
        hiddenSet = new HashSet<>();
        action = new HiderAction();
        strategy = new BacktrackGreedyStrategy();
    }

    @Override
    public void setGraphController(GraphController controller) {
        super.setGraphController(controller);
        vertexNumber = controller.getVertexCount();
        strategy.setGraphController(graphController);
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        super.setLocalGraph(localGraph);
        strategy.setLocalGraph(localGraph);
    }

    protected void addTargetNode(Vertex vertex) {
        if(!targetNodes.contains(vertex)){
            targetNodes.add(vertex);
        }
    }

    protected boolean hideAtCurrNode(){
        if(visitedSet.size() >= vertexNumber * graphPortion && !targetsGenerated){
            targetNodes = computeTargetNodes();
            targetsGenerated = true;
        }

        if(targetsGenerated && targetNodes.isEmpty() && !hiddenSet.contains(currPos)){
            hiddenSet.add(currPos);
            return true;
        }

        if(targetNodes.contains(currPos)){
            targetNodes.remove(currPos);
            hiddenSet.add(currPos);
            return true;
        }

        return false;

    }

    public abstract List<Vertex> computeTargetNodes();

    @Override
    public HiderAction nextAction() {

        if(hideAtCurrNode()){
            action.setPayload(null);
            action.setType(HiderActionType.HIDE);
            return action;
        }

        visitedSet.add(currPos);
        action.setType(HiderActionType.MOVE_TO);

        if( (targetNodes.size() + hiddenSet.size()) < hideNumber ){
            currPos = strategy.nextNode();
            action.setPayload(currPos);
            return action;
        }

        if ( !currentPath.isEmpty() ){
            currPos = currentPath.remove(0).getAnotherSide(currPos);
            strategy.updateCurrentPos(currPos);
            action.setPayload(currPos);
            return action;
        }

        if(!targetNodes.isEmpty() && !localGraph.containVertex(targetNodes.get(0))){
            currPos = strategy.nextNode();
            action.setPayload(currPos);
            return action;
        }

        DijkstraShortestPath<Vertex, Edge> dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
        GraphPath<Vertex, Edge> path = dsp.getPath(currPos, targetNodes.get(0));

        if(path.getEdgeList() == null || path.getEdgeList().isEmpty() ) {
            currPos = strategy.nextNode();
            action.setPayload(currPos);
            return action;
        }

        currentPath = new ArrayList<Edge>(path.getEdgeList());
        currPos = currentPath.remove(0).getAnotherSide(currPos);

        strategy.updateCurrentPos(currPos);
        action.setPayload(currPos);
        return action;
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        targetNodes.clear();
        currentPath.clear();
        visitedSet.clear();
        hiddenSet.clear();
        strategy.startNode(startLocation);
        targetsGenerated = false;
    }

}
