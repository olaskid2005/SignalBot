package com.tradingbot.indicators;

import com.tradingbot.data.OHLCVData;

import java.util.List;

/**
 * Computes the Relative Strength Index (RSI) for a given set of OHLCV data.
 */
public class RSIIndicator {

    private final int period;

    /**
     * Constructor for RSIIndicator.
     *
     * @param period The lookback period for RSI calculation.
     */
    public RSIIndicator(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period must be greater than 0.");
        }
        this.period = period;
    }

    /**
     * Calculates the RSI for a list of OHLCV data.
     *
     * @param data The list of OHLCV data.
     * @return An array of RSI values corresponding to the input data.
     */
    public double[] calculate(List<OHLCVData> data) {
        if (data == null || data.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate RSI.");
        }

        double[] rsiValues = new double[data.size()];

        double gainSum = 0;
        double lossSum = 0;

        // Initialize with the first period gains/losses
        for (int i = 1; i <= period; i++) {
            double change = data.get(i).getClose() - data.get(i - 1).getClose();
            if (change > 0) {
                gainSum += change;
            } else {
                lossSum += Math.abs(change);
            }
        }

        double avgGain = gainSum / period;
        double avgLoss = lossSum / period;

        // Calculate RSI for the first valid point
        rsiValues[period] = calculateRSI(avgGain, avgLoss);

        // Calculate RSI for subsequent points
        for (int i = period + 1; i < data.size(); i++) {
            double change = data.get(i).getClose() - data.get(i - 1).getClose();
            double gain = Math.max(0, change);
            double loss = Math.max(0, -change);

            avgGain = (avgGain * (period - 1) + gain) / period;
            avgLoss = (avgLoss * (period - 1) + loss) / period;

            rsiValues[i] = calculateRSI(avgGain, avgLoss);
        }

        return rsiValues;
    }

    /**
     * Calculates the RSI value given average gains and losses.
     *
     * @param avgGain The average gain.
     * @param avgLoss The average loss.
     * @return The RSI value.
     */
    private double calculateRSI(double avgGain, double avgLoss) {
        if (avgLoss == 0) {
            return 100; // Overbought
        }
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    /**
     * Gets the period for this RSI indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }
}
