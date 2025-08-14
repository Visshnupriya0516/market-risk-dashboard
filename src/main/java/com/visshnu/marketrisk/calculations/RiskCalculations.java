package com.visshnu.marketrisk.calculations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RiskCalculations {

    // Compute daily returns
    public static List<Double> computeDailyReturns(List<Double> prices) {
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            double ret = (prices.get(i) - prices.get(i - 1)) / prices.get(i - 1);
            returns.add(ret);
        }
        return returns;
    }

    // Annualized volatility
    public static double annualizedVolatility(List<Double> dailyReturns) {
        double mean = dailyReturns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = dailyReturns.stream()
                .mapToDouble(r -> Math.pow(r - mean, 2))
                .sum() / (dailyReturns.size() - 1);
        double dailyVol = Math.sqrt(variance);
        return dailyVol * Math.sqrt(252); // 252 trading days
    }

    // Historical VaR
    public static double historicalVaR(List<Double> dailyReturns, double confidenceLevel) {
        List<Double> sortedReturns = new ArrayList<>(dailyReturns);
        Collections.sort(sortedReturns);
        int index = (int) Math.floor((1 - confidenceLevel) * sortedReturns.size());
        return -sortedReturns.get(index);
    }

    // Parametric VaR
    public static double parametricVaR(List<Double> dailyReturns, double confidenceLevel) {
        double mean = dailyReturns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = dailyReturns.stream()
                .mapToDouble(r -> Math.pow(r - mean, 2))
                .sum() / (dailyReturns.size() - 1);
        double stdDev = Math.sqrt(variance);

        // Z-score for 95% confidence
        double zScore = 1.65;
        return -(mean - zScore * stdDev);
    }
}
