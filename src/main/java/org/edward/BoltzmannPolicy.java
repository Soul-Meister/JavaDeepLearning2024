package org.edward;

import java.util.Random;

public class BoltzmannPolicy implements Policy {
    private double temperature;
    private double decayRate;
    private double minTemperature;
    private Random random;

    public BoltzmannPolicy(double initialTemperature, double decayRate, double minTemperature) {
        this.temperature = initialTemperature;
        this.decayRate = decayRate;
        this.minTemperature = minTemperature;
        this.random = new Random();
    }

    // Select action using Boltzmann (softmax) exploration
    public int selectAction(double[] qValues) {
        double[] probabilities = new double[qValues.length];
        double sum = 0.0;

        // Compute probabilities using softmax function
        for (int i = 0; i < qValues.length; i++) {
            probabilities[i] = Math.exp(qValues[i] / temperature);
            sum += probabilities[i];
        }

        // Normalize the probabilities
        for (int i = 0; i < qValues.length; i++) {
            probabilities[i] /= sum;
        }

        // Randomly select action based on computed probabilities
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i;
            }
        }

        // Fallback in case of numerical issues
        return qValues.length - 1;
    }

    // Decay the temperature over time to reduce exploration
    public void decayDelta() {
        if (temperature > minTemperature) {
            temperature *= decayRate;
            if (temperature < minTemperature) {
                temperature = minTemperature;
            }
        }
    }

    // Get the current temperature
    public double getDelta() {
        return temperature;
    }
}
