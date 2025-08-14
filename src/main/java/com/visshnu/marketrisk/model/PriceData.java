package com.visshnu.marketrisk.model;

public class PriceData {
    private String date;
    private double close;

    public PriceData(String date, double close) {
        this.date = date;
        this.close = close;
    }

    public String getDate() {
        return date;
    }

    public double getClose() {
        return close;
    }

    @Override
    public String toString() {
        return "PriceData{" +
                "date='" + date + '\'' +
                ", close=" + close +
                '}';
    }
}
