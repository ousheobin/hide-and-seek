package ac.kcl.inf.has.simulation.controller;

import ac.kcl.inf.has.agent.execution.DataRecorder;
import ac.kcl.inf.has.agent.execution.ExecutionPermits;
import ac.kcl.inf.has.agent.player.Agent;
import ac.kcl.inf.has.agent.player.AgentBuilder;
import ac.kcl.inf.has.agent.strategies.StrategyFactory;
import ac.kcl.inf.has.agent.strategies.hider.single.HideAllNode;
import ac.kcl.inf.has.agent.strategies.seeker.single.DiscoverAllNodes;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.simulation.analysis.PayoffWriter;
import ac.kcl.inf.has.simulation.analysis.SimulationRecord;
import ac.kcl.inf.has.simulation.config.Config;
import ac.kcl.inf.has.simulation.config.GraphConfig;
import ac.kcl.inf.has.simulation.config.SimulationConfigEntry;
import ac.kcl.inf.has.simulation.execution.ThreadPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationController {

    private static SimulationController controller;

    private Config config;

    private SimulationController() {
    }

    public static SimulationController getController() {
        if (controller == null) {
            controller = new SimulationController();
        }
        return controller;
    }

    public SimulationController configure(Config cfg) {
        this.config = cfg;
        return this;
    }

    public SimulationController doSimulate() throws IOException {
        if (this.config == null) {
            System.err.println("[Error] Simulation controller not being configured");
            return this;
        }
        PayoffWriter payoffWriter = new PayoffWriter(new File("./result"));
        ThreadPool pool = ThreadPool.getThreadPool();
        List<SimulationConfigEntry> sims = config.getSimulationConfigEntryList();
        int simulationId = 1;
        int simsSize = sims.size();
        int repeatTimes = config.getEachSimulationRepeat();
        AtomicInteger completeCnt = new AtomicInteger(0);
        int recommendBatchSize = Runtime.getRuntime().availableProcessors() * 4;

        for (SimulationConfigEntry configEntry : sims) {
            SimulationRecord record = new SimulationRecord();
            System.out.println("[Info] Performing simulation " + simulationId + "/" + simsSize);
            GraphConfig graphConfig = configEntry.getGraphConfig();
            CountDownLatch latch = new CountDownLatch(Math.min(recommendBatchSize, repeatTimes));
            completeCnt.set(1);
            for (int repeat = 1; repeat <= repeatTimes; repeat++) {

                CountDownLatch finalLatch = latch;
                ThreadPool.getThreadPool().submitTask(()->{

                    try{
                        DataRecorder dataRecorder = DataRecorder.getDataRecorder();
                        CountDownLatch gameControllerLatch = new CountDownLatch(2);
                        ExecutionPermits permits = ExecutionPermits.newPermits();

                        GraphController env =
                                GraphController.build()
                                        .newGraph(
                                                graphConfig.getGenerateStrategy(),
                                                graphConfig.getVertex(),
                                                config.getVertexWeightUpperBound(),
                                                config.getEdgeWeightUpperBound())
                                        .knowNodeWeightInAdvance(graphConfig.isCanKnowCostInAdvance());

                        AgentBuilder builder =
                                AgentBuilder.getBuilder()
                                        .graphController(env)
                                        .hideObjectNumber(graphConfig.getHideNumber())
                                        .totalRound(configEntry.getRounds());

                        int hiderDeceptiveRounds = configEntry.getHiderConfig().getDeceptiveRounds();
                        hiderDeceptiveRounds = (hiderDeceptiveRounds == -1) ? configEntry.getRounds() : hiderDeceptiveRounds;
                        hiderDeceptiveRounds = Math.min(configEntry.getRounds(), hiderDeceptiveRounds);

                        Agent hider = builder.newHider(
                                configEntry.getHiderConfig().getType(),
                                StrategyFactory.generateHidingStrategy(
                                        configEntry.getHiderConfig().getStrategy(),
                                        graphConfig.getHideNumber(),
                                        configEntry.getHiderConfig().getGraphPortion(),
                                        hiderDeceptiveRounds
                                        ),
                                dataRecorder
                        );

                        Agent seeker = builder.newSeeker(
                                configEntry.getHiderConfig().getType(),
                                StrategyFactory.generateSeekingStrategy(
                                        configEntry.getSeekerConfig().getStrategy(),
                                        configEntry.getSeekerConfig().getGraphPortion()),
                                dataRecorder);

                        hider.setGameControllerLatch(gameControllerLatch);
                        seeker.setGameControllerLatch(gameControllerLatch);

                        hider.bindPermits(permits);
                        seeker.bindPermits(permits);

                        pool.addNewAgent(hider);
                        pool.addNewAgent(seeker);

                        hider.setCanMove(true);

                        try {
                            gameControllerLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

//                        System.out.println("Hider path length: " + hider.getPathLength() + " Seeker path length: " + seeker.getPathLength());

                        Map<Agent, List<Double>> data = dataRecorder.getDataMap();

                        record.addEachRoundCost("hider", data.getOrDefault(hider, new ArrayList<>()));
                        record.addEachRoundCost("seeker", data.getOrDefault(seeker, new ArrayList<>()));

                        System.out.println("[Info] Repeat " + completeCnt.getAndIncrement() + "/" + repeatTimes + " completed");

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    finalLatch.countDown();
                });

                if(repeat % recommendBatchSize == 0 || repeat == repeatTimes){
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(repeat < repeatTimes){
                        latch = new CountDownLatch(Math.min(recommendBatchSize, repeatTimes - repeat));
                    }
                }
            }



//            record.printMap();

            simulationId++;

            payoffWriter.writeOverallData(configEntry, record.calculateCost());
            payoffWriter.writeEachRoundData(configEntry, record.getEachRoundCost());

        }

        payoffWriter.close();

        pool.terminate();
        return this;
    }
}
