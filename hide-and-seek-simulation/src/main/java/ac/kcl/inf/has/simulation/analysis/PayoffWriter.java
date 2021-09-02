package ac.kcl.inf.has.simulation.analysis;

import ac.kcl.inf.has.simulation.config.SimulationConfigEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayoffWriter {

    private String simId;
    private File resultFolder;

    private FileWriter overallWriter;
    private FileWriter eachRoundWriter;

    public PayoffWriter(File resultFolder) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        this.simId = sdf.format(new Date());
        this.resultFolder = resultFolder;
        if(!this.resultFolder.exists()){
            if(!this.resultFolder.mkdirs()){
                throw new RuntimeException("Unable to create result folder:" + resultFolder.getAbsolutePath());
            }
        }
        this.init();
    }

    private void init() throws IOException {
        overallWriter = new FileWriter(new File(this.resultFolder, simId+".csv"));
        eachRoundWriter = new FileWriter(new File(this.resultFolder, simId + "_rounds.csv"));

        overallWriter.write("type,hiderStrategy,seekerStrategy,graph,n,k,round,min,max,avg,25%,median,75%,std,payoff\n");
        eachRoundWriter.write("hiderStrategy,seekerStrategy,graph,n,k,round,hiderPayoff,seekerPayoff\n");
    }

    public void writeOverallData(SimulationConfigEntry config, List<OverallCost> overallCosts) throws IOException {

        StringBuilder data = new StringBuilder();

        double hiderCost = 0.0;
        double seekerCost = 0.0;

        Map<String, Double> costMap = new HashMap<>();
        for (OverallCost cost: overallCosts){
            if (cost.getAgName().equalsIgnoreCase("hider")) {
                costMap.put("hider-"+cost.getRound(),cost.getAvg());
            }else{
                costMap.put("seeker-"+cost.getRound(),cost.getAvg());
            }
        }

        for (OverallCost cost: overallCosts){
            data.append(cost.getAgName());
            data.append(",\"");
            data.append(config.getHiderConfig().getStrategy());
            data.append("\",\"");
            data.append(config.getSeekerConfig().getStrategy());
            data.append("\",\"");
            data.append(config.getGraphConfig().formatStr());
            data.append("\",");
            data.append(config.getGraphConfig().getVertex());
            data.append(',');
            data.append(config.getGraphConfig().getHideNumber());
            data.append(',');
            data.append(cost.getRound());
            data.append(',');
            data.append(cost.getMin());
            data.append(',');
            data.append(cost.getMax());
            data.append(',');
            data.append(cost.getAvg());
            data.append(',');
            data.append(cost.getQ1());
            data.append(',');
            data.append(cost.getMedian());
            data.append(',');
            data.append(cost.getQ3());
            data.append(',');
            data.append(cost.getStd());
            data.append(',');
            double payoff = 0;
            if (cost.getAgName().equalsIgnoreCase("hider")) {
                payoff = costMap.get("seeker-"+cost.getRound()) - costMap.get("hider-"+cost.getRound());
            }else {
                payoff = -1 * costMap.get("seeker-"+cost.getRound());
            }
            data.append(payoff);
            data.append('\n');
        }


        overallWriter.write(data.toString());
        overallWriter.flush();
    }

    public void writeEachRoundData(SimulationConfigEntry config, List<EachRoundCost> eachRoundValue) throws IOException{
        StringBuilder data = new StringBuilder();

        if(eachRoundValue.size() != 2){
            throw new RuntimeException();
        }

        List<Double> hiderVal = null;
        List<Double> seekerVal = null;

        for (EachRoundCost roundCost: eachRoundValue){
            if(roundCost.getAgName().equalsIgnoreCase("hider")){
                hiderVal = roundCost.getEachRoundCost();
            }else{
                seekerVal = roundCost.getEachRoundCost();
            }
        }

        if(hiderVal == null || seekerVal == null || hiderVal.size() != seekerVal.size()){
            throw new RuntimeException();
        }

        StringBuilder prefix = new StringBuilder();
        prefix.append('"');
        prefix.append(config.getHiderConfig().getStrategy());
        prefix.append("\",\"");
        prefix.append(config.getSeekerConfig().getStrategy());
        prefix.append("\",\"");
        prefix.append(config.getGraphConfig().formatStr());
        prefix.append("\",");
        prefix.append(config.getGraphConfig().getVertex());
        prefix.append(',');
        prefix.append(config.getGraphConfig().getHideNumber());

        int size = seekerVal.size();
        for (int i = 0; i < size; i ++ ){
            data.append(prefix);
            data.append(',');
            data.append((i + 1));
            data.append(',');
            data.append(seekerVal.get(i) - hiderVal.get(i));
            data.append(',');
            data.append(-1 * seekerVal.get(i));
            data.append('\n');
        }

        eachRoundWriter.write(data.toString());
        eachRoundWriter.flush();
    }

    public void close() throws IOException {
        this.overallWriter.close();
        this.eachRoundWriter.close();
    }


}
