package com.tradingbot.indicators;

import com.tradingbot.data.OHLCVData;

import java.util.List;

/**
 * Computes the Average Directional Index (ADX) for a given set of OHLCV data.
 */
public class ADXIndicator {

    private final int period;

    /**
     * Constructor for ADXIndicator.
     *
     * @param period The lookback period for ADX calculation.
     */
    public ADXIndicator(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period must be greater than 0.");
        }
        this.period = period;
    }

    /**
     * Calculates the ADX for a list of OHLCV data.
     *
     * @param data The list of OHLCV data.
     * @return An array of ADX values corresponding to the input data.
     */
    public double[] calculate(List<OHLCVData> data) {
        if (data == null || data.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate ADX.");
        }

        double[] adxValues = new double[data.size()];
        double[] trValues = new double[data.size()];
        double[] plusDMValues = new double[data.size()];
        double[] minusDMValues = new double[data.size()];

        // Calculate True Range (TR), +DM, and -DM
        for (int i = 1; i < data.size(); i++) {
            double highDiff = data.get(i).getHigh() - data.get(i - 1).getHigh();
            double lowDiff = data.get(i - 1).getLow() - data.get(i).getLow();

            trValues[i] = Math.max(
                    data.get(i).getHigh() - data.get(i).getLow(),
                    Math.max(Math.abs(data.get(i).getHigh() - data.get(i - 1).getClose()),
                             Math.abs(data.get(i).getLow() - data.get(i - 1).getClose()))
            );

            plusDMValues[i] = highDiff > lowDiff && highDiff > 0 ? highDiff : 0;
            minusDMValues[i] = lowDiff > highDiff && lowDiff > 0 ? lowDiff : 0;
        }

        // Calculate smoothed TR, +DI, -DI, and DX
        double[] smoothedTR = smooth(trValues, period);
        double[] smoothedPlusDM = smooth(plusDMValues, period);
        double[] smoothedMinusDM = smooth(minusDMValues, period);

        double[] plusDI = new double[data.size()];
        double[] minusDI = new double[data.size()];
        double[] dx = new double[data.size()];

        for (int i = period; i < data.size(); i++) {
            plusDI[i] = (smoothedPlusDM[i] / smoothedTR[i]) * 100;
            minusDI[i] = (smoothedMinusDM[i] / smoothedTR[i]) * 100;
            dx[i] = (Math.abs(plusDI[i] - minusDI[i]) / (plusDI[i] + minusDI[i])) * 100;
        }

        // Calculate ADX
        double[] smoothedDX = smooth(dx, period);
        System.arraycopy(smoothedDX, 0, adxValues, 0, data.size());

        return adxValues;
    }

    /**
     * Smooths the input values using a rolling sum over the specified period.
     *
     * @param values The input values to smooth.
     * @param period The lookback period for smoothing.
     * @return An array of smoothed values.
     */
    private double[] smooth(double[] values, int period) {
        double[] smoothed = new double[values.length];
        double sum = 0;

        for (int i = 0; i < values.length; i++) {
            sum += values[i];
            if (i >= period) {
                sum -= values[i - period];
            }

            smoothed[i] = i >= period - 1 ? sum / period : 0;
        }

        return smoothed;
    }

    /**
     * Gets the period for this ADX indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }
}
