package com.visshnu.marketrisk.calculations;

import com.visshnu.marketrisk.model.PriceData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiskCalculationsTest {

    private List<PriceData> samplePrices() {
        return Arrays.asList(
                new PriceData(LocalDate.of(2023, 1, 1), 100),
                new PriceData(LocalDate.of(2023, 1, 2), 102),
                new PriceData(LocalDate.of(2023, 1, 3), 101),
                new PriceData(LocalDate.of(2023, 1, 4), 105),
                new PriceData(LocalDate.of(2023, 1, 5), 107)
        );
    }

    @Test
    void testComputeReturns() {
        double[] returns = RiskCalculations.computeReturns(samplePrices());
        assertEquals(4, returns.length, "Returns length should be N-1");
    }

    @Test
    void testVolatility() {
        double[] returns = RiskCalculations.computeReturns(samplePrices());
        double vol = RiskCalculations.calculateVolatility(returns);
        assertTrue(vol > 0, "Volatility should be positive");
    }

    @Test
    void testHistoricalVaR() {
        double[] returns = RiskCalculations.computeReturns(samplePrices());
        double var = RiskCalculations.calculateHistoricalVaR(returns, 0.95);
        assertTrue(var <= 0, "VaR should be negative or zero");
    }

    @Test
    void testCVaR() {
        double[] returns = RiskCalculations.computeReturns(samplePrices());
        double cvar = RiskCalculations.calculateCVaR(returns, 0.95);
        assertTrue(cvar <= 0, "CVaR should be negative or zero");
    }

    @Test
    void testMaxDrawdown() {
        double mdd = RiskCalculations.calculateMaxDrawdown(samplePrices());
        assertTrue(mdd <= 0, "Max Drawdown should be zero or negative");
    }

    @Test
    void testSharpeRatio() {
        double[] returns = RiskCalculations.computeReturns(samplePrices());
        double sharpe = RiskCalculations.calculateSharpeRatio(returns);
        assertTrue(sharpe != 0, "Sharpe Ratio should not be zero");
    }
}
