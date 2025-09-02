package com.visshnu.marketrisk.api;

import com.visshnu.marketrisk.db.CsvReader;
import com.visshnu.marketrisk.model.PriceData;

import java.io.IOException;
import java.util.List;

/**
 * MarketDataApiClient is responsible for fetching or loading price data.
 * For now, it loads from CSV, but later you can extend it to call real APIs.
 */
public class MarketDataApiClient {

    /**
     * Load historical price data for a given company.
     * 
     * @param companyName The stock/company name (CSV file should be {companyName}_timeseries.csv)
     * @return List of PriceData objects
     * @throws IOException if CSV cannot be read
     */
    public static List<PriceData> getHistoricalPrices(String companyName) throws IOException {
        String filePath = "src/main/resources/" + companyName + "_timeseries.csv";
        return CsvReader.readPriceData(filePath);
    }

    /**
     * Simple helper to print data preview (first N rows).
     */
    public static void previewData(List<PriceData> prices, int limit) {
        System.out.println("Date\t\tClose Price");
        System.out.println("----------------------------");
        prices.stream()
                .limit(limit)
                .forEach(pd -> System.out.println(pd.getDate() + "\t" + pd.getClose()));
    }
}
