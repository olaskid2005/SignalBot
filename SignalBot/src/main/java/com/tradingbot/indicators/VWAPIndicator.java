package com.tradingbot.indicators;

import com.tradingbot.data.OHLCVData;

import java.util.List;

/**
 * Computes the Volume Weighted Average Price (VWAP) for a given set of OHLCV data.
 */
public class VWAPIndicator {

    /**
     * Calculates the VWAP for a list of OHLCV data.
     *
     * @param data The list of OHLCV data.
     * @return An array of VWAP values corresponding to the input data.
     */
    public double[] calculate(List<OHLCVData> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be null or empty.");
        }

        double[] vwapValues = new double[data.size()];
        double cumulativeTPV = 0; // Cumulative Typical Price * Volume
        double cumulativeVolume = 0; // Cumulative Volume

        for (int i = 0; i < data.size(); i++) {
            OHLCVData ohlcv = data.get(i);

            // Calculate typical price: (High + Low + Close) / 3
            double typicalPrice = (ohlcv.getHigh() + ohlcv.getLow() + ohlcv.getClose()) / 3;

            // Update cumulative TPV and volume
            cumulativeTPV += typicalPrice * ohlcv.getVolume();
            cumulativeVolume += ohlcv.getVolume();

            // Calculate VWAP
            vwapValues[i] = cumulativeVolume > 0 ? cumulativeTPV / cumulativeVolume : 0;
        }

        return vwapValues;
    }
}
