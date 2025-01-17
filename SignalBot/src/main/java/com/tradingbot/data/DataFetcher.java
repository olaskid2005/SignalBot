package com.tradingbot.data;

import com.tradingbot.integration.GateIOAPI;
import org.json.JSONObject;

import java.util.List;

/**
 * Unified interface for fetching live and historical market data.
 */
public class DataFetcher {

    private final GateIOAPI gateIOAPI;

    /**
     * Constructor for DataFetcher.
     *
     * @param gateIOAPI An instance of the GateIOAPI class for Gate.io-specific data fetching.
     */
    public DataFetcher(GateIOAPI gateIOAPI) {
        this.gateIOAPI = gateIOAPI;
    }

    /**
     * Fetches live ticker data for a given trading pair using Gate.io.
     *
     * @param tradingPair The trading pair (e.g., "BTC_USDT").
     * @return A JSONObject containing live ticker data.
     */
    public JSONObject fetchLiveData(String tradingPair) {
        return gateIOAPI.fetchLiveTicker(tradingPair);
    }

    /**
     * Fetches historical OHLCV data for a given trading pair and timeframe using Gate.io.
     *
     * @param tradingPair The trading pair (e.g., "BTC_USDT").
     * @param interval    The timeframe interval (e.g., "1m", "5m").
     * @param limit       The number of candlesticks to fetch.
     * @return A list of OHLCV data arrays.
     */
    public List<double[]> fetchHistoricalData(String tradingPair, String interval, int limit) {
        return gateIOAPI.fetchHistoricalData(tradingPair, interval, limit);
    }
}
