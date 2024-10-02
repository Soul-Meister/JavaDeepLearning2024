package org.edward;

import java.util.Random;

public class Neuron {
    private double[] weights;
    private double bias;
    private double output;
    private double delta; // For backpropagation
    private double[] inputs;
    private ActivationFunction activationFunction = new LeakyReLUActivation(0.01);

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    // Constructor to create a Neuron with a given number of inputs, without initializing weights
    public Neuron(int numInputs) {
        weights = new double[numInputs];
        bias = 0.0; // Default bias value, will be set later
    }

    // Method to initialize weights and bias using He initialization
    public void initializeWeights(double stddev, Random rand) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rand.nextGaussian() * stddev;
        }
        bias = rand.nextGaussian() * stddev;
    }

    public double activate(double[] inputs, ActivationFunction activationFunction) {
        this.inputs = inputs;
        double sum = bias;
        for (int i = 0; i < inputs.length; i++) {
            sum += weights[i] * inputs[i];
        }
        output = activationFunction.activate(sum);
        return output;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getOutput() {
        return output;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getInput(int i) {
        return inputs[i];
    }
}
