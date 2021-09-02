package ac.kcl.inf.has.agent.player.seeker.impl;

import ac.kcl.inf.has.agent.execution.DataRecorder;
import ac.kcl.inf.has.agent.execution.ExecutionPermits;
import ac.kcl.inf.has.agent.player.AbstractPlayerAgent;
import ac.kcl.inf.has.agent.player.seeker.Seeker;
import ac.kcl.inf.has.agent.strategies.action.SeekerAction;
import ac.kcl.inf.has.agent.strategies.action.SeekerActionType;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.SeekingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

public class NormalSeeker extends AbstractPlayerAgent implements Seeker {

    private SeekingStrategy strategy;

    public NormalSeeker(GraphController controller, int totalRound, SeekingStrategy strategy,
                        DataRecorder dataRecorder, String name) {
        super(controller, totalRound,dataRecorder);
        this.agentName = name;
        this.strategy = strategy;
        strategy.setGraphController(graphController);
        strategy.setLocalGraph(this.localGraph);
        this.initForNewRound();
    }

    @Override
    public boolean roundIsEnded() {
        return graphController.allFound();
    }

    @Override
    public Vertex nextNode() {
        return null;
    }

    @Override
    public String getAgentName() {
        return "seeker.normal."+agentName;
    }

    @Override
    public void performSearch(SeekingStrategy strategy) {
//        System.out.println("Seeker start location: " + currentNode());
        int seekTime = 0;
        while (!roundIsEnded()){
            SeekerAction action = strategy.nextAction();
            if(action.getType() == SeekerActionType.DISCOVER){
                boolean discoverRet = discoverAtCurrentNode();
                strategy.updateDiscoverInfo(currentNode(), discoverRet);
                seekTime ++;
                if(roundIsEnded()){
                    break;
                }
            }else if(action.getType() == SeekerActionType.MOVE_TO ){
                Object payload = action.getPayload();
                if(payload instanceof Vertex){
                    Vertex next = (Vertex) payload;
                    travelTo(next);
                }
            }
        }
//        System.out.println("Seek time:" + seekTime + " length: " + getPathLength());
//        System.out.println("Cost: " + dataRecorder.getDataMap());

        setCanMove(false);
        permits.notifyHiders();
    }

    @Override
    public boolean discoverAtCurrentNode() {
        double weight = getGraphController().requestCostAtNode(currentNode());
        dataRecorder.addCost(this, weight);
        if(!graphController.discoverNode(currentNode())){
//            System.out.println("Discover v"+currentNode().getIndex()+" (Not found) cost:"+weight);
            return false;
        }
//        System.out.println("Discover v"+currentNode().getIndex()+" (Found) cost:"+weight);
        getHiddenLocations().add(currentNode());
        return true;
    }

    @Override
    public void run() {
        try{
            while (!this.gameIsEnded()){
                while (!canMove()){
                    Thread.sleep(100);
                }
                strategy.notifyForNewRound(currentNode());
                this.performSearch(strategy);
                this.initForNewRound();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        this.notifyForComplete();
    }

    @Override
    public String toString() {
        return getAgentName();
    }
}
