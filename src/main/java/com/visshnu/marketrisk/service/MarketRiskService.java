package com.visshnu.marketrisk.service;

import com.visshnu.marketrisk.model.PriceData;
import java.util.List;

public class MarketRiskService {

    public static double calculateAveragePrice(List<PriceData> prices) {
        if (prices.isEmpty()) return 0;
        double total = 0;
        for (PriceData pd : prices) {
            total += pd.getClose();
        }
        return total / prices.size();
    }

    public static double calculatePriceVolatility(List<PriceData> prices) {
        if (prices.size() < 2) return 0;

        double avg = calculateAveragePrice(prices);
        double sumSquaredDiff = 0;
        for (PriceData pd : prices) {
            double diff = pd.getClose() - avg;
            sumSquaredDiff += diff * diff;
        }
        return Math.sqrt(sumSquaredDiff / prices.size());
    }
}
