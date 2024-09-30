package org.edward;

import java.util.LinkedList;
import java.util.Random;
import java.util.PriorityQueue;

public class ReplayBuffer {
    private int bufferSize;
    private PriorityQueue<Experience> buffer; // Use priority queue to store experiences
    private Random random;
    private double alpha; // Controls how much prioritization is used

    public ReplayBuffer(int size, double alpha) {
        this.bufferSize = size;
        this.alpha = alpha;
        this.buffer = new PriorityQueue<>((e1, e2) -> Double.compare(e2.priority, e1.priority)); // Higher priority experiences come first
        this.random = new Random();
    }

    // Add experiences with a priority
    public void addExperience(Experience experience) {
        // Use combined priority (based on score and TD error)
        if (buffer.size() >= bufferSize) {
            buffer.poll(); // Remove the lowest priority experience if the buffer is full
        }
        buffer.add(experience);
    }


    // Get a batch of prioritized experiences
    public Experience[] getBatch(int batchSize) {
        Experience[] batch = new Experience[batchSize];

        // Total sum of priorities raised to the power of alpha (controls how much prioritization)
        double totalPriority = buffer.stream().mapToDouble(e -> Math.pow(e.priority, alpha)).sum();

        for (int i = 0; i < batchSize; i++) {
            // Randomly sample based on weighted priority
            double rand = random.nextDouble() * totalPriority;

            double cumulativePriority = 0;
            for (Experience exp : buffer) {
                cumulativePriority += Math.pow(exp.priority, alpha);
                if (cumulativePriority >= rand) {
                    batch[i] = exp;
                    break;
                }
            }
        }

        return batch;
    }



    public boolean isReady(int batchSize) {
        return buffer.size() >= batchSize;
    }
}

class Experience {
    public double[] state;
    public int action;
    public double reward;
    public double[] nextState;
    public double priority; // Combined priority based on score and TD error
    public double score;    // Separate score attribute
    public double tdError;  // TD error

    public Experience(double[] state, int action, double reward, double[] nextState, double score, double tdError) {
        this.state = state;
        this.action = action;
        this.reward = reward;
        this.nextState = nextState;
        this.score = score;
        this.tdError = tdError;
        this.priority = calculatePriority(); // Calculate combined priority
    }

    // Method to calculate combined priority using both score and TD error
    private double calculatePriority() {
        // Weighing score and TD error to derive overall priority (you can adjust the weight)
        double scoreWeight = 0.3;
        double tdErrorWeight = 0.7;
        return scoreWeight * score + tdErrorWeight * tdError;
    }
}

