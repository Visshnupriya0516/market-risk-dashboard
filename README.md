


# 📊 Market Risk Dashboard  

An end-to-end **JavaFX dashboard** for visualizing and analyzing market risk.  
The project loads stock/currency price data, processes returns, computes risk metrics, and visualizes insights in an interactive dashboard.  

Built with a **modular design** (service, model, UI layers) so each component can be extended or reused independently.  

---

## 🚀 Features  

### 📥 Data Ingestion  
- Load stock and currency price data (from CSV or APIs).  
- Supports multiple tickers (e.g., `TCS.NS`, `AAPL`, `USD`).  

### 🧹 Data Processing  
- Cleans and parses time-series price data.  
- Computes daily returns.  

### 📊 Risk Metrics  
- Volatility  
- Mean Return  
- Sharpe Ratio  
- Value-at-Risk (95%)  
- Maximum Drawdown  

### 📈 Visualization (JavaFX UI)  
- Interactive charts for stock/currency performance.  
- Real-time dashboard view (FXML + controllers).  

---

## 🏗️ Project Structure  

```

market-risk-dashboard/
│── src/
│   ├── main/java/com/visshnu/marketrisk/
│   │   ├── api/                # Market data API client
│   │   ├── calculations/       # Risk calculations
│   │   ├── db/                 # CSV reader & DB helpers
│   │   ├── model/              # Price data model
│   │   ├── service/            # Business logic
│   │   └── ui/                 # JavaFX UI (FXML, controllers, main app)
│   │
│   └── test/java/com/visshnu/marketrisk/ # Unit tests
│
│── src/main/resources/         # FXML, configs
│── pom.xml                     # Maven build config
│── README.md                   # Project documentation

````

---

## ⚙️ Installation  

### Prerequisites  
- Java **17+**  
- Maven **3.6+**  

### Clone Repository  
```bash
git clone https://github.com/Visshnupriya0516/market-risk-dashboard.git
cd market-risk-dashboard
````

### Build Project

```bash
mvn clean package
```

---

## ▶️ Usage

### Run with Maven (JavaFX Plugin)

```bash
mvn javafx:run
```

### Run JAR File

```bash
java -cp target/market-risk-dashboard-1.0-SNAPSHOT.jar com.visshnu.marketrisk.Main
```

### Example Run

```
Enter Company Name: TCS.NS
✅ Risk metrics calculated
✅ Dashboard loaded successfully
```

---

## 📂 Example Outputs

* **Charts** → JavaFX interactive dashboard
* **Console Output** → Computed risk metrics

---

## 🛠️ Tech Stack

* **Language**: Java 17
* **UI**: JavaFX
* **Build Tool**: Maven
* **Testing**: JUnit

---

## 🏆 Project Highlights

* End-to-end risk analysis tool with a **visual dashboard**
* Modular Java design (easily extendable)
* Computes **real-world financial risk metrics**
* Interactive **JavaFX charts** for stock & currency data

---

## 👩‍💻 Author

Developed by **Visshnupriya 🚀**
GitHub: [Visshnupriya0516](https://github.com/Visshnupriya0516)

