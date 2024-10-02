package org.edward;

import java.util.Random;

public class EpsilonGreedy implements Policy{
    private double epsilon;
    private double decayRate;
    private double minEpsilon;
    private Random random;

    public EpsilonGreedy(double initialEpsilon, double decayRate, double minEpsilon) {
        this.epsilon = initialEpsilon;
        this.decayRate = decayRate;
        this.minEpsilon = minEpsilon;
        this.random = new Random();
    }

    public int selectActionWithNoise(double[] qValues) {
        double noiseLevel = 0.1; // Adjust this value
        for (int i = 0; i < qValues.length; i++) {
            qValues[i] += (Math.random() - 0.5) * noiseLevel; // Adding small noise
        }

        // Select the action with the maximum (noisy) Q-value
        int bestAction = 0;
        for (int i = 1; i < qValues.length; i++) {
            if (qValues[i] > qValues[bestAction]) {
                bestAction = i;
            }
        }
        return bestAction;
    }

    public int selectAction(double[] qValues) {
        if (Math.random() < epsilon) {
            // Random action for exploration
            //return random.nextInt(qValues.length);
            if(Math.floor(Math.random()* 40) <= 2){
                return 0;
            }
            return 1;
        } else {
            // Use noise-perturbed Q-values for more controlled exploration
            return getMaxIndex(qValues);
           // return selectActionWithNoise(qValues);
        }
    }

    private int getMaxIndex(double[] values) {
        int maxIndex = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] > values[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void decayDelta() {
        if (epsilon > minEpsilon) {
            epsilon *= decayRate;
            if (epsilon < minEpsilon) {
                epsilon = minEpsilon;
            }
        }
    }

    public double getDelta() {
        return epsilon;
    }
}
