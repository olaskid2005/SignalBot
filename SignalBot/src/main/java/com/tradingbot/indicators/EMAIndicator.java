package com.tradingbot.indicators;

import java.util.List;

/**
 * Computes the Exponential Moving Average (EMA) for a given set of prices.
 */
public class EMAIndicator {

    private final int period;

    /**
     * Constructor for EMAIndicator.
     *
     * @param period The lookback period for EMA calculation.
     */
    public EMAIndicator(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period must be greater than 0.");
        }
        this.period = period;
    }

    /**
     * Calculates the EMA for a list of prices.
     *
     * @param prices The list of prices.
     * @return An array of EMA values corresponding to the input prices.
     */
    public double[] calculate(List<Double> prices) {
        if (prices == null || prices.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate EMA.");
        }

        double[] emaValues = new double[prices.size()];
        double multiplier = 2.0 / (period + 1);

        // Initialize the first EMA value with the SMA of the first period
        double sum = 0;
        for (int i = 0; i < period; i++) {
            sum += prices.get(i);
        }
        emaValues[period - 1] = sum / period;

        // Calculate EMA for the rest of the values
        for (int i = period; i < prices.size(); i++) {
            emaValues[i] = ((prices.get(i) - emaValues[i - 1]) * multiplier) + emaValues[i - 1];
        }

        return emaValues;
    }

    /**
     * Gets the period for this EMA indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }
}
