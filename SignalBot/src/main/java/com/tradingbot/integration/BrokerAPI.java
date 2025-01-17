package com.tradingbot.integration;

import com.tradingbot.risk.RiskManager;
import com.tradingbot.signals.SignalGenerator;
import com.tradingbot.data.OHLCVData;

import java.util.List;

/**
 * Provides trade suggestions and displays actionable signals for manual execution.
 */
public class BrokerAPI {

    private final SignalGenerator signalGenerator;
    private final RiskManager riskManager;

    /**
     * Constructor for BrokerAPI.
     *
     * @param signalGenerator The SignalGenerator instance for generating signals.
     * @param riskManager     The RiskManager instance for risk calculations.
     */
    public BrokerAPI(SignalGenerator signalGenerator, RiskManager riskManager) {
        this.signalGenerator = signalGenerator;
        this.riskManager = riskManager;
    }

    /**
     * Generates a trade suggestion based on the latest market data.
     *
     * @param data           The list of OHLCV data.
     * @param entryPrice     The price at which the trade is entered.
     * @param stopLossPoints The stop-loss distance in points.
     * @param riskReward     The desired risk-reward ratio.
     */
    public void generateTradeSuggestion(List<OHLCVData> data, double entryPrice, double stopLossPoints, double riskReward) {
        if (data == null || data.isEmpty()) {
            System.err.println("Insufficient market data for generating trade suggestions.");
            return;
        }

        // Generate trading signal
        String signal = signalGenerator.generateSignal(data);
        if (signal.equals("Hold")) {
            System.out.println("Signal: Hold. No trade suggestion available.");
            return;
        }

        // Calculate position size and risk management levels
        double positionSize = riskManager.calculatePositionSize(stopLossPoints);
        double stopLossPrice = riskManager.calculateStopLoss(entryPrice, positionSize);
        double takeProfitPrice = riskManager.calculateTakeProfit(entryPrice, stopLossPrice, riskReward);

        // Display trade suggestion
        System.out.println("--- Trade Suggestion ---");
        System.out.println("Signal: " + signal);
        System.out.println("Entry Price: " + entryPrice);
        System.out.println("Position Size: " + positionSize);
        System.out.println("Stop-Loss Price: " + stopLossPrice);
        System.out.println("Take-Profit Price: " + takeProfitPrice);
        System.out.println("Risk-Reward Ratio: " + riskReward);
    }
}
