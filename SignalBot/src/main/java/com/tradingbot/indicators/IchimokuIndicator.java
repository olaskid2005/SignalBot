package com.tradingbot.indicators;

import com.tradingbot.data.OHLCVData;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes the Ichimoku Cloud indicator for a given set of OHLCV data.
 */
public class IchimokuIndicator {

    private final int tenkanPeriod; // Conversion Line Period
    private final int kijunPeriod;  // Base Line Period
    private final int senkouSpanBPeriod; // Leading Span B Period
    private final int chikouSpanLag; // Lagging Span Period

    /**
     * Constructor for IchimokuIndicator.
     *
     * @param tenkanPeriod The period for the Conversion Line (Tenkan-sen).
     * @param kijunPeriod The period for the Base Line (Kijun-sen).
     * @param senkouSpanBPeriod The period for the Leading Span B (Senkou Span B).
     * @param chikouSpanLag The lagging period for the Chikou Span.
     */
    public IchimokuIndicator(int tenkanPeriod, int kijunPeriod, int senkouSpanBPeriod, int chikouSpanLag) {
        if (tenkanPeriod <= 0 || kijunPeriod <= 0 || senkouSpanBPeriod <= 0 || chikouSpanLag <= 0) {
            throw new IllegalArgumentException("All periods must be greater than 0.");
        }
        this.tenkanPeriod = tenkanPeriod;
        this.kijunPeriod = kijunPeriod;
        this.senkouSpanBPeriod = senkouSpanBPeriod;
        this.chikouSpanLag = chikouSpanLag;
    }

    /**
     * Calculates the Ichimoku Cloud components for a list of OHLCV data.
     *
     * @param data The list of OHLCV data.
     * @return A 2D array containing Ichimoku components: [Tenkan-sen, Kijun-sen, Senkou Span A, Senkou Span B, Chikou Span].
     */
    public double[][] calculate(List<OHLCVData> data) {
        if (data == null || data.size() < Math.max(tenkanPeriod, Math.max(kijunPeriod, senkouSpanBPeriod))) {
            throw new IllegalArgumentException("Not enough data to calculate Ichimoku components.");
        }

        double[] tenkanSen = calculateLine(data, tenkanPeriod);
        double[] kijunSen = calculateLine(data, kijunPeriod);

        // Senkou Span A: Average of Tenkan-sen and Kijun-sen, plotted forward KijunPeriod
        double[] senkouSpanA = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            if (i >= kijunPeriod - 1) {
                senkouSpanA[i] = (tenkanSen[i] + kijunSen[i]) / 2;
            }
        }

        // Senkou Span B: Highest High + Lowest Low over SenkouSpanBPeriod, plotted forward KijunPeriod
        double[] senkouSpanB = calculateLine(data, senkouSpanBPeriod);

        // Chikou Span: Closing price, plotted backward ChikouSpanLag periods
        double[] chikouSpan = new double[data.size()];
        for (int i = 0; i < data.size() - chikouSpanLag; i++) {
            chikouSpan[i] = data.get(i + chikouSpanLag).getClose();
        }

        return new double[][] { tenkanSen, kijunSen, senkouSpanA, senkouSpanB, chikouSpan };
    }

    /**
     * Calculates a line (Tenkan-sen, Kijun-sen, or Senkou Span B) for a given period.
     *
     * @param data The list of OHLCV data.
     * @param period The lookback period for the line calculation.
     * @return An array of line values.
     */
    private double[] calculateLine(List<OHLCVData> data, int period) {
        double[] line = new double[data.size()];
        for (int i = period - 1; i < data.size(); i++) {
            double highestHigh = Double.MIN_VALUE;
            double lowestLow = Double.MAX_VALUE;

            for (int j = i - period + 1; j <= i; j++) {
                highestHigh = Math.max(highestHigh, data.get(j).getHigh());
                lowestLow = Math.min(lowestLow, data.get(j).getLow());
            }

            line[i] = (highestHigh + lowestLow) / 2;
        }
        return line;
    }

    /**
     * Gets the period for Tenkan-sen.
     *
     * @return The period for the Conversion Line (Tenkan-sen).
     */
    public int getTenkanPeriod() {
        return tenkanPeriod;
    }

    /**
     * Gets the period for Kijun-sen.
     *
     * @return The period for the Base Line (Kijun-sen).
     */
    public int getKijunPeriod() {
        return kijunPeriod;
    }

    /**
     * Gets the period for Senkou Span B.
     *
     * @return The period for the Leading Span B (Senkou Span B).
     */
    public int getSenkouSpanBPeriod() {
        return senkouSpanBPeriod;
    }

    /**
     * Gets the lagging period for Chikou Span.
     *
     * @return The lagging period for the Chikou Span.
     */
    public int getChikouSpanLag() {
        return chikouSpanLag;
    }
}
