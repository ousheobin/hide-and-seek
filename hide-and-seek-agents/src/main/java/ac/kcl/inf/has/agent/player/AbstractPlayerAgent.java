package ac.kcl.inf.has.agent.player;

import ac.kcl.inf.has.agent.execution.DataRecorder;
import ac.kcl.inf.has.agent.execution.ExecutionPermits;
import ac.kcl.inf.has.agent.knowledge.LocalGraph;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractPlayerAgent implements Agent{

    protected String agentName;

    private int totalRound;
    private int currentRound;

    protected GraphController graphController;
    protected LocalGraph localGraph;
    protected DataRecorder dataRecorder;
    protected ExecutionPermits permits;

    private boolean canMove;

    private Set<Vertex> visitedNodes;
    private Set<Edge> visitedEdges;
    private Set<Vertex> hiddenLocations;

    private Vertex currentNode;

    private CountDownLatch gameControllerLatch;

    private int pathLength;

    public AbstractPlayerAgent(GraphController controller,int totalRound, DataRecorder dataRecorder){
        this.graphController = controller;
        this.totalRound = totalRound;
        this.currentRound = 0;
        this.localGraph = new LocalGraph();
        this.dataRecorder = dataRecorder;
        this.canMove = false;
        this.pathLength = 0;
    }

    @Override
    public GraphController getGraphController() {
        return graphController;
    }

    @Override
    public Set<Vertex> getUniqueVisitedNodeSet() {
        return visitedNodes;
    }

    @Override
    public Set<Edge> getUniqueVisitedEdgeSet() {
        return visitedEdges;
    }

    @Override
    public boolean canMove() {
        return canMove;
    }

    @Override
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    @Override
    public boolean gameIsEnded() {
        return currentRound > totalRound;
    }

    @Override
    public Vertex currentNode() {
        return this.currentNode;
    }

    @Override
    public void initForNewRound() {
        currentRound ++;
        if(!this.gameIsEnded()){
            System.out.println(getAgentName() + " starts round #" + currentRound );
            visitedNodes = new HashSet<>();
            visitedEdges = new HashSet<>();
            hiddenLocations = new HashSet<>();
            dataRecorder.newRound(this);
            if(currentNode == null){
                currentNode = graphController.randomStartPoint();
            }else{
                Vertex newStartPoint = graphController.randomStartPoint();
                List<Vertex> path = graphController.travelToNewStaringPoint(currentNode,newStartPoint);
                for (Vertex nextHop : path){
                    if(nextHop.equals(currentNode)){
                        continue;
                    }
                    localGraph.updateGraph(currentNode, graphController.getConnectedVertex(currentNode));
                    travelTo(nextHop);
                }
                currentNode = newStartPoint;
            }
        }
    }

    @Override
    public void bindPermits(ExecutionPermits executionPermits) {
        if(executionPermits != null){
            this.permits = executionPermits;
            permits.register(this);
        }
    }

    @Override
    public void setGameControllerLatch(CountDownLatch gameControllerLatch) {
        this.gameControllerLatch = gameControllerLatch;
    }

    @Override
    public Set<Vertex> getHiddenLocations() {
        return hiddenLocations;
    }

    public void travelTo(Vertex v){
        double edgeWeight = graphController.edgeWeight(this.currentNode, v);
        if(edgeWeight < 0){
            throw new IllegalArgumentException("Unreachable vertex:" + v +" (from : "+currentNode+" )");
        }
        pathLength ++;
//        System.out.println("Travel from v"+currentNode().getIndex() +" to v" + v.getIndex()+" cost: " + edgeWeight);
        this.dataRecorder.addCost(this, edgeWeight);
        this.currentNode = v;
        if(v.getWeight() < 0){
            double cost = graphController.requestCostAtNode(v);
            v.setWeight(cost);
        }
    }

    protected void notifyForComplete(){
        if(gameControllerLatch != null){
            gameControllerLatch.countDown();
        }
    }

    @Override
    public int getPathLength() {
        return pathLength;
    }
}
