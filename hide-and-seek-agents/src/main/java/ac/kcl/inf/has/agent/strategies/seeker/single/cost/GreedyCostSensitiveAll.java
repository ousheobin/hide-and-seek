package ac.kcl.inf.has.agent.strategies.seeker.single.cost;

import ac.kcl.inf.has.agent.strategies.exploration.GreedyCostSensitiveStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.single.DiscoverAllNodes;

public class GreedyCostSensitiveAll extends DiscoverAllNodes {

    @Override
    public String getName() {
        return "sGreedyCostSensitiveAll";
    }

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new GreedyCostSensitiveStrategy();
    }

}
