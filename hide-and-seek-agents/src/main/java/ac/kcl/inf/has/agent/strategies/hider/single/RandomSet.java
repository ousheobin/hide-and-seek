package ac.kcl.inf.has.agent.strategies.hider.single;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.hider.AbstractHidingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;
import java.util.stream.Collectors;

public class RandomSet extends AbstractHidingStrategy {

    protected List<Vertex> hideLocations;
    protected Set<Vertex> visited;
    protected Set<Vertex> unvisited;
    protected List<Edge> currentPath;

    protected Set<Vertex> hidden;

    public RandomSet(int hideNumber) {
        super(hideNumber);
        visited = new HashSet<>();
        unvisited = new HashSet<>();
        hidden = new HashSet<>();
    }

    @Override
    public String getName() {
        return "hRandomSet";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currPos = startLocation;
        hideLocations = generateNewRandomSet(hideNumber);
        cleanRecords();
    }

    protected List<Vertex> generateNewRandomSet(int size){
        return graphController.generateRandomHideLocation(size);
    }

    @Override
    public HiderAction nextAction() {

        if(!this.hideLocations.isEmpty() && hideLocations.contains(currPos)){
            action.setType(HiderActionType.HIDE);
            action.setPayload(null);
            hidden.add(currPos);
            this.hideLocations.remove(currPos);
            return action;
        }

        localGraph.updateGraph(currPos, graphController.getConnectedVertex(currPos));

        unvisited.remove(currPos);
        visited.add(currPos);

//        System.out.print("At node "+currPos+ " remain locs: " + hideLocations + " --> ");

        action.setType(HiderActionType.MOVE_TO);
        unvisited.addAll(localGraph.connectEdges(currPos)
                .stream()
                .map(edge -> edge.getAnotherSide(currPos))
                .filter(vertex -> !visited.contains(vertex))
                .collect(Collectors.toSet()));

        double minDistance = Double.MAX_VALUE;
        Vertex nextTarget = null;
        DijkstraShortestPath<Vertex,Edge> dsp;
        GraphPath<Vertex,Edge> path;

        if(!currentPath.isEmpty() && !currentPath.get(0).partOfEdgs(currPos)){
            currentPath.clear();
        }

        if(!currentPath.isEmpty()){
            currPos = currentPath.remove(0).getAnotherSide(currPos);
            action.setPayload(currPos);
            return action;
        }

        for (Vertex vertex : hideLocations){
            if(unvisited.contains(vertex)){
                dsp = new DijkstraShortestPath<>(this.localGraph.getLocalGraph());
                path = dsp.getPath(currPos, vertex);
                if(path.getWeight() < minDistance){
                    minDistance = path.getWeight();
                    nextTarget = vertex;
                }
            }
        }

        if (nextTarget == null){

            for (Vertex v : unvisited){
                dsp = new DijkstraShortestPath<>(this.localGraph.getLocalGraph());
                path = dsp.getPath(currPos, v);
                if(path.getWeight() < minDistance){
                    minDistance = path.getWeight();
                    nextTarget = v;
                }
            }

        }

        dsp = new DijkstraShortestPath<>(this.localGraph.getLocalGraph());
        path = dsp.getPath(currPos,nextTarget);

        if (path.getEdgeList() != null && !path.getEdgeList().isEmpty()){
            currentPath = new ArrayList<>(path.getEdgeList());
        }

        if(!currentPath.isEmpty()){
            currPos = currentPath.remove(0).getAnotherSide(currPos);
        }else {
            currPos = localGraph.randomSelectConnectedVertex(currPos);
        }

        action.setPayload(currPos);
        return action;
    }

    public void setHideLocations(List<Vertex> hideLocations) {
        this.hideLocations = hideLocations;
    }

    protected void cleanRecords(){
        visited.clear();
        unvisited.clear();
        hidden.clear();
        currentPath = new ArrayList<>();
    }
}
