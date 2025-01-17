package com.tradingbot.signals;

import com.tradingbot.data.OHLCVData;
import com.tradingbot.indicators.*;
import com.tradingbot.ml.MLModel;

import java.util.List;

/**
 * Generates trading signals based on indicators and machine learning models.
 */
public class SignalGenerator {

    private final RSIIndicator rsiIndicator;
    private final MACDIndicator macdIndicator;
    private final BollingerBandsIndicator bollingerBandsIndicator;
    private final MLModel mlModel;

    /**
     * Constructor for SignalGenerator.
     *
     * @param rsiPeriod        Period for RSI calculation.
     * @param macdShortPeriod  Short period for MACD calculation.
     * @param macdLongPeriod   Long period for MACD calculation.
     * @param macdSignalPeriod Signal line period for MACD calculation.
     * @param bollingerPeriod  Period for Bollinger Bands calculation.
     * @param bollingerMultiplier Multiplier for Bollinger Bands width.
     * @param mlModel          Machine learning model for advanced predictions.
     */
    public SignalGenerator(int rsiPeriod, int macdShortPeriod, int macdLongPeriod, int macdSignalPeriod,
                           int bollingerPeriod, double bollingerMultiplier, MLModel mlModel) {
        this.rsiIndicator = new RSIIndicator(rsiPeriod);
        this.macdIndicator = new MACDIndicator(macdShortPeriod, macdLongPeriod, macdSignalPeriod);
        this.bollingerBandsIndicator = new BollingerBandsIndicator(bollingerPeriod, bollingerMultiplier);
        this.mlModel = mlModel;
    }

    /**
     * Generates a signal based on the latest market data.
     *
     * @param data The list of OHLCV data.
     * @return A trading signal: "Buy", "Sell", or "Hold".
     */
    public String generateSignal(List<OHLCVData> data) {
        if (data == null || data.size() < 1) {
            throw new IllegalArgumentException("Insufficient market data for signal generation.");
        }

        OHLCVData latestData = data.get(data.size() - 1);

        // Calculate indicators
        double[] rsiValues = rsiIndicator.calculate(data);
        double[][] macdValues = macdIndicator.calculate(data.stream().map(OHLCVData::getClose).toList());
        double[][] bollingerValues = bollingerBandsIndicator.calculate(data.stream().map(OHLCVData::getClose).toList());

        double latestRSI = rsiValues[rsiValues.length - 1];
        double latestMACD = macdValues[0][macdValues[0].length - 1];
        double macdSignal = macdValues[1][macdValues[1].length - 1];
        double upperBand = bollingerValues[0][bollingerValues[0].length - 1];
        double lowerBand = bollingerValues[2][bollingerValues[2].length - 1];

        // Indicator-based decision
        if (latestRSI < 30 && latestData.getClose() < lowerBand && latestMACD > macdSignal) {
            return "Buy";
        } else if (latestRSI > 70 && latestData.getClose() > upperBand && latestMACD < macdSignal) {
            return "Sell";
        }

        // Use machine learning model for additional validation
        double[] features = { latestRSI, latestMACD, macdSignal, latestData.getClose(), upperBand, lowerBand };
        double mlPrediction = mlModel.predict(List.of(features)).get(0);

        if (mlPrediction > 0.7) {
            return "Buy";
        } else if (mlPrediction < 0.3) {
            return "Sell";
        }

        return "Hold";
    }
}
