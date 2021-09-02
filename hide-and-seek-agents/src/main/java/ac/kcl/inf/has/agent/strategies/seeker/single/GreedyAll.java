package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.GreedyStrategy;

public class GreedyAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new GreedyStrategy();
    }

    @Override
    public String getName() {
        return "sGreedyAll";
    }
}
