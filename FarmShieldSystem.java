import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Core manager class for the FarmShield application.
 * Holds all farms and generated alerts, and provides
 * business logic for risk assessment, alerts, and reporting.
 */
public class FarmShieldSystem {

    private static final String DATA_FILE = "farmshield_data.ser";

    private List<Farm> farms;
    private List<Alert> alerts;
    private int farmCounter;
    private int animalCounter;
    private int itemCounter;

    public FarmShieldSystem() {
        this.farms = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.farmCounter = 0;
        this.animalCounter = 0;
        this.itemCounter = 0;
    }

    // ---------------------------------------------------------------
    // Farm management
    // ---------------------------------------------------------------

    public Farm registerFarm(String farmName, String ownerName, String location, String farmType) {
        farmCounter++;
        String farmId = String.format("F%03d", farmCounter);
        Farm farm = new Farm(farmId, farmName, ownerName, location, farmType);
        farms.add(farm);
        return farm;
    }

    public boolean removeFarm(String farmId) {
        return farms.removeIf(f -> f.getFarmId().equalsIgnoreCase(farmId));
    }

    public Farm findFarm(String farmId) {
        for (Farm f : farms) {
            if (f.getFarmId().equalsIgnoreCase(farmId)) {
                return f;
            }
        }
        return null;
    }

    public List<Farm> getAllFarms() {
        return farms;
    }

    // ---------------------------------------------------------------
    // Animal management
    // ---------------------------------------------------------------

    public Animal addAnimal(Farm farm, String type, int age, boolean vaccinated, String healthStatus) {
        animalCounter++;
        String animalId = String.format("A%03d", animalCounter);
        Animal animal = new Animal(animalId, type, age, vaccinated, healthStatus);
        farm.addAnimal(animal);
        return animal;
    }

    // ---------------------------------------------------------------
    // Checklist management
    // ---------------------------------------------------------------

    public ChecklistItem addChecklistItem(Farm farm, String category, String description, boolean completed) {
        itemCounter++;
        String itemId = String.format("C%03d", itemCounter);
        ChecklistItem item = new ChecklistItem(itemId, category, description, completed);
        farm.addChecklistItem(item);
        return item;
    }

    // ---------------------------------------------------------------
    // Risk & alerts
    // ---------------------------------------------------------------

    /**
     * Re-evaluates a farm's biosecurity status and generates fresh alerts
     * for anything that needs attention. Returns the alerts generated
     * during this call.
     */
    public List<Alert> generateAlertsForFarm(Farm farm) {
        List<Alert> newAlerts = new ArrayList<>();

        double riskScore = RiskCalculator.calculateRiskScore(farm);
        String riskLevel = RiskCalculator.getRiskLevel(riskScore);

        if (riskLevel.equals("HIGH")) {
            newAlerts.add(new Alert(farm.getFarmId(), farm.getFarmName(),
                    "Overall biosecurity risk is HIGH (score: " + riskScore + "). Immediate action required.",
                    Alert.Severity.HIGH));
        } else if (riskLevel.equals("MEDIUM")) {
            newAlerts.add(new Alert(farm.getFarmId(), farm.getFarmName(),
                    "Overall biosecurity risk is MEDIUM (score: " + riskScore + "). Review recommended.",
                    Alert.Severity.MEDIUM));
        }

        for (ChecklistItem item : farm.getChecklist()) {
            if (!item.isCompleted()) {
                newAlerts.add(new Alert(farm.getFarmId(), farm.getFarmName(),
                        "Pending checklist item [" + item.getItemId() + "] " + item.getCategory()
                                + ": " + item.getDescription(),
                        Alert.Severity.MEDIUM));
            }
        }

        for (Animal animal : farm.getAnimals()) {
            if (!animal.isVaccinated()) {
                newAlerts.add(new Alert(farm.getFarmId(), farm.getFarmName(),
                        "Animal [" + animal.getAnimalId() + "] (" + animal.getType() + ") is not vaccinated.",
                        Alert.Severity.HIGH));
            }
            if (animal.getHealthStatus().equalsIgnoreCase("Sick")) {
                newAlerts.add(new Alert(farm.getFarmId(), farm.getFarmName(),
                        "Animal [" + animal.getAnimalId() + "] (" + animal.getType() + ") is reported SICK.",
                        Alert.Severity.HIGH));
            }
        }

        alerts.addAll(newAlerts);
        return newAlerts;
    }

    public List<Alert> getAllAlerts() {
        return alerts;
    }

    public List<Alert> getAlertsForFarm(String farmId) {
        List<Alert> result = new ArrayList<>();
        for (Alert a : alerts) {
            if (a.getFarmId().equalsIgnoreCase(farmId)) {
                result.add(a);
            }
        }
        return result;
    }

    // ---------------------------------------------------------------
    // Reporting
    // ---------------------------------------------------------------

    public String generateFarmReport(Farm farm) {
        StringBuilder sb = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        double riskScore = RiskCalculator.calculateRiskScore(farm);
        String riskLevel = RiskCalculator.getRiskLevel(riskScore);
        double checklistCompliance = RiskCalculator.calculateChecklistCompliance(farm);
        double vaccinationRate = RiskCalculator.calculateVaccinationRate(farm);

        sb.append("=======================================================\n");
        sb.append("               FARMSHIELD - FARM REPORT\n");
        sb.append("=======================================================\n");
        sb.append("Generated on: ").append(timestamp).append("\n\n");

        sb.append("Farm ID       : ").append(farm.getFarmId()).append("\n");
        sb.append("Farm Name     : ").append(farm.getFarmName()).append("\n");
        sb.append("Owner         : ").append(farm.getOwnerName()).append("\n");
        sb.append("Location      : ").append(farm.getLocation()).append("\n");
        sb.append("Farm Type     : ").append(farm.getFarmType()).append("\n\n");

        sb.append("-------------------------------------------------------\n");
        sb.append("ANIMAL RECORDS (" + farm.getAnimals().size() + ")\n");
        sb.append("-------------------------------------------------------\n");
        if (farm.getAnimals().isEmpty()) {
            sb.append("No animals recorded.\n");
        } else {
            for (Animal a : farm.getAnimals()) {
                sb.append(a.toString()).append("\n");
            }
        }

        sb.append("\n-------------------------------------------------------\n");
        sb.append("BIOSECURITY CHECKLIST (" + farm.getChecklist().size() + ")\n");
        sb.append("-------------------------------------------------------\n");
        if (farm.getChecklist().isEmpty()) {
            sb.append("No checklist items recorded.\n");
        } else {
            for (ChecklistItem c : farm.getChecklist()) {
                sb.append(c.toString()).append("\n");
            }
        }

        sb.append("\n-------------------------------------------------------\n");
        sb.append("RISK ASSESSMENT\n");
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format("Checklist Compliance : %.2f%%\n", checklistCompliance));
        sb.append(String.format("Vaccination Rate      : %.2f%%\n", vaccinationRate));
        sb.append(String.format("Risk Score            : %.2f / 100\n", riskScore));
        sb.append("Risk Level            : ").append(riskLevel).append("\n");

        sb.append("\n-------------------------------------------------------\n");
        sb.append("ACTIVE ALERTS FOR THIS FARM\n");
        sb.append("-------------------------------------------------------\n");
        List<Alert> farmAlerts = getAlertsForFarm(farm.getFarmId());
        if (farmAlerts.isEmpty()) {
            sb.append("No alerts generated yet. Run risk calculation to check.\n");
        } else {
            for (Alert a : farmAlerts) {
                sb.append(a.toString()).append("\n");
            }
        }

        sb.append("=======================================================\n");
        return sb.toString();
    }

    public void saveReportToFile(Farm farm, String report) {
        String fileName = "Report_" + farm.getFarmId() + "_" + farm.getFarmName().replaceAll("\\s+", "_") + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(report);
            System.out.println("Report saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Persistence (save/load full system state between runs)
    // ---------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            farms = (List<Farm>) ois.readObject();
            alerts = (List<Alert>) ois.readObject();
            farmCounter = ois.readInt();
            animalCounter = ois.readInt();
            itemCounter = ois.readInt();
            System.out.println("Previous data loaded successfully (" + farms.size() + " farm(s) found).");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No valid previous data found. Starting fresh.");
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(farms);
            oos.writeObject(alerts);
            oos.writeInt(farmCounter);
            oos.writeInt(animalCounter);
            oos.writeInt(itemCounter);
            System.out.println("Data saved successfully. Goodbye!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
