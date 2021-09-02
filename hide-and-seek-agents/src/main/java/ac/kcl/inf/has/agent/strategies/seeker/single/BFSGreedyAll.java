package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.BFSGreedyStrategy;

public class BFSGreedyAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new BFSGreedyStrategy();
    }

    @Override
    public String getName() {
        return "sBFSGreedyAll";
    }
}
