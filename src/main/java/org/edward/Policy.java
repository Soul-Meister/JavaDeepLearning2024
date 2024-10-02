package org.edward;

public interface Policy {
    public int selectAction(double[] qValues);
}
