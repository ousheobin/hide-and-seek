package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.DFSStrategy;

public class DFSAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        this.explorationStrategy = new DFSStrategy();
    }

    @Override
    public String getName() {
        return "sDFSAll";
    }
}
