package com.tradingbot.indicators;

import java.util.List;

/**
 * Computes the Momentum indicator for a given set of prices.
 */
public class MomentumIndicator {

    private final int period;

    /**
     * Constructor for MomentumIndicator.
     *
     * @param period The lookback period for momentum calculation.
     */
    public MomentumIndicator(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period must be greater than 0.");
        }
        this.period = period;
    }

    /**
     * Calculates the Momentum indicator for a list of prices.
     *
     * @param prices The list of prices.
     * @return An array of Momentum values corresponding to the input prices.
     */
    public double[] calculate(List<Double> prices) {
        if (prices == null || prices.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate Momentum.");
        }

        double[] momentumValues = new double[prices.size()];

        for (int i = period; i < prices.size(); i++) {
            momentumValues[i] = prices.get(i) - prices.get(i - period);
        }

        return momentumValues;
    }

    /**
     * Gets the period for this Momentum indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }
}
