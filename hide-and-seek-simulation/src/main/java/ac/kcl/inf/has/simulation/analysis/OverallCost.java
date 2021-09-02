package ac.kcl.inf.has.simulation.analysis;

public class OverallCost {

    private String agName;
    private int round;

    private double max;
    private double min;
    private double avg;
    private double q1;
    private double median;
    private double q3;
    private double std;

    public void setAgName(String agName) {
        this.agName = agName;
    }

    public String getAgName() {
        return agName;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getQ1() {
        return q1;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getQ3() {
        return q3;
    }

    public void setQ3(double q3) {
        this.q3 = q3;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    @Override
    public String toString() {
        return "OverallCost{" +
                "agName='" + agName + '\'' +
                ", round=" + round +
                ", max=" + max +
                ", min=" + min +
                ", avg=" + avg +
                ", q1=" + q1 +
                ", median=" + median +
                ", q3=" + q3 +
                ", std=" + std +
                '}';
    }

}
