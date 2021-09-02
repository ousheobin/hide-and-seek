package ac.kcl.inf.has.agent.strategies.seeker.single.cost;

import ac.kcl.inf.has.agent.strategies.exploration.CostSensitiveFirstStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.single.DiscoverAllNodes;

public class CostSensitiveFirst extends DiscoverAllNodes {

    @Override
    public String getName() {
        return "sCostSensitiveFirst";
    }

    @Override
    public void initExplorationStrategy() {
        explorationStrategy = new CostSensitiveFirstStrategy();
    }

}
