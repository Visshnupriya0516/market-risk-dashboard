package com.visshnu.marketrisk.db;
import com.visshnu.marketrisk.model.PriceData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

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
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String date = parts[0].trim();
                    double close = Double.parseDouble(parts[1].trim());
                    priceList.add(new PriceData(date, close));
                }
            }
        }
        return priceList;
    }
}
