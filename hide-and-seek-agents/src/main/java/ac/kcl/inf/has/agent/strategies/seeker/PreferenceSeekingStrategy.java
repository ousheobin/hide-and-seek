package ac.kcl.inf.has.agent.strategies.seeker;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
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

public abstract class PreferenceSeekingStrategy extends AbstractSeekingStrategy {

    protected List<Vertex> targetNodes;
    protected List<Edge> currentPath;
    protected Set<Vertex> visitedSet;
    protected Set<Vertex> foundSet;
    protected Set<Vertex> discoveredSet;

    protected int estimatedHideNumber;
    protected Vertex currPos;

    private int vertexNumber;
    private boolean targetsGenerated;

    private final double graphPortion;
    private final SeekerAction action;
    private final ExplorationStrategy strategy;


    public PreferenceSeekingStrategy(double graphPortion) {
        this.graphPortion = graphPortion;
        targetsGenerated = false;
        targetNodes = new ArrayList<>();
        currentPath = new ArrayList<>();
        visitedSet = new HashSet<>();
        foundSet = new HashSet<>();
        discoveredSet = new HashSet<>();
        action = new SeekerAction();
        strategy = new BacktrackGreedyStrategy();
        estimatedHideNumber = 1;
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

    protected boolean discoverCurrNode(){

        if(visitedSet.size() >= vertexNumber * graphPortion && !targetsGenerated){
            targetNodes = computeTargetNodes();
            targetsGenerated = true;
        }

        if(targetsGenerated && targetNodes.isEmpty() && !discoveredSet.contains(currPos)){
            // Target nodes less than estimated or not found in target nodes
            return true;
        }

        if(targetNodes.contains(currPos)){
            targetNodes.remove(currPos);
            return true;
        }

        return false;

    }

    public abstract List<Vertex> computeTargetNodes();

    @Override
    public SeekerAction nextAction() {

        if(discoverCurrNode()){
            discoveredSet.add(currPos);
            action.setPayload(null);
            action.setType(SeekerActionType.DISCOVER);
            return action;
        }

        visitedSet.add(currPos);
        action.setType(SeekerActionType.MOVE_TO);

        if ( currentPath.size() > 0 ){
            currPos = currentPath.remove(0).getAnotherSide(currPos);
            strategy.updateCurrentPos(currPos);
            action.setPayload(currPos);
            return action;
        }

        DijkstraShortestPath<Vertex, Edge> dsp;
        GraphPath<Vertex, Edge> path;

        if(!targetNodes.isEmpty()){

            if(!localGraph.containVertex(targetNodes.get(0))){
                currPos = strategy.nextNode();
                action.setPayload(currPos);
                return action;
            }

            dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
            path = dsp.getPath(currPos, targetNodes.get(0));

            if(path.getEdgeList() == null || path.getEdgeList().isEmpty() ) {
                currPos = strategy.nextNode();
                action.setPayload(currPos);
                return action;
            }

            currentPath = new ArrayList<Edge>(path.getEdgeList());
            currPos = currentPath.remove(0).getAnotherSide(currPos);
            strategy.updateCurrentPos(currPos);

        } else {
            currPos = strategy.nextNode();
        }

        action.setPayload(currPos);
        return action;
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        targetNodes.clear();
        currentPath.clear();
        visitedSet.clear();
        discoveredSet.clear();
        updateHideNumberEstimation();
        strategy.startNode(startLocation);
        foundSet.clear();
        targetsGenerated = false;
    }

    protected void updateHideNumberEstimation() {
        estimatedHideNumber = Math.max(1,this.foundSet.size());
    }

    @Override
    public void updateDiscoverInfo(Vertex vertex, boolean found) {
        if(found){
            foundSet.add(vertex);
        }
    }

}
