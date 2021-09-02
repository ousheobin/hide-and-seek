package ac.kcl.inf.has.agent.strategies.hider.mutilple;

import ac.kcl.inf.has.agent.strategies.hider.single.RandomSet;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Deceptive extends RandomSet {

    private List<Vertex> deceptiveSet;
    private int currentRound;
    private int deceptiveRounds;
    private int deceptiveNumbers;

    public Deceptive(int hideNumber,int deceptiveRounds, int deceptiveNumbers) {
        super(hideNumber);
        currentRound = 0;
        this.deceptiveRounds = deceptiveRounds;
        this.deceptiveNumbers = deceptiveNumbers;
    }

    @Override
    public String getName() {
        return "hDeceptive";
    }

    @Override
    public void notifyForNewRound(Vertex startLocation) {
        cleanRecords();
        currPos = startLocation;
        currentRound ++;
        if(deceptiveSet == null){
            deceptiveSet = graphController.generateRandomHideLocation(deceptiveNumbers);
        }
        if(currentRound < deceptiveRounds){
            setHideLocations(new ArrayList<>(deceptiveSet));
        }else{
            setHideLocations(generateNewRandomSet(hideNumber));
        }
    }

    @Override
    protected List<Vertex> generateNewRandomSet(int size) {
        return graphController.generateRandomHideLocation(size, new HashSet<>(deceptiveSet));
    }
}
