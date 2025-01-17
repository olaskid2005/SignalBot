package com.tradingbot.indicators;

import com.tradingbot.data.OHLCVData;

import java.util.List;

/**
 * Computes the Money Flow Index (MFI) for a given set of OHLCV data.
 */
public class MFIIndicator {

    private final int period;

    /**
     * Constructor for MFIIndicator.
     *
     * @param period The lookback period for MFI calculation.
     */
    public MFIIndicator(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period must be greater than 0.");
        }
        this.period = period;
    }

    /**
     * Calculates the MFI for a list of OHLCV data.
     *
     * @param data The list of OHLCV data.
     * @return An array of MFI values corresponding to the input data.
     */
    public double[] calculate(List<OHLCVData> data) {
        if (data == null || data.size() < period) {
            throw new IllegalArgumentException("Not enough data to calculate MFI.");
        }

        double[] mfiValues = new double[data.size()];

        for (int i = period; i < data.size(); i++) {
            double positiveFlow = 0;
            double negativeFlow = 0;

            for (int j = i - period + 1; j <= i; j++) {
                OHLCVData current = data.get(j);
                OHLCVData previous = data.get(j - 1);

                // Calculate typical price: (High + Low + Close) / 3
                double currentTypicalPrice = (current.getHigh() + current.getLow() + current.getClose()) / 3;
                double previousTypicalPrice = (previous.getHigh() + previous.getLow() + previous.getClose()) / 3;

                // Calculate money flow: Typical Price * Volume
                double moneyFlow = currentTypicalPrice * current.getVolume();

                if (currentTypicalPrice > previousTypicalPrice) {
                    positiveFlow += moneyFlow;
                } else if (currentTypicalPrice < previousTypicalPrice) {
                    negativeFlow += moneyFlow;
                }
            }

            // Calculate Money Flow Ratio and MFI
            double moneyFlowRatio = positiveFlow / (negativeFlow == 0 ? 1 : negativeFlow);
            mfiValues[i] = 100 - (100 / (1 + moneyFlowRatio));
        }

        return mfiValues;
    }

    /**
     * Gets the period for this MFI indicator.
     *
     * @return The lookback period.
     */
    public int getPeriod() {
        return period;
    }
}
