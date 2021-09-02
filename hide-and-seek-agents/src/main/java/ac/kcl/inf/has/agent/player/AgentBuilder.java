package ac.kcl.inf.has.agent.player;

import ac.kcl.inf.has.agent.execution.DataRecorder;
import ac.kcl.inf.has.agent.player.hider.Hider;
import ac.kcl.inf.has.agent.player.hider.impl.NormalHider;
import ac.kcl.inf.has.agent.player.seeker.Seeker;
import ac.kcl.inf.has.agent.player.seeker.impl.NormalSeeker;
import ac.kcl.inf.has.agent.strategies.exploration.ExplorationStrategy;
import ac.kcl.inf.has.agent.strategies.hider.HidingStrategy;
import ac.kcl.inf.has.agent.strategies.seeker.SeekingStrategy;
import ac.kcl.inf.has.env.controller.GraphController;

import java.util.concurrent.atomic.AtomicInteger;

public class AgentBuilder {

    private static AtomicInteger seekerIndex = new AtomicInteger(0);
    private static AtomicInteger hiderIndex = new AtomicInteger(0);

    private GraphController graphController;

    private int gameRounds = 1;
    private int hideObjectNumber = 1;

    private AgentBuilder() {

    }

    public static AgentBuilder getBuilder() {
        return new AgentBuilder();
    }

    public AgentBuilder graphController(GraphController graphController) {
        if (graphController != null) {
            this.graphController = graphController;
        }
        return this;
    }

    public AgentBuilder totalRound(int round) {
        gameRounds = Math.max(1, round);
        return this;
    }

    public AgentBuilder hideObjectNumber(int number) {
        hideObjectNumber = Math.max(1, number);
        return this;
    }

    public Seeker newSeeker(String type, SeekingStrategy strategy, DataRecorder dataRecorder) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        Seeker seeker = null;
        switch (type) {
            case "normal":
                seeker =
                        new NormalSeeker(
                                this.graphController,
                                gameRounds,
                                strategy,
                                dataRecorder,
                                Integer.toString(seekerIndex.getAndIncrement()));
                break;
            default:
                throw new IllegalArgumentException("Unknown agent type:" + type);
        }
        return seeker;
    }

    public Hider newHider(String type, HidingStrategy strategy, DataRecorder dataRecorder) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        Hider hider = null;
        switch (type) {
            case "normal":
                hider =
                        new NormalHider(
                                this.graphController,
                                gameRounds,
                                strategy,
                                dataRecorder,
                                Integer.toString(hiderIndex.getAndIncrement()));
                break;
            default:
                throw new IllegalArgumentException("Unknown agent type:" + type);
        }
        hider.setHideNumber(hideObjectNumber);
        return hider;
    }
}
