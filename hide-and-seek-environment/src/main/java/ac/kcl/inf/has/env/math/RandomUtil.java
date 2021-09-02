package ac.kcl.inf.has.env.math;

import java.util.Random;

public class RandomUtil {

    private Random random;

    public RandomUtil(){
        random = new Random();
    }

    public double getNextRandom() {
        return random.nextDouble();
    }

    public double getNextRandom(int upperBound){
        return random.nextDouble() * upperBound;
    }

}
