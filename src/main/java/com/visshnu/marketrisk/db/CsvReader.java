package com.visshnu.marketrisk.db;

import com.visshnu.marketrisk.model.PriceData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<PriceData> readPriceData(String filePath) throws IOException {
        List<PriceData> priceList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { // Skip header
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split("[,;\\t]"); // allow comma, semicolon, tab
                if (parts.length >= 2) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0].trim(), FORMATTER);
                        double close = Double.parseDouble(parts[1].trim());
                        priceList.add(new PriceData(date, close));
                    } catch (Exception e) {
                        System.err.println("⚠️ Skipping bad row: " + line);
                    }
                }
            }
        }
        return priceList;
    }
}
