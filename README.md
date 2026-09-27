# FarmShield: Biosecurity Risk Management System

A Java console (command-line) application for managing farm biosecurity —
built for the CS5304 Java Programming project (FarmShield).

## Features

1. **Farm registration and management** — register farms, view all farms
2. **Animal record management** — add/view/update/remove animal records (Cow, Goat, Sheep, Pig, Chicken)
3. **Biosecurity checklist maintenance** — add/view/update/remove checklist items (Sanitation, Vaccination, Quarantine, Feed Management, Visitor Control)
4. **Risk score calculation** — computes a 0–100 risk score from checklist compliance (60%) and vaccination rate (40%), and classifies it as LOW / MEDIUM / HIGH
5. **Alert generation** — automatically flags high overall risk, pending checklist items, unvaccinated animals, and sick animals
6. **Farm report generation** — prints a full report to the console and can save it as a `.txt` file

Data is automatically saved to `farmshield_data.ser` on exit and reloaded the
next time you run the program, so your farms persist between sessions.

## Project Structure

```
FarmShield/
├── Main.java              # Console menu / entry point
├── Farm.java               # Farm model (holds animals + checklist)
├── Animal.java              # Animal record model
├── ChecklistItem.java        # Biosecurity checklist item model
├── Alert.java                 # Alert model
├── RiskCalculator.java         # Risk scoring logic
├── FarmShieldSystem.java        # Core manager: CRUD, alerts, reports, persistence
└── README.md
```

## How to Compile and Run

You need a JDK (Java 8 or later) installed. From inside the `FarmShield` folder:

```bash
javac *.java
java Main
```

## Sample Walkthrough

1. Choose **1** to register a farm (e.g. "Green Valley Farm").
2. Choose **2** to add a few animals to it (mark some as not vaccinated to see alerts later).
3. Choose **3** to add checklist items (leave a couple marked "pending").
4. Choose **4** to calculate the risk score — this also generates alerts.
5. Choose **5** to view all alerts generated so far.
6. Choose **6** to generate and optionally save a full farm report.
7. Choose **0** to save all data and exit.

## Notes on the Risk Model

```
riskScore = 100 - (0.6 * checklistCompliance% + 0.4 * vaccinationRate%)

LOW    : riskScore < 30
MEDIUM : 30 <= riskScore < 60
HIGH   : riskScore >= 60
```

You're free to tune these weights/thresholds in `RiskCalculator.java` to match
whatever your report/presentation describes.
