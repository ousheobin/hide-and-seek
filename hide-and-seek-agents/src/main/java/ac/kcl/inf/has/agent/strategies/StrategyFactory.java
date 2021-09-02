package ac.kcl.inf.has.agent.strategies;

import ac.kcl.inf.has.agent.strategies.hider.HidingStrategy;
import ac.kcl.inf.has.agent.strategies.hider.mutilple.*;
import ac.kcl.inf.has.agent.strategies.hider.single.FirstK;
import ac.kcl.inf.has.agent.strategies.hider.single.HideAllNode;
import ac.kcl.inf.has.agent.strategies.hider.single.HighCostGreedy;
import ac.kcl.inf.has.agent.strategies.hider.single.RandomSet;
import ac.kcl.inf.has.agent.strategies.seeker.SeekingStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.multiple.*;
import ac.kcl.inf.has.agent.strategies.seeker.single.*;
import ac.kcl.inf.has.agent.strategies.seeker.single.cost.CostSensitiveFirst;
import ac.kcl.inf.has.agent.strategies.seeker.single.cost.GreedyCostSensitiveAll;

public class StrategyFactory {

    public static HidingStrategy generateHidingStrategy(String name, int hideNumber,
                                                        double graphPortion, int deceptiveRounds ){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Unexpected hider strategy name (cannot be empty)");
        }
        hideNumber = Math.max(hideNumber,1);
        switch (name){
            case "hAllLocation":
                return new HideAllNode(hideNumber);
            case "hRandomSet":
                return new RandomSet(hideNumber);
            case "hFirstK":
                return new FirstK(hideNumber);
            case "hLeastConnected":
                return new LeastConnected(hideNumber, graphPortion);
            case "hLeastConnectedWithCost":
                return new LeastConnectedWithCost(hideNumber, graphPortion);
            case "hMaxDistance":
                return new MaxDistance(hideNumber,graphPortion);
            case "hNotConnected":
                return new NotConnected(hideNumber);
            case "hDeceptive":
                return new Deceptive(hideNumber, deceptiveRounds, hideNumber);
            case "hDeceptiveWithCost":
                return new DeceptiveWithCost(hideNumber, deceptiveRounds, graphPortion);
            case "hUniqueRandomSet":
                return new UniqueRandomSet(hideNumber);
            case "hHighestCost":
                return new HighestCost(hideNumber, graphPortion);
            case "hHighCostGreedy":
                return new HighCostGreedy(hideNumber);
            case "hUniqueHighestCost":
                return new UniqueHighestCost(hideNumber, graphPortion);
        }
        return null;
    }

    public static SeekingStrategy generateSeekingStrategy(String name, double graphPortion){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Unexpected hider strategy name (cannot be empty)");
        }
        switch (name){
            case "sBacktrackGreedyAll":
                return new BacktrackGreedyAll();
            case "sBFSAll":
                return new BFSAll();
            case "sBFSGreedyAll":
                return new BFSGreedyAll();
            case "sDFSAll":
                return new DFSAll();
            case "sDFSGreedyAll":
                return new DFSGreedyAll();
            case "sGreedyAll":
                return new GreedyAll();
            case "sRandomAll":
                return new RandomAll();
            case "sCostSensitiveFirst":
                return new CostSensitiveFirst();
            case "sGreedyCostSensitiveAll":
                return new GreedyCostSensitiveAll();
            case "sHighestCostFirst":
                return new HighestCostFirst(graphPortion);
            case "sCloseToAverageCost":
                return new CloseToAverageCost(graphPortion);
            case "sLinkedPath":
                return new LinkedPath();
            case "sHighProbability":
                return new HighProbability();
            case "sHighProbabilityWithCost":
                return new HighProbabilityWithCost();
            case "sInverseHighProbability":
                return new InverseHighProbability();
            case "sInverseHighProbabilityWithCost":
                return new InverseHighProbabilityWithCost();
            case "sLeastConnectedFirst":
                return new LeastConnectedFirst();
            case "sMaxDistanceFirst":
                return new MaxDistanceFirst(graphPortion);
        }
        throw new IllegalArgumentException("Unexpected seeking strategy:" + name);
    }

}
