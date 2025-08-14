package com.visshnu.marketrisk.calculations;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class RiskCalculationsTest {

    @Test
    void testAnnualizedVolatility() {
        List<Double> returns = List.of(0.01, -0.005, 0.002, 0.015, -0.007);
        double vol = RiskCalculations.annualizedVolatility(returns);
        assertTrue(vol > 0, "Volatility should be positive");
    }

    @Test
    void testHistoricalVaR() {
        List<Double> returns = List.of(-0.05, -0.02, 0.01, 0.03, -0.04);
        double var = RiskCalculations.historicalVaR(returns, 0.95);
        assertTrue(var <= 0, "VaR should be negative or zero");
    }
}

