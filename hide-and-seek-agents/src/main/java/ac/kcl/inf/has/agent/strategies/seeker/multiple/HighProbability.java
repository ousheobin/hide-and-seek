package ac.kcl.inf.has.agent.strategies.seeker.multiple;

import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.AbstractSeekingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class HighProbability extends AbstractSeekingStrategy {

    protected List<Vertex> likelyNodes;
    protected List<Edge> currentPath;
    protected Set<Vertex> discoveredSet;
    protected Set<Vertex> foundSet;

    protected Vertex currentPos;
    protected int estimatedHideNumber;
    protected SeekerAction action;
    protected ExplorationStrategy explorationStrategy;

    protected final Map<Vertex, Integer> frequency;


    public HighProbability(){
        likelyNodes = new ArrayList<>();
        currentPath = new ArrayList<>();
        frequency = new HashMap<>();
        discoveredSet = new HashSet<>();
        foundSet = new HashSet<>();
        action = new SeekerAction();
        explorationStrategy = new BacktrackGreedyStrategy();
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

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        currentPos = startLocation;
        estimatedHideNumber = Math.max(1, foundSet.size());

        currentPath.clear();
        discoveredSet.clear();
        foundSet.clear();
        likelyNodes = selectNodes();

        explorationStrategy.startNode(startLocation);
    }

    @Override
    public SeekerAction nextAction() {

        if(discoverHere()){
            discoveredSet.add(currentPos);
            action.setPayload(null);
            action.setType(SeekerActionType.DISCOVER);
            return action;
        }

        DijkstraShortestPath<Vertex, Edge> dsp;
        GraphPath<Vertex, Edge> path;
        action.setType(SeekerActionType.MOVE_TO);

        if ( currentPath.size() > 0 ){
            currentPos = currentPath.remove(0).getAnotherSide(currentPos);
            explorationStrategy.updateCurrentPos(currentPos);
            action.setPayload(currentPos);
            return action;
        }

        if(!likelyNodes.isEmpty()){
            dsp = new DijkstraShortestPath<>(localGraph.getLocalGraph());
            path = dsp.getPath(currentPos, likelyNodes.get(0));

            if(path.getEdgeList() == null || path.getEdgeList().isEmpty() ) {
                currentPos = explorationStrategy.nextNode();
                action.setPayload(currentPos);
                return action;
            }

            currentPath = new ArrayList<>(path.getEdgeList());
            currentPos = currentPath.remove(0).getAnotherSide(currentPos);
            explorationStrategy.updateCurrentPos(currentPos);

        } else {
            currentPos = explorationStrategy.nextNode();
        }

        action.setPayload(currentPos);
        return action;
    }

    protected boolean discoverHere(){
        if(!discoveredSet.contains(currentPos)){
            if ( likelyNodes.contains(currentPos) ){
                likelyNodes.remove(currentPos);
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateDiscoverInfo(Vertex vertex, boolean found) {
        if(found){
            foundSet.add(currentPos);
            if(!frequency.containsKey(vertex)){
                frequency.put(vertex, 0);
            }
            frequency.put(vertex, frequency.get(vertex) + 1);
        }
    }

    protected List<Vertex> selectNodes(){
        List<Vertex> selectedNodes = new ArrayList<>();
        if(frequency.isEmpty()){
            // The first round, just discover follow the exploration strategy.
            return selectedNodes;
        }

        int totalScale = 0;
        Map<Vertex, Double> probability = new HashMap<>();
        for (Vertex v: frequency.keySet()){
            totalScale += frequency.get(v);
        }

        for (Vertex v: frequency.keySet()){
            probability.put(v, (double)frequency.get(v) / totalScale);
        }

        double maxProbability;
        List<Vertex> potentialNodes = new ArrayList<>();
        for (int i = 0 ; i < estimatedHideNumber ; i ++){
            maxProbability = Double.MIN_VALUE;
            potentialNodes.clear();

            for (Vertex v : probability.keySet()){
                if(probability.get(v) > maxProbability){
                    maxProbability = probability.get(v);
                    potentialNodes.clear();
                    potentialNodes.add(v);
                } else if( Double.compare(maxProbability, probability.get(v)) == 0){
                    potentialNodes.add(v);
                }
            }

            Vertex selected = null;
            if(!potentialNodes.isEmpty()){
                selected = potentialNodes.get((int) (Math.random() * potentialNodes.size()));
            }

            selectedNodes.add(selected);
            probability.remove(selected);

        }
        return selectedNodes;
    }

    @Override
    public String getName() {
        return "sHighProbability";
    }

}
