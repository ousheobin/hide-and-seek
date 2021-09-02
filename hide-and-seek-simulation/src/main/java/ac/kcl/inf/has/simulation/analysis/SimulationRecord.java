package ac.kcl.inf.has.simulation.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationRecord {

    private Map<String, List<List<Double>>> eachRoundCostMap;

    public SimulationRecord(){
        eachRoundCostMap = new ConcurrentHashMap<>();
    }

    public void addEachRoundCost(String ag, List<Double> values){
        if(!eachRoundCostMap.containsKey(ag)){
            eachRoundCostMap.putIfAbsent(ag, new CopyOnWriteArrayList<>());
        }
        eachRoundCostMap.get(ag).add(values);
    }

    public List<OverallCost> calculateCost(){

        List<OverallCost> costs = new ArrayList<>();
        for (String ag: eachRoundCostMap.keySet()){
            List<List<Double>> values = eachRoundCostMap.get(ag);

            int repeatTimes = values.size();
            int rounds = values.get(0).size();

            for (int r = 1; r <= rounds ; r ++){
                List<Double> repeatData = new ArrayList<>();
                for (int repeat = 0; repeat < repeatTimes; repeat ++){
                    repeatData.add(values.get(repeat).get(r-1));
                }
                Collections.sort(repeatData);
                OverallCost cost = new OverallCost();
                cost.setAgName(ag);
                cost.setRound(r);
                cost.setMax(repeatData.get(repeatTimes -1));
                cost.setMin(repeatData.get(0));
                cost.setMedian(repeatData.get(Math.min(Math.round(repeatTimes * 0.5f), repeatTimes- 1)));
                cost.setQ1(repeatData.get(Math.min(Math.round(repeatTimes * 0.25f), repeatTimes- 1)));
                cost.setQ3(repeatData.get(Math.min(Math.round(repeatTimes * 0.75f), repeatTimes- 1)));
                double sum = 0;
                for (int i = 0 ; i < repeatTimes ; i ++ ){
                    sum += repeatData.get(i);
                }
                double avg = sum / repeatTimes;
                cost.setAvg(avg);
                double std = 0.0;
                for (int i = 0 ; i < repeatTimes ; i ++ ){
                    std += Math.pow((repeatData.get(i) - avg), 2);
                }
                cost.setStd(Math.sqrt(std / repeatTimes));
                costs.add(cost);
            }

        }

        return costs;
    }

    public List<EachRoundCost> getEachRoundCost(){
        List<EachRoundCost> costs = new ArrayList<>();
        for (String ag: eachRoundCostMap.keySet()){
            costs.add(new EachRoundCost(ag,eachRoundCostMap.get(ag)));
        }
        return costs;
    }

}
