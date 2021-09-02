package ac.kcl.inf.has.simulation.analysis;

import java.util.ArrayList;
import java.util.List;

public class EachRoundCost {

    private String agName;

    private List<Double> eachRoundCost;

    public EachRoundCost(String agName, List<List<Double>> values){
        this.init(values);
        this.agName = agName;
    }

    private void init(List<List<Double>> values){
        
        if(values.isEmpty()){
            throw new IllegalArgumentException("Values is empty");
        }
        int repeatTimes = values.size();
        int index = 0;
        List<Double> eachRepeat = values.get(index);

        if(eachRepeat.isEmpty()){
            throw new IllegalArgumentException("Values is empty");
        }
        int rounds = eachRepeat.size();

        this.eachRoundCost = new ArrayList<>();
        for (int i = 0; i < rounds; i ++ ) {
            eachRoundCost.add(0.0d);
        }

        while (index < repeatTimes){

            for (int i = 0; i < rounds ; i ++){
                eachRoundCost.set(i, eachRoundCost.get(i) + eachRepeat.get(i));
            }

            index ++;
            if(index < repeatTimes){
                eachRepeat = values.get(index);
            }

        }

        for (int i = 0; i < rounds; i ++ ) {
            eachRoundCost.set(i, eachRoundCost.get(i) / repeatTimes);
        }

    }

    public String getAgName() {
        return agName;
    }

    public List<Double> getEachRoundCost() {
        return eachRoundCost;
    }
}
