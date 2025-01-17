package com.tradingbot.data;

import java.time.Instant;

/**
 * Represents Open-High-Low-Close-Volume (OHLCV) trading data.
 */
public class OHLCVData {

    private final Instant timestamp;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    /**
     * Constructor for OHLCVData.
     *
     * @param timestamp The timestamp of the data point.
     * @param open The opening price.
     * @param high The highest price.
     * @param low The lowest price.
     * @param close The closing price.
     * @param volume The trading volume.
     */
    public OHLCVData(Instant timestamp, double open, double high, double low, double close, double volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    // Getters

    public Instant getTimestamp() {
        return timestamp;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "OHLCVData{" +
                "timestamp=" + timestamp +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }

    /**
     * Converts a JSON object to an OHLCVData instance.
     *
     * @param json The JSON object containing OHLCV data.
     * @return An OHLCVData instance.
     */
    public static OHLCVData fromJson(org.json.JSONObject json) {
        Instant timestamp = Instant.ofEpochSecond(json.getLong("timestamp"));
        double open = json.getDouble("open");
        double high = json.getDouble("high");
        double low = json.getDouble("low");
        double close = json.getDouble("close");
        double volume = json.getDouble("volume");

        return new OHLCVData(timestamp, open, high, low, close, volume);
    }
}
