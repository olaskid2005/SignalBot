package com.tradingbot.indicators;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes Bollinger Bands for a given set of closing prices.
 */
public class BollingerBandsIndicator {

    private final int period;
    private final double multiplier;

    /**
     * Constructor for BollingerBandsIndicator.
     *
     * @param period     The lookback period for calculating the moving average and standard deviation.
     * @param multiplier The multiplier for the standard deviation to determine band width.
     */
    public BollingerBandsIndicator(int period, double multiplier) {
        if (period <= 0 || multiplier <= 0) {
            throw new IllegalArgumentException("Period and multiplier must be greater than 0.");
        }
        this.period = period;
        this.multiplier = multiplier;
    }

    /**
     * Calculates the Bollinger Bands for a list of closing prices.
     *
     * @param closingPrices The list of closing prices.
     * @return A 2D array containing upper band, middle band, and lower band values.
     */
    public double[][] calculate(List<Double> closingPrices) {
        if (closingPrices == null || closingPrices.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate Bollinger Bands.");
        }

        double[] upperBand = new double[closingPrices.size()];
        double[] middleBand = new double[closingPrices.size()];
        double[] lowerBand = new double[closingPrices.size()];

        for (int i = period - 1; i < closingPrices.size(); i++) {
            List<Double> window = closingPrices.subList(i - period + 1, i + 1);
            double mean = calculateMean(window);
            double stdDev = calculateStandardDeviation(window, mean);

            middleBand[i] = mean;
            upperBand[i] = mean + (multiplier * stdDev);
            lowerBand[i] = mean - (multiplier * stdDev);
        }

        return new double[][] { upperBand, middleBand, lowerBand };
    }

    /**
     * Calculates the mean of a list of values.
     *
     * @param values The list of values.
     * @return The mean of the values.
     */
    private double calculateMean(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    /**
     * Calculates the standard deviation of a list of values.
     *
     * @param values The list of values.
     * @param mean   The mean of the values.
     * @return The standard deviation of the values.
     */
    private double calculateStandardDeviation(List<Double> values, double mean) {
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.size());
    }

    /**
     * Gets the period for this Bollinger Bands indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Gets the multiplier for this Bollinger Bands indicator.
     *
     * @return The standard deviation multiplier.
     */
    public double getMultiplier() {
        return multiplier;
    }
}
