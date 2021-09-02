package ac.kcl.inf.has.agent.execution;

import ac.kcl.inf.has.agent.player.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataRecorder {

    private ConcurrentHashMap<Agent, List<Double>> dataMap;

    private DataRecorder(){
        this.dataMap = new ConcurrentHashMap<>();
    }

    public static DataRecorder getDataRecorder(){
        return new DataRecorder();
    }

    public void newRound(Agent ag){
        if(!dataMap.containsKey(ag)){
            dataMap.putIfAbsent(ag,new ArrayList<>());
        }
        List<Double> dataList = dataMap.get(ag);
        dataList.add(0.0);
    }

    public void addCost(Agent ag, double weight){
        List<Double> dataList = dataMap.get(ag);
        int size = dataList.size();
        dataList.set( size - 1, dataList.get(size - 1) + weight);
    }

    public ConcurrentHashMap<Agent, List<Double>> getDataMap() {
        return dataMap;
    }

}
