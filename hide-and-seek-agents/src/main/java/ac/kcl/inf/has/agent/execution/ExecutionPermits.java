package ac.kcl.inf.has.agent.execution;

import ac.kcl.inf.has.agent.player.Agent;
import ac.kcl.inf.has.agent.player.hider.Hider;
import ac.kcl.inf.has.agent.player.seeker.Seeker;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExecutionPermits {

    private List<Agent> hiders;
    private List<Agent> seekers;

    private ExecutionPermits(){
        hiders = new CopyOnWriteArrayList<>();
        seekers = new CopyOnWriteArrayList<>();
    }

    public static ExecutionPermits newPermits() {
        return new ExecutionPermits();
    }

    public void register(Agent ag){
        if(ag instanceof Hider){
            hiders.add(ag);
        }else if(ag instanceof Seeker){
            seekers.add(ag);
        }
    }

    public synchronized void notifySeekers(){
        for (Agent ag : seekers){
            ag.setCanMove(true);
        }
    }

    public synchronized void notifyHiders(){
        for (Agent ag : hiders){
            ag.setCanMove(true);
        }
    }

    public void clear(){
        hiders.clear();
        seekers.clear();
    }

}
