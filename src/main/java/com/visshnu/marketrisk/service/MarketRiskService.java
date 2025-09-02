package com.visshnu.marketrisk.service;

import com.visshnu.marketrisk.model.PriceData;
import com.visshnu.marketrisk.calculations.RiskCalculations;

import java.util.List;

public class MarketRiskService {

    private final List<PriceData> prices;
    private final double[] returns; // log returns

    public MarketRiskService(List<PriceData> prices) {
        if (prices == null || prices.size() < 2) {
            throw new IllegalArgumentException("Price data must have at least 2 points.");
        }
        this.prices = prices;
        this.returns = RiskCalculations.computeReturns(prices);
    }

    /** Average price */
    public double calculateAveragePrice() {
        return prices.stream().mapToDouble(PriceData::getClose).average().orElse(0);
    }

    /** Historical volatility (using log returns) */
    public double calculateVolatility() {
        return RiskCalculations.calculateVolatility(returns);
    }

    /** Historical VaR */
    public double calculateHistoricalVaR(double confidenceLevel) {
        return RiskCalculations.calculateHistoricalVaR(returns, confidenceLevel);
    }

    /** Conditional VaR */
    public double calculateCVaR(double confidenceLevel) {
        return RiskCalculations.calculateCVaR(returns, confidenceLevel);
    }

    /** Maximum Drawdown */
    public double calculateMaxDrawdown() {
        return RiskCalculations.calculateMaxDrawdown(prices);
    }

    /** Sharpe Ratio (assumes 0% risk-free for now) */
    public double calculateSharpeRatio() {
        return RiskCalculations.calculateSharpeRatio(returns);
    }
}
