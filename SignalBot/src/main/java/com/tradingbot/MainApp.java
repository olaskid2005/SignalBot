package com.tradingbot;

import com.tradingbot.data.DataFetcher;
import com.tradingbot.data.OHLCVData;
import com.tradingbot.indicators.*;
import com.tradingbot.ml.*;
import com.tradingbot.risk.RiskManager;
import com.tradingbot.signals.SignalGenerator;
import com.tradingbot.integration.BrokerAPI;
import com.tradingbot.integration.GateIOAPI;
import java.time.Instant;

import org.json.JSONObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main application entry point for the trading bot.
 */
public class MainApp {

    public static void main(String[] args) {
        // Configuration
        String tradingPair = "BTC_USDT";
        String interval = "1h";
        int dataLimit = 100;
        double accountBalance = 10000.0;
        double riskPerTrade = 0.01;
        double riskRewardRatio = 2.0;

        // Initialize components
        GateIOAPI gateIOAPI = new GateIOAPI();
        DataFetcher dataFetcher = new DataFetcher(gateIOAPI);
        RiskManager riskManager = new RiskManager(accountBalance, riskPerTrade);

        // Indicators
        RSIIndicator rsiIndicator = new RSIIndicator(14);
        MACDIndicator macdIndicator = new MACDIndicator(12, 26, 9);
        BollingerBandsIndicator bollingerBandsIndicator = new BollingerBandsIndicator(20, 2);

        // Machine Learning Model (Placeholder: Logistic Regression)
        MLModel mlModel = new LogisticRegressionModel(6, 0.01, 1000);

        // Signal Generator
        SignalGenerator signalGenerator = new SignalGenerator(14, 12, 26, 9, 20, 2, mlModel);

        // Broker API
        BrokerAPI brokerAPI = new BrokerAPI(signalGenerator, riskManager);

        // Fetch data
        System.out.println("Fetching historical data...");
        List<double[]> ohlcvRawData = dataFetcher.fetchHistoricalData(tradingPair, interval, dataLimit);
        List<OHLCVData> ohlcvData = ohlcvRawData.stream()
        .map(data -> new OHLCVData(Instant.ofEpochMilli((long) data[0]), data[1], data[2], data[3], data[4], data[5]))
        .collect(Collectors.toList());

        // Generate and display signal
        if (!ohlcvData.isEmpty()) {
            double entryPrice = ohlcvData.get(ohlcvData.size() - 1).getClose();
            double stopLossDistance = entryPrice * 0.02; // Example: 2% stop-loss

            System.out.println("Generating trade suggestion...");
            brokerAPI.generateTradeSuggestion(ohlcvData, entryPrice, stopLossDistance, riskRewardRatio);
        } else {
            System.err.println("Failed to fetch sufficient data for signal generation.");
        }

        // Fetch live data for reference
        System.out.println("Fetching live ticker data...");
        JSONObject liveTicker = dataFetcher.fetchLiveData(tradingPair);
        if (liveTicker != null) {
            System.out.println("Live Ticker Data: " + liveTicker);
        } else {
            System.err.println("Failed to fetch live ticker data.");
        }
    }
}
