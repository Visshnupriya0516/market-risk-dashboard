package com.visshnu.marketrisk;

import com.visshnu.marketrisk.db.CsvReader;
import com.visshnu.marketrisk.model.PriceData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Ask user for company name (no extension required)
        System.out.print("Enter Company Name: ");
        String companyName = sc.nextLine().trim();

        // Folder where all CSV files are stored
        String folderPath = "C:\\Users\\vissh\\OneDrive\\Desktop\\Visshnu\\Project\\market-risk-dashboard\\src\\main\\resources"; // Change once only

        // Build file path automatically
        String csvFilePath = Paths.get(folderPath, companyName + "_timeseries.csv").toString();


        File csvFile = new File(csvFilePath);

        // Check if file exists
        if (!csvFile.exists()) {
            System.err.println("CSV file for '" + companyName + "' not found in folder: " + folderPath);
            return;
        }

        try {
            List<PriceData> prices = CsvReader.readPriceData(csvFilePath);

            if (prices.isEmpty()) {
                System.out.println("No price data found!");
                return;
            }

            System.out.println("Date\t\tClose Price");
            System.out.println("----------------------------");
            for (PriceData pd : prices) {
                System.out.println(pd.getDate() + "\t" + pd.getClose());
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }
}
