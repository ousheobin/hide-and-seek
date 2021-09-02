package ac.kcl.inf.has.simulation.config;

import ac.kcl.inf.has.env.graph.generation.GraphGenerateStrategies;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static Config config;

    private int eachSimulationRepeat;
    private int vertexWeightUpperBound;
    private int edgeWeightUpperBound;
    private List<SimulationConfigEntry> simulationConfigEntryList;

    private Config(File file) throws IOException {
        this.simulationConfigEntryList = new ArrayList<>();
        this.eachSimulationRepeat = 1;
        this.vertexWeightUpperBound = 0;
        this.edgeWeightUpperBound = 0;
        this.readConfigFile(file);
    }

    public static Config init(File file) throws IOException {
        if (config != null) {
            throw new RuntimeException("Config have been initialized.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("Config file: " + file + " not exists.");
        }
        config = new Config(file);
        return config;
    }

    public static Config getInstance() {
        return config;
    }

    private void readConfigFile(File file) throws IOException {
        Gson gson = new Gson();
        JsonObject configJson = gson.fromJson(new FileReader(file), JsonObject.class);
        JsonArray simulations = configJson.getAsJsonArray("simulations");
        if (simulations == null || simulations.isEmpty()) {
            System.err.println("No simulation have been configured");
            return;
        }
        JsonElement repeatEntry = configJson.get("eachSimulationRepeat");
        if(repeatEntry != null && !repeatEntry.isJsonNull()){
            eachSimulationRepeat = repeatEntry.getAsInt();
        }

        JsonElement vertexWeightJsonEle = configJson.get("vertexWeightUpperBound");
        if(vertexWeightJsonEle != null && !vertexWeightJsonEle.isJsonNull()){
            vertexWeightUpperBound = vertexWeightJsonEle.getAsInt();
            vertexWeightUpperBound = Math.max(vertexWeightUpperBound,0);
        }

        JsonElement edgeWeightJsonEle = configJson.get("edgeWeightUpperBound");
        if(edgeWeightJsonEle != null && !edgeWeightJsonEle.isJsonNull()){
            edgeWeightUpperBound = edgeWeightJsonEle.getAsInt();
            edgeWeightUpperBound = Math.max(edgeWeightUpperBound,0);
        }

        for (JsonElement simulation : simulations) {
            JsonObject simObj = simulation.getAsJsonObject();
            JsonElement graphJson = simObj.get("graph");

            GraphGenerateStrategies strategy = null;
            int vertex = 2;
            int hideNumber = 1;
            boolean canKnowCostInAdvance = false;

            if (graphJson != null) {
                if (graphJson.getAsJsonObject().get("topology") != null) {
                    String topology = graphJson.getAsJsonObject().get("topology").getAsString().toLowerCase();
                    switch (topology) {
                        case "scalefree":
                            strategy = GraphGenerateStrategies.SCALE_FREE;
                            break;
                        case "empty":
                            strategy = GraphGenerateStrategies.EMPTY;
                            break;
                        case "ring":
                            strategy = GraphGenerateStrategies.RING;
                            break;
                        default:
                            strategy = GraphGenerateStrategies.RANDOM;
                    }
                }else{
                    strategy = GraphGenerateStrategies.RANDOM;
                }

                if (graphJson.getAsJsonObject().get("canKnowCostInAdvance") != null) {
                    vertex = graphJson.getAsJsonObject().get("vertex").getAsInt();
                }

                if (graphJson.getAsJsonObject().get("canKnowCostInAdvance") != null) {
                    canKnowCostInAdvance = graphJson.getAsJsonObject().get("canKnowCostInAdvance").getAsBoolean();
                }

                if (graphJson.getAsJsonObject().get("hideNumber") != null) {
                    hideNumber = graphJson.getAsJsonObject().get("hideNumber").getAsInt();
                }

            }

            GraphConfig graphConfig = new GraphConfig();
            graphConfig.setGenerateStrategy(strategy);
            graphConfig.setVertex(vertex);
            graphConfig.setCanKnowCostInAdvance(canKnowCostInAdvance);
            graphConfig.setHideNumber(hideNumber);

            JsonElement hiderJson = simObj.get("hider");
            AgentConfig hiderCfg = extractAgentConfig(hiderJson);

            JsonElement seekerJson = simObj.get("seeker");
            AgentConfig seekerCfg = extractAgentConfig(seekerJson);

            SimulationConfigEntry simConfig = new SimulationConfigEntry();
            simConfig.setGraphConfig(graphConfig);
            simConfig.setHiderConfig(hiderCfg);
            simConfig.setSeekerConfig(seekerCfg);

            JsonElement rounds = simObj.get("rounds");
            if(rounds != null && !rounds.isJsonNull()){
                simConfig.setRounds(rounds.getAsInt());
            }

            this.simulationConfigEntryList.add(simConfig);
        }
    }

    private AgentConfig extractAgentConfig(JsonElement json) {
        AgentConfig cfg = new AgentConfig();

        if(json != null){
            if (json.getAsJsonObject().get("type") != null) {
                cfg.setType(json.getAsJsonObject().get("type").getAsString());
            }

            if (json.getAsJsonObject().get("strategy") != null) {
                cfg.setStrategy(json.getAsJsonObject().get("strategy").getAsString());
            }

            if (json.getAsJsonObject().get("portion") != null) {
                cfg.setGraphPortion(json.getAsJsonObject().get("portion").getAsDouble());
            }

            if (json.getAsJsonObject().get("deceptiveRounds") != null) {
                cfg.setDeceptiveRounds(json.getAsJsonObject().get("deceptiveRounds").getAsInt());
            }

        }
        return cfg;
    }

    public List<SimulationConfigEntry> getSimulationConfigEntryList() {
        return simulationConfigEntryList;
    }

    public int getEachSimulationRepeat() {
        return eachSimulationRepeat;
    }

    public int getVertexWeightUpperBound() {
        return vertexWeightUpperBound;
    }

    public int getEdgeWeightUpperBound() {
        return edgeWeightUpperBound;
    }
}
