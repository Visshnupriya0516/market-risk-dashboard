package com.visshnu.marketrisk;

import com.visshnu.marketrisk.db.CsvReader;
import com.visshnu.marketrisk.model.PriceData;
import com.visshnu.marketrisk.service.MarketRiskService;
import com.visshnu.marketrisk.calculations.RiskCalculations;

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
        String folderPath = "src/main/resources"; // make relative instead of hard-coded path
        String csvFilePath = Paths.get(folderPath, companyName + "_timeseries.csv").toString();

        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            System.err.println("❌ CSV file for '" + companyName + "' not found in folder: " + folderPath);
            return;
        }

        try {
            List<PriceData> prices = CsvReader.readPriceData(csvFilePath);

            if (prices.isEmpty()) {
                System.out.println("⚠️ No price data found!");
                return;
            }
  // ✅ Create one risk service instance
  MarketRiskService riskService = new MarketRiskService(prices);
            boolean running = true;
            while (running) {
                System.out.println("\n📊 Market Risk Dashboard for " + companyName);
                System.out.println("1. View Data");
                System.out.println("2. Average Price");
                System.out.println("3. Volatility");
                System.out.println("4. Daily Returns");
                System.out.println("5. Historical VaR (95%)");
                System.out.println("6. CVaR (95%)");
                System.out.println("7. Sharpe Ratio");
                System.out.println("8. Max Drawdown");
                System.out.println("9. Exit");
                System.out.print("Choose an option: ");

                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> {
                        System.out.println("Date\t\tClose Price");
                        System.out.println("----------------------------");
                        prices.forEach(pd -> System.out.println(pd.getDate() + "\t" + pd.getClose()));
                    }
                    case 2 -> {
                        double avg = riskService.calculateAveragePrice();
                        System.out.printf("📈 Average Price: %.2f%n", avg);
                    }
                    case 3 -> {
                        double vol = riskService.calculateVolatility();
                        System.out.printf("📉 Volatility: %.4f%n", vol);
                    }
                    case 5 -> {
                        double var95 = riskService.calculateHistoricalVaR(0.95);
                        System.out.printf("⚠️ Historical VaR (95%%): %.4f%n", var95);
                    }
                    case 6 -> {
                        double cvar = riskService.calculateCVaR(0.95);
                        System.out.printf("⚠️ CVaR (95%%): %.4f%n", cvar);
                    }
                    case 7 -> {
                        double sharpe = riskService.calculateSharpeRatio();
                        System.out.printf("📊 Sharpe Ratio: %.4f%n", sharpe);
                    }
                    case 8 -> {
                        double mdd = riskService.calculateMaxDrawdown();
                        System.out.printf("📉 Max Drawdown: %.4f%n", mdd);
                    }
                    case 9 -> {
                        running = false;
                        System.out.println("Exiting...");
                    }
                    default -> System.out.println("Invalid choice, try again.");
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Error reading CSV: " + e.getMessage());
        }
    }
}
