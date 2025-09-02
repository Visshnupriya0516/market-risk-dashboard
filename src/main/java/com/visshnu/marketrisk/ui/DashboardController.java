package com.visshnu.marketrisk.ui;

import com.visshnu.marketrisk.db.CsvReader;
import com.visshnu.marketrisk.model.PriceData;
import com.visshnu.marketrisk.service.MarketRiskService;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardController {

    @FXML private Label lblFile;
    @FXML private Slider sliderConfidence;
    @FXML private Label lblConfidence;

    @FXML private Label lblAvgPrice;
    @FXML private Label lblVol;
    @FXML private Label lblVaR;
    @FXML private Label lblCVaR;
    @FXML private Label lblSharpe;
    @FXML private Label lblMDD;

    @FXML private LineChart<String, Number> priceChart;
    @FXML private CategoryAxis priceChartXAxis;
    @FXML private NumberAxis priceChartYAxis;

    @FXML private BarChart<String, Number> returnsChart;
    @FXML private CategoryAxis returnsChartXAxis;
    @FXML private NumberAxis returnsChartYAxis;

    @FXML private TableView<PriceData> tablePrices;
    @FXML private TableColumn<PriceData, String> colDate;
    @FXML private TableColumn<PriceData, Number> colClose;

    private final ObservableList<PriceData> priceItems = FXCollections.observableArrayList();
    private List<PriceData> loadedPrices = new ArrayList<>();
    private MarketRiskService riskService;

    private static final DecimalFormat DF2 = new DecimalFormat("#,##0.00");
    private static final DecimalFormat DF4 = new DecimalFormat("#,##0.0000");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        // Table setup
        tablePrices.setItems(priceItems);
        colDate.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().getDate().format(DATE_FMT)));
        colClose.setCellValueFactory(cd ->
                new ReadOnlyDoubleWrapper(cd.getValue().getClose()));

        // Confidence slider (90% to 99%)
        sliderConfidence.setMin(0.90);
        sliderConfidence.setMax(0.99);
        sliderConfidence.setValue(0.95);
        sliderConfidence.setBlockIncrement(0.01);
        sliderConfidence.valueProperty().addListener((obs, oldV, newV) -> {
            double cl = roundTo(newV.doubleValue(), 2);
            lblConfidence.setText((int)Math.round(cl * 100) + "%");
            refreshRiskMetrics();
            refreshReturnsHistogram();
        });

        lblConfidence.setText("95%");
        setupCharts();
    }

    private void setupCharts() {
        priceChart.setCreateSymbols(false);
        priceChart.getData().clear();
        priceChartXAxis.setLabel("Date");
        priceChartYAxis.setLabel("Close");

        returnsChart.getData().clear();
        returnsChartXAxis.setLabel("Return Bin");
        returnsChartYAxis.setLabel("Frequency");
        returnsChart.setCategoryGap(2);
        returnsChart.setBarGap(1);
    }

    @FXML
    public void onChooseFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Price CSV (date,close)");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fc.showOpenDialog(lblFile.getScene().getWindow());
        if (file == null) return;

        lblFile.setText(file.getAbsolutePath());
        try {
            loadedPrices = CsvReader.readPriceData(file.getAbsolutePath());
            // Sort by date ascending (just in case)
            loadedPrices.sort(Comparator.comparing(PriceData::getDate));
            priceItems.setAll(loadedPrices);

            riskService = new MarketRiskService(loadedPrices);

            refreshRiskMetrics();
            refreshPriceSeries();
            refreshReturnsHistogram();

        } catch (IOException ex) {
            showError("Error reading CSV", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            showError("Invalid data", ex.getMessage());
        }
    }

    private void refreshRiskMetrics() {
        if (riskService == null) return;

        double cl = roundTo(sliderConfidence.getValue(), 2);

        double avg = riskService.calculateAveragePrice();
        double vol = riskService.calculateVolatility();
        double var = riskService.calculateHistoricalVaR(cl);
        double cvar = riskService.calculateCVaR(cl);
        double sharpe = riskService.calculateSharpeRatio();
        double mdd = riskService.calculateMaxDrawdown();

        lblAvgPrice.setText(DF2.format(avg));
        lblVol.setText(DF4.format(vol));
        lblVaR.setText(DF4.format(var));
        lblCVaR.setText(DF4.format(cvar));
        lblSharpe.setText(DF4.format(sharpe));
        lblMDD.setText(DF4.format(mdd));
    }

    private void refreshPriceSeries() {
        if (loadedPrices == null || loadedPrices.size() < 2) return;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Close");
        for (PriceData pd : loadedPrices) {
            series.getData().add(new XYChart.Data<>(pd.getDate().format(DATE_FMT), pd.getClose()));
        }

        priceChart.getData().clear();
        priceChart.getData().add(series);
    }

    private void refreshReturnsHistogram() {
        if (loadedPrices == null || loadedPrices.size() < 2) return;

        // Compute log returns
        List<Double> rets = computeLogReturns(loadedPrices);

        // Build histogram (30 bins)
        int bins = 30;
        double min = rets.stream().min(Double::compareTo).orElse(0.0);
        double max = rets.stream().max(Double::compareTo).orElse(0.0);
        if (min == max) { min -= 0.0001; max += 0.0001; }
        double width = (max - min) / bins;
        int[] counts = new int[bins];

        for (double r : rets) {
            int idx = (int) Math.floor((r - min) / width);
            if (idx >= bins) idx = bins - 1;
            if (idx < 0) idx = 0;
            counts[idx]++;
        }

        XYChart.Series<String, Number> hist = new XYChart.Series<>();
        hist.setName("Returns Histogram");

        List<String> categories = new ArrayList<>();
        for (int i = 0; i < bins; i++) {
            double binStart = min + i * width;
            double binEnd = binStart + width;
            String label = DF4.format(binStart) + "–" + DF4.format(binEnd);
            categories.add(label);
            hist.getData().add(new XYChart.Data<>(label, counts[i]));
        }

        returnsChartXAxis.setCategories(FXCollections.observableArrayList(categories));
        returnsChart.getData().clear();
        returnsChart.getData().add(hist);
    }

    private static List<Double> computeLogReturns(List<PriceData> prices) {
        List<Double> out = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            double p0 = prices.get(i - 1).getClose();
            double p1 = prices.get(i).getClose();
            if (p0 > 0 && p1 > 0) {
                out.add(Math.log(p1 / p0));
            }
        }
        return out;
    }

    private static double roundTo(double v, int places) {
        double pow = Math.pow(10, places);
        return Math.round(v * pow) / pow;
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
