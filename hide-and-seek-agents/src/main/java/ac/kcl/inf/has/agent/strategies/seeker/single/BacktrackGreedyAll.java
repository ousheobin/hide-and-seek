package ac.kcl.inf.has.agent.strategies.seeker.single;

import ac.kcl.inf.has.agent.strategies.exploration.BacktrackGreedyStrategy;

public class BacktrackGreedyAll extends DiscoverAllNodes{

    @Override
    public void initExplorationStrategy() {
        explorationStrategy = new BacktrackGreedyStrategy();
    }

    @Override
    public String getName() {
        return "sBacktrackGreedyAll";
    }
}
