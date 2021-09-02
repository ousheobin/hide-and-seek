package ac.kcl.inf.has.agent.player.hider.impl;

import ac.kcl.inf.has.agent.execution.DataRecorder;
import ac.kcl.inf.has.agent.execution.ExecutionPermits;
import ac.kcl.inf.has.agent.player.AbstractPlayerAgent;
import ac.kcl.inf.has.agent.player.hider.Hider;
import ac.kcl.inf.has.agent.strategies.action.HiderAction;
import ac.kcl.inf.has.agent.strategies.action.HiderActionType;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.hider.HidingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Vertex;

public class NormalHider extends AbstractPlayerAgent implements Hider {

    private HidingStrategy strategy;
    private int hideNumber = 1;

    public NormalHider(GraphController controller, int totalRound, HidingStrategy strategy,
                       DataRecorder dataRecorder, String name) {
        super(controller, totalRound, dataRecorder);
        this.strategy = strategy;
        this.agentName = name;
        strategy.setGraphController(graphController);
        strategy.setLocalGraph(this.localGraph);
        this.initForNewRound();
    }

    @Override
    public boolean roundIsEnded() {
        return this.getHiddenLocations().size() == hideNumber;
    }

    @Override
    public Vertex nextNode() {
        return null;
    }

    @Override
    public void performHide(HidingStrategy strategy) {
//        System.out.println("Hider start location: " + currentNode());
        while (!roundIsEnded()){

            HiderAction action = strategy.nextAction();
            if(action.getType() == HiderActionType.HIDE){
                hideAtCurrentNode();
                if(roundIsEnded()){
                    break;
                }
            }else if(action.getType() == HiderActionType.MOVE_TO ){
                Object payload = action.getPayload();
                if(payload instanceof Vertex){
                    Vertex next = (Vertex) payload;
                    travelTo(next);
                }
            }
        }
//        System.out.println("Cost: " + dataRecorder.getDataMap());
        setCanMove(false);
        permits.notifySeekers();
    }

    @Override
    public boolean hideAtCurrentNode() {
        if(this.getHiddenLocations().contains(currentNode())){
            return false;
        }
        graphController.hideObject(currentNode());
//        System.out.println("Hide in v" + currentNode().getIndex());
        return this.getHiddenLocations().add(currentNode());
    }

    @Override
    public void setHideNumber(int hideNumber) {
        this.hideNumber = hideNumber;
    }

    @Override
    public void run() {
       try{
           while (!this.gameIsEnded()){
               while (!canMove()){
                   Thread.sleep(100);
               }
               this.getGraphController().newRound();
               this.strategy.notifyForNewRound(currentNode());
               this.performHide(this.strategy);
               this.initForNewRound();
           }
       }catch (Exception ex){
           ex.printStackTrace();
       }
       this.notifyForComplete();
    }

    @Override
    public String getAgentName() {
        return "hider.normal." + agentName;
    }

    @Override
    public String toString() {
        return getAgentName();
    }
}
