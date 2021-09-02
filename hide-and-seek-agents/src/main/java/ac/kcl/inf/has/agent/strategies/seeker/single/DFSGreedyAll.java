package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.DFSGreedyStrategy;

public class DFSGreedyAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new DFSGreedyStrategy();
    }

    @Override
    public String getName() {
        return "sDFSGreedyAll";
    }
}
