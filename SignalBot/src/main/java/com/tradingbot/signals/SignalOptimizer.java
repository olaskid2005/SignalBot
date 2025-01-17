package com.tradingbot.signals;

import java.util.HashMap;
import java.util.Map;

/**
 * Optimizes the parameters and thresholds used in signal generation.
 */
public class SignalOptimizer {

    private final Map<String, Double> parameters;

    /**
     * Constructor for SignalOptimizer.
     * Initializes default parameters.
     */
    public SignalOptimizer() {
        parameters = new HashMap<>();
        // Default parameter values
        parameters.put("rsiThresholdBuy", 30.0);
        parameters.put("rsiThresholdSell", 70.0);
        parameters.put("macdThresholdBuy", 0.0);
        parameters.put("macdThresholdSell", 0.0);
        parameters.put("bollingerMultiplier", 2.0);
    }

    /**
     * Sets a parameter for optimization.
     *
     * @param key   The parameter name.
     * @param value The parameter value.
     */
    public void setParameter(String key, double value) {
        parameters.put(key, value);
    }

    /**
     * Gets the value of a parameter.
     *
     * @param key The parameter name.
     * @return The parameter value.
     */
    public double getParameter(String key) {
        return parameters.getOrDefault(key, 0.0);
    }

    /**
     * Optimizes the parameters based on historical performance data.
     *
     * @param historicalData The historical performance data.
     */
    public void optimize(Map<String, Double> historicalData) {
        // Example: Optimize RSI thresholds based on historical success rates
        double rsiBuySuccessRate = historicalData.getOrDefault("rsiBuySuccessRate", 0.5);
        double rsiSellSuccessRate = historicalData.getOrDefault("rsiSellSuccessRate", 0.5);

        if (rsiBuySuccessRate > 0.6) {
            parameters.put("rsiThresholdBuy", Math.max(20.0, parameters.get("rsiThresholdBuy") - 1));
        }

        if (rsiSellSuccessRate > 0.6) {
            parameters.put("rsiThresholdSell", Math.min(80.0, parameters.get("rsiThresholdSell") + 1));
        }

        // Example: Adjust Bollinger Band multiplier
        double bollingerPerformance = historicalData.getOrDefault("bollingerPerformance", 0.5);
        if (bollingerPerformance > 0.6) {
            parameters.put("bollingerMultiplier", parameters.get("bollingerMultiplier") + 0.1);
        } else {
            parameters.put("bollingerMultiplier", Math.max(1.0, parameters.get("bollingerMultiplier") - 0.1));
        }

        // Add more optimization logic as needed
    }

    /**
     * Displays the current parameters for debugging.
     */
    public void displayParameters() {
        System.out.println("Current Parameters:");
        parameters.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
