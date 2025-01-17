package com.tradingbot.risk;

/**
 * Manages risk by calculating position sizes, stop-loss levels, and risk-reward ratios.
 */
public class RiskManager {

    private final double accountBalance;
    private final double riskPerTrade;

    /**
     * Constructor for RiskManager.
     *
     * @param accountBalance The total account balance.
     * @param riskPerTrade   The percentage of the account balance to risk per trade (e.g., 0.01 for 1%).
     */
    public RiskManager(double accountBalance, double riskPerTrade) {
        if (accountBalance <= 0 || riskPerTrade <= 0 || riskPerTrade > 1) {
            throw new IllegalArgumentException("Invalid account balance or risk per trade.");
        }
        this.accountBalance = accountBalance;
        this.riskPerTrade = riskPerTrade;
    }

    /**
     * Calculates the position size based on stop-loss distance.
     *
     * @param stopLossDistance The difference between entry price and stop-loss price.
     * @return The calculated position size.
     */
    public double calculatePositionSize(double stopLossDistance) {
        if (stopLossDistance <= 0) {
            throw new IllegalArgumentException("Stop-loss distance must be greater than 0.");
        }

        double riskAmount = accountBalance * riskPerTrade;
        return riskAmount / stopLossDistance;
    }

    /**
     * Calculates the stop-loss price based on entry price and risk amount.
     *
     * @param entryPrice    The price at which the trade is entered.
     * @param positionSize  The size of the position.
     * @return The stop-loss price.
     */
    public double calculateStopLoss(double entryPrice, double positionSize) {
        if (positionSize <= 0) {
            throw new IllegalArgumentException("Position size must be greater than 0.");
        }

        double riskAmount = accountBalance * riskPerTrade;
        return entryPrice - (riskAmount / positionSize);
    }

    /**
     * Calculates the take-profit price based on the desired risk-reward ratio.
     *
     * @param entryPrice      The price at which the trade is entered.
     * @param stopLossPrice   The stop-loss price.
     * @param riskRewardRatio The desired risk-reward ratio (e.g., 2 for 1:2).
     * @return The take-profit price.
     */
    public double calculateTakeProfit(double entryPrice, double stopLossPrice, double riskRewardRatio) {
        if (riskRewardRatio <= 0) {
            throw new IllegalArgumentException("Risk-reward ratio must be greater than 0.");
        }

        double riskPerUnit = entryPrice - stopLossPrice;
        return entryPrice + (riskPerUnit * riskRewardRatio);
    }

    /**
     * Displays the risk management configuration.
     */
    public void displayConfiguration() {
        System.out.println("Account Balance: " + accountBalance);
        System.out.println("Risk per Trade: " + (riskPerTrade * 100) + "%");
    }
}
