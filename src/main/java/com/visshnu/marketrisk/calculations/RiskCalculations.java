package com.visshnu.marketrisk.calculations;

import com.visshnu.marketrisk.model.PriceData;

import java.util.Arrays;
import java.util.List;

public class RiskCalculations {

    /** Compute log returns from price series */
    public static double[] computeReturns(List<PriceData> prices) {
        double[] returns = new double[prices.size() - 1];
        for (int i = 1; i < prices.size(); i++) {
            double prev = prices.get(i - 1).getClose();
            double curr = prices.get(i).getClose();
            returns[i - 1] = Math.log(curr / prev);
        }
        return returns;
    }

    /** Historical Volatility (standard deviation of returns * sqrt(252)) */
    public static double calculateVolatility(double[] returns) {
        double mean = Arrays.stream(returns).average().orElse(0.0);
        double variance = Arrays.stream(returns)
                .map(r -> Math.pow(r - mean, 2))
                .average().orElse(0.0);
        return Math.sqrt(variance) * Math.sqrt(252);
    }

    /** Historical Value-at-Risk */
    public static double calculateHistoricalVaR(double[] returns, double confidenceLevel) {
        double[] sorted = Arrays.copyOf(returns, returns.length);
        Arrays.sort(sorted);
        int index = (int) Math.floor((1 - confidenceLevel) * sorted.length);
        return sorted[index];
    }

    /** Conditional Value-at-Risk (Expected Shortfall) */
    public static double calculateCVaR(double[] returns, double confidenceLevel) {
        double var = calculateHistoricalVaR(returns, confidenceLevel);
        return Arrays.stream(returns)
                .filter(r -> r <= var)
                .average().orElse(var);
    }

    /** Maximum Drawdown */
    public static double calculateMaxDrawdown(List<PriceData> prices) {
        double peak = prices.get(0).getClose();
        double maxDrawdown = 0.0;
        for (PriceData pd : prices) {
            double price = pd.getClose();
            if (price > peak) {
                peak = price;
            }
            double drawdown = (price - peak) / peak;
            maxDrawdown = Math.min(maxDrawdown, drawdown);
        }
        return maxDrawdown;
    }

    /** Sharpe Ratio (assumes risk-free rate = 0) */
    public static double calculateSharpeRatio(double[] returns) {
        double mean = Arrays.stream(returns).average().orElse(0.0);
        double volatility = calculateVolatility(returns);
        return volatility == 0 ? 0 : mean / volatility * Math.sqrt(252);
    }
}
