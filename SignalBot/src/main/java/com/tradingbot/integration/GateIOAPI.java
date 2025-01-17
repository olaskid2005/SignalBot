package com.tradingbot.integration;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements Gate.io-specific API logic for fetching live and historical data.
 */
public class GateIOAPI {

    private static final String BASE_URL = "https://api.gateio.ws/api/v4/spot";
    private final OkHttpClient httpClient;

    /**
     * Constructor for GateIOAPI.
     */
    public GateIOAPI() {
        this.httpClient = new OkHttpClient();
    }

    /**
     * Fetches live ticker data for a given trading pair.
     *
     * @param tradingPair The trading pair (e.g., "BTC_USDT").
     * @return A JSONObject containing live ticker data.
     */
    public JSONObject fetchLiveTicker(String tradingPair) {
        String endpoint = BASE_URL + "/tickers";
        HttpUrl url = HttpUrl.parse(endpoint).newBuilder()
                .addQueryParameter("currency_pair", tradingPair)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONArray jsonArray = new JSONArray(response.body().string());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ticker = jsonArray.getJSONObject(i);
                    if (ticker.getString("currency_pair").equalsIgnoreCase(tradingPair)) {
                        return ticker;
                    }
                }
            } else {
                System.err.println("Failed to fetch live ticker: " + response.message());
            }
        } catch (IOException e) {
            System.err.println("Error fetching live ticker data: " + e.getMessage());
        }

        return null; // Return null if no data is available
    }

    /**
     * Fetches historical candlestick data for a given trading pair and timeframe.
     *
     * @param tradingPair The trading pair (e.g., "BTC_USDT").
     * @param interval    The timeframe interval (e.g., "1m", "5m", "1h").
     * @param limit       The number of candlesticks to fetch.
     * @return A list of OHLCV data arrays.
     */
    public List<double[]> fetchHistoricalData(String tradingPair, String interval, int limit) {
        String endpoint = BASE_URL + "/candlesticks";
        HttpUrl url = HttpUrl.parse(endpoint).newBuilder()
                .addQueryParameter("currency_pair", tradingPair)
                .addQueryParameter("interval", interval)
                .addQueryParameter("limit", String.valueOf(limit))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        List<double[]> ohlcvData = new ArrayList<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONArray jsonArray = new JSONArray(response.body().string());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray candle = jsonArray.getJSONArray(i);
                    double[] ohlcv = new double[6];
                    ohlcv[0] = candle.getLong(0);   // Timestamp
                    ohlcv[1] = candle.getDouble(1); // Open
                    ohlcv[2] = candle.getDouble(2); // High
                    ohlcv[3] = candle.getDouble(3); // Low
                    ohlcv[4] = candle.getDouble(4); // Close
                    ohlcv[5] = candle.getDouble(5); // Volume
                    ohlcvData.add(ohlcv);
                }
            } else {
                System.err.println("Failed to fetch historical data: " + response.message());
            }
        } catch (IOException e) {
            System.err.println("Error fetching historical data: " + e.getMessage());
        }

        return ohlcvData;
    }
}
