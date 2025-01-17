package com.tradingbot.indicators;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes the Moving Average Convergence Divergence (MACD) indicator.
 */
public class MACDIndicator {

    private final int shortPeriod;
    private final int longPeriod;
    private final int signalPeriod;

    /**
     * Constructor for MACDIndicator.
     *
     * @param shortPeriod  The short EMA period.
     * @param longPeriod   The long EMA period.
     * @param signalPeriod The signal line EMA period.
     */
    public MACDIndicator(int shortPeriod, int longPeriod, int signalPeriod) {
        if (shortPeriod <= 0 || longPeriod <= 0 || signalPeriod <= 0) {
            throw new IllegalArgumentException("Periods must be greater than 0.");
        }
        if (shortPeriod >= longPeriod) {
            throw new IllegalArgumentException("Short period must be less than long period.");
        }
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
        this.signalPeriod = signalPeriod;
    }

    /**
     * Calculates the MACD values for a list of closing prices.
     *
     * @param closingPrices The list of closing prices.
     * @return A 2D array containing MACD Line, Signal Line, and Histogram values.
     */
    public double[][] calculate(List<Double> closingPrices) {
        if (closingPrices == null || closingPrices.size() < longPeriod) {
            throw new IllegalArgumentException("Not enough data to calculate MACD.");
        }

        double[] shortEma = calculateEMA(closingPrices, shortPeriod);
        double[] longEma = calculateEMA(closingPrices, longPeriod);

        double[] macdLine = new double[closingPrices.size()];
        for (int i = 0; i < closingPrices.size(); i++) {
            macdLine[i] = shortEma[i] - longEma[i];
        }

        // Convert macdLine (double[]) to List<Double>
        List<Double> macdLineList = new ArrayList<>();
        for (double value : macdLine) {
            macdLineList.add(value);
        }

        double[] signalLine = calculateEMA(macdLineList, signalPeriod);
        double[] histogram = new double[closingPrices.size()];
        for (int i = 0; i < closingPrices.size(); i++) {
            histogram[i] = macdLine[i] - signalLine[i];
        }

        return new double[][] { macdLine, signalLine, histogram };
    }

    /**
     * Calculates the Exponential Moving Average (EMA) for a list of values.
     *
     * @param values The list of values.
     * @param period The period for the EMA.
     * @return An array of EMA values.
     */
    private double[] calculateEMA(List<Double> values, int period) {
        double[] ema = new double[values.size()];
        double multiplier = 2.0 / (period + 1);

        // Initialize the first EMA value with the first closing price
        ema[period - 1] = values.stream().limit(period).mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Calculate EMA for the rest of the values
        for (int i = period; i < values.size(); i++) {
            ema[i] = ((values.get(i) - ema[i - 1]) * multiplier) + ema[i - 1];
        }

        return ema;
    }

    /**
     * Gets the short EMA period.
     *
     * @return The short EMA period.
     */
    public int getShortPeriod() {
        return shortPeriod;
    }

    /**
     * Gets the long EMA period.
     *
     * @return The long EMA period.
     */
    public int getLongPeriod() {
        return longPeriod;
    }

    /**
     * Gets the signal line EMA period.
     *
     * @return The signal line EMA period.
     */
    public int getSignalPeriod() {
        return signalPeriod;
    }
}
