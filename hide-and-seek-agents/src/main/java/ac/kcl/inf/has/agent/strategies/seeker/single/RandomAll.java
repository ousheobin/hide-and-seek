package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.RandomStrategy;

public class RandomAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new RandomStrategy();
    }

    @Override
    public String getName() {
        return "sRandomAll";
    }
}
