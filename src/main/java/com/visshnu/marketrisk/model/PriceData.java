package com.visshnu.marketrisk.model;

import java.time.LocalDate;

public class PriceData {
    private LocalDate date;
    private double close;

    public PriceData(LocalDate date, double close) {
        this.date = date;
        this.close = close;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getClose() {
        return close;
    }

    @Override
    public String toString() {
        return "PriceData{" +
                "date=" + date +
                ", close=" + close +
                '}';
    }
}
