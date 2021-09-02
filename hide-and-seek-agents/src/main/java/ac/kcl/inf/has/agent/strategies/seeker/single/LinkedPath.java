package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class LinkedPath extends AbstractSeekingStrategy {

    private Set<Vertex> discoveredSet;
    private Set<Vertex> foundSet;
    private Map<Vertex,Integer> visitedCnt;

    private Stack<Vertex> foundStack;
    private List<Edge> currentPath;

    private List<Vertex> potentialVertex;
    private boolean isFound;

    private BacktrackGreedyStrategy explorationStrategy;

    private Vertex currPos;

    private SeekerAction action;

    public LinkedPath(){
        discoveredSet = new HashSet<>();
        foundSet = new HashSet<>();
        visitedCnt = new HashMap<>();
        foundStack = new Stack<>();
        action = new SeekerAction();
        currentPath = new ArrayList<>();
        potentialVertex = new ArrayList<>();
        explorationStrategy = new BacktrackGreedyStrategy();
    }

    @Override
    public String getName() {
        return "sLinkedPath";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        discoveredSet.clear();
        foundSet.clear();
        visitedCnt.clear();
        foundStack.clear();
        currentPath.clear();
        potentialVertex.clear();
        explorationStrategy.startNode(startLocation);
        isFound = false;
        localGraph.updateGraph(currPos,graphController.getConnectedVertex(currPos));
    }

    @Override
    public void setGraphController(GraphController controller) {
        super.setGraphController(controller);
        explorationStrategy.setGraphController(controller);
    }

    @Override
    public void setLocalGraph(LocalGraph localGraph) {
        super.setLocalGraph(localGraph);
        explorationStrategy.setLocalGraph(localGraph);
    }

    /**
     * This implementation refers to the LinkedPath
     * implementation in Hands written by Dr.Martin Chapman.
     * @return the Seeker's action
     */
    @Override
    public SeekerAction nextAction() {
        if(!discoveredSet.contains(currPos)){
            action.setType(SeekerActionType.DISCOVER);
            action.setPayload(null);
            discoveredSet.add(currPos);
            return action;
        }

        updateVisitedCnt(currPos);
        action.setType(SeekerActionType.MOVE_TO);

        if ( foundSet.isEmpty()) {
            currPos = explorationStrategy.nextNode();
            action.setPayload(currPos);
            return action;
        } else {

            if ( foundSet.contains(currPos) && visitedCnt.getOrDefault(currPos,0) == 1) {
                potentialVertex.clear();
                currentPath.clear();
                isFound = true;
                foundStack.push(currPos);
            }

            potentialVertex.remove(currPos);

            if ( !currentPath.isEmpty() ) {
                currPos = currentPath.remove(0).getAnotherSide(currPos);
                action.setPayload(currPos);
                explorationStrategy.updateCurrentPos(currPos);
                return action;
            }

            if ( potentialVertex.isEmpty() && isFound) {
                for ( Edge connectedEdge : localGraph.connectEdges(currPos) ) {
                    Vertex nextSide = connectedEdge.getAnotherSide(currPos);
                    if ( !discoveredSet.contains(nextSide)  && !foundSet.contains(nextSide) ) {
                        potentialVertex.add(nextSide);
                    }
                }
                isFound = false;
            }

            DijkstraShortestPath<Vertex, Edge> dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
            GraphPath<Vertex,Edge> path;

            if ( potentialVertex.size() > 0 ) {
                path = dsp.getPath(currPos, potentialVertex.remove(0));
            } else if ( foundStack.size() > 1 ) {
                isFound = true;
                foundStack.pop();
                path = dsp.getPath(currPos,foundStack.pop());
            } else {
                currPos = explorationStrategy.nextNode();
                action.setPayload(currPos);
                return action;
            }

            // If no path available, then return to exploring
            if ( path.getEdgeList() == null || path.getEdgeList().isEmpty() ) {
                currPos = explorationStrategy.nextNode();
                action.setPayload(currPos);
                return action;
            }

            currentPath = new ArrayList<>(path.getEdgeList());
            currPos = currentPath.remove(0).getAnotherSide(currPos);

            action.setPayload(currPos);
            explorationStrategy.updateCurrentPos(currPos);
            return action;

        }
    }

    @Override
    public void updateDiscoverInfo(Vertex vertex, boolean found) {
        if(found){
            this.foundSet.add(vertex);
        }
    }

    private void updateVisitedCnt(Vertex vertex){
        if(!visitedCnt.containsKey(vertex)){
            visitedCnt.put(vertex,1);
        }else {
            visitedCnt.put(vertex, visitedCnt.get(vertex) + 1);
        }
    }

}
