package com.tradingbot.indicators;

import java.util.List;

/**
 * Computes the Stochastic RSI (Relative Strength Index) for a given set of RSI values.
 */
public class StochasticRSIIndicator {

    private final int period;

    /**
     * Constructor for StochasticRSIIndicator.
     *
     * @param period The lookback period for the Stochastic RSI calculation.
     */
    public StochasticRSIIndicator(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period must be greater than 0.");
        }
        this.period = period;
    }

    /**
     * Calculates the Stochastic RSI for a list of RSI values.
     *
     * @param rsiValues The list of RSI values.
     * @return An array of Stochastic RSI values corresponding to the input RSI values.
     */
    public double[] calculate(List<Double> rsiValues) {
        if (rsiValues == null || rsiValues.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate Stochastic RSI.");
        }

        double[] stochasticRSIValues = new double[rsiValues.size()];

        for (int i = period - 1; i < rsiValues.size(); i++) {
            double minRSI = Double.MAX_VALUE;
            double maxRSI = Double.MIN_VALUE;

            // Calculate min and max RSI over the lookback period
            for (int j = i - period + 1; j <= i; j++) {
                minRSI = Math.min(minRSI, rsiValues.get(j));
                maxRSI = Math.max(maxRSI, rsiValues.get(j));
            }

            // Calculate Stochastic RSI
            if (maxRSI - minRSI == 0) {
                stochasticRSIValues[i] = 0; // Prevent division by zero
            } else {
                stochasticRSIValues[i] = (rsiValues.get(i) - minRSI) / (maxRSI - minRSI);
            }
        }

        return stochasticRSIValues;
    }

    /**
     * Gets the period for this Stochastic RSI indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }
}
