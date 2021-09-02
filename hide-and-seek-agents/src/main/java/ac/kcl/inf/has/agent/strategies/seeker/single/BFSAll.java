package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.BFSStrategy;

public class BFSAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new BFSStrategy();
    }

    @Override
    public String getName() {
        return "sBFSAll";
    }
}
