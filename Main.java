import java.util.List;
import java.util.Scanner;

/**
 * FarmShield: Biosecurity Risk Management System
 * Java Programming Project - Console Application
 *
 * Entry point providing a menu-driven interface for:
 *   1. Farm registration and management
 *   2. Animal record management
 *   3. Biosecurity checklist maintenance
 *   4. Risk score calculation
 *   5. Alert generation for potential risks
 *   6. Farm report generation
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final FarmShieldSystem system = new FarmShieldSystem();

    public static void main(String[] args) {
        system.loadData();
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerFarmMenu();
                    break;
                case 2:
                    manageAnimalsMenu();
                    break;
                case 3:
                    manageChecklistMenu();
                    break;
                case 4:
                    calculateRiskMenu();
                    break;
                case 5:
                    viewAlertsMenu();
                    break;
                case 6:
                    generateReportMenu();
                    break;
                case 7:
                    viewAllFarmsMenu();
                    break;
                case 0:
                    running = false;
                    system.saveData();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
        sc.close();
    }

    // ---------------------------------------------------------------
    // Menu printing
    // ---------------------------------------------------------------

    private static void printBanner() {
        System.out.println("=======================================================");
        System.out.println("     FARMSHIELD - BIOSECURITY RISK MANAGEMENT SYSTEM");
        System.out.println("=======================================================");
    }

    private static void printMainMenu() {
        System.out.println("\n----------------------- MAIN MENU -----------------------");
        System.out.println("1. Register a New Farm");
        System.out.println("2. Manage Animal Records");
        System.out.println("3. Manage Biosecurity Checklist");
        System.out.println("4. Calculate Risk Score");
        System.out.println("5. View Alerts");
        System.out.println("6. Generate Farm Report");
        System.out.println("7. View All Farms");
        System.out.println("0. Save & Exit");
        System.out.println("-----------------------------------------------------------");
    }

    // ---------------------------------------------------------------
    // 1. Farm registration and management
    // ---------------------------------------------------------------

    private static void registerFarmMenu() {
        System.out.println("\n--- Register New Farm ---");
        String name = readString("Enter Farm Name: ");
        String owner = readString("Enter Owner Name: ");
        String location = readString("Enter Location: ");
        String type = readFarmType();

        Farm farm = system.registerFarm(name, owner, location, type);
        System.out.println("Farm registered successfully! Farm ID: " + farm.getFarmId());
    }

    private static String readFarmType() {
        System.out.println("Select Farm Type:");
        System.out.println("1. Livestock");
        System.out.println("2. Poultry");
        System.out.println("3. Mixed");
        int choice = readInt("Enter choice: ");
        switch (choice) {
            case 1: return "Livestock";
            case 2: return "Poultry";
            case 3: return "Mixed";
            default:
                System.out.println("Invalid choice, defaulting to 'Mixed'.");
                return "Mixed";
        }
    }

    private static void viewAllFarmsMenu() {
        System.out.println("\n--- All Registered Farms ---");
        List<Farm> farms = system.getAllFarms();
        if (farms.isEmpty()) {
            System.out.println("No farms registered yet.");
            return;
        }
        System.out.println("ID       | Name                 | Owner           | Location        | Type       | Animals | Checklist");
        for (Farm f : farms) {
            System.out.println(f);
        }
    }

    // ---------------------------------------------------------------
    // 2. Animal record management
    // ---------------------------------------------------------------

    private static void manageAnimalsMenu() {
        Farm farm = selectFarm();
        if (farm == null) return;

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Animals: " + farm.getFarmName() + " (" + farm.getFarmId() + ") ---");
            System.out.println("1. Add Animal");
            System.out.println("2. View Animals");
            System.out.println("3. Update Animal Health/Vaccination Status");
            System.out.println("4. Remove Animal");
            System.out.println("0. Back to Main Menu");
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1: addAnimal(farm); break;
                case 2: viewAnimals(farm); break;
                case 3: updateAnimal(farm); break;
                case 4: removeAnimal(farm); break;
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void addAnimal(Farm farm) {
        System.out.println("\n--- Add Animal ---");
        System.out.println("Select Animal Type: 1. Cow  2. Goat  3. Sheep  4. Pig  5. Chicken");
        int typeChoice = readInt("Enter choice: ");
        String type;
        switch (typeChoice) {
            case 1: type = "Cow"; break;
            case 2: type = "Goat"; break;
            case 3: type = "Sheep"; break;
            case 4: type = "Pig"; break;
            case 5: type = "Chicken"; break;
            default:
                System.out.println("Invalid choice, defaulting to 'Cow'.");
                type = "Cow";
        }
        int age = readInt("Enter Age (in months): ");
        boolean vaccinated = readYesNo("Is the animal vaccinated? (y/n): ");
        String health = readHealthStatus();

        Animal animal = system.addAnimal(farm, type, age, vaccinated, health);
        System.out.println("Animal added successfully! Animal ID: " + animal.getAnimalId());
    }

    private static String readHealthStatus() {
        System.out.println("Select Health Status: 1. Healthy  2. Sick  3. Under Observation");
        int choice = readInt("Enter choice: ");
        switch (choice) {
            case 1: return "Healthy";
            case 2: return "Sick";
            case 3: return "Under Observation";
            default:
                System.out.println("Invalid choice, defaulting to 'Healthy'.");
                return "Healthy";
        }
    }

    private static void viewAnimals(Farm farm) {
        System.out.println("\n--- Animal Records: " + farm.getFarmName() + " ---");
        if (farm.getAnimals().isEmpty()) {
            System.out.println("No animals recorded for this farm.");
            return;
        }
        System.out.println("ID       | Type       | Age   | Vaccination  | Health Status");
        for (Animal a : farm.getAnimals()) {
            System.out.println(a);
        }
    }

    private static void updateAnimal(Farm farm) {
        viewAnimals(farm);
        if (farm.getAnimals().isEmpty()) return;
        String id = readString("Enter Animal ID to update: ");
        Animal animal = farm.findAnimal(id);
        if (animal == null) {
            System.out.println("Animal not found.");
            return;
        }
        boolean vaccinated = readYesNo("Is the animal vaccinated now? (y/n): ");
        animal.setVaccinated(vaccinated);
        String health = readHealthStatus();
        animal.setHealthStatus(health);
        System.out.println("Animal record updated successfully.");
    }

    private static void removeAnimal(Farm farm) {
        viewAnimals(farm);
        if (farm.getAnimals().isEmpty()) return;
        String id = readString("Enter Animal ID to remove: ");
        boolean removed = farm.removeAnimal(id);
        System.out.println(removed ? "Animal removed successfully." : "Animal not found.");
    }

    // ---------------------------------------------------------------
    // 3. Biosecurity checklist maintenance
    // ---------------------------------------------------------------

    private static void manageChecklistMenu() {
        Farm farm = selectFarm();
        if (farm == null) return;

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Biosecurity Checklist: " + farm.getFarmName() + " (" + farm.getFarmId() + ") ---");
            System.out.println("1. Add Checklist Item");
            System.out.println("2. View Checklist");
            System.out.println("3. Mark Item as Completed/Pending");
            System.out.println("4. Remove Checklist Item");
            System.out.println("0. Back to Main Menu");
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1: addChecklistItem(farm); break;
                case 2: viewChecklist(farm); break;
                case 3: updateChecklistItem(farm); break;
                case 4: removeChecklistItem(farm); break;
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void addChecklistItem(Farm farm) {
        System.out.println("\n--- Add Checklist Item ---");
        System.out.println("Select Category:");
        System.out.println("1. Sanitation  2. Vaccination  3. Quarantine  4. Feed Management  5. Visitor Control");
        int choice = readInt("Enter choice: ");
        String category;
        switch (choice) {
            case 1: category = "Sanitation"; break;
            case 2: category = "Vaccination"; break;
            case 3: category = "Quarantine"; break;
            case 4: category = "Feed Management"; break;
            case 5: category = "Visitor Control"; break;
            default:
                System.out.println("Invalid choice, defaulting to 'Sanitation'.");
                category = "Sanitation";
        }
        String description = readString("Enter Description: ");
        boolean completed = readYesNo("Is this item already completed? (y/n): ");

        ChecklistItem item = system.addChecklistItem(farm, category, description, completed);
        System.out.println("Checklist item added! Item ID: " + item.getItemId());
    }

    private static void viewChecklist(Farm farm) {
        System.out.println("\n--- Biosecurity Checklist: " + farm.getFarmName() + " ---");
        if (farm.getChecklist().isEmpty()) {
            System.out.println("No checklist items recorded for this farm.");
            return;
        }
        System.out.println("ID     | Category         | Description                              | Status");
        for (ChecklistItem c : farm.getChecklist()) {
            System.out.println(c);
        }
    }

    private static void updateChecklistItem(Farm farm) {
        viewChecklist(farm);
        if (farm.getChecklist().isEmpty()) return;
        String id = readString("Enter Item ID to update: ");
        ChecklistItem item = farm.findChecklistItem(id);
        if (item == null) {
            System.out.println("Checklist item not found.");
            return;
        }
        boolean completed = readYesNo("Mark as completed? (y/n): ");
        item.setCompleted(completed);
        System.out.println("Checklist item updated successfully.");
    }

    private static void removeChecklistItem(Farm farm) {
        viewChecklist(farm);
        if (farm.getChecklist().isEmpty()) return;
        String id = readString("Enter Item ID to remove: ");
        boolean removed = farm.removeChecklistItem(id);
        System.out.println(removed ? "Checklist item removed successfully." : "Checklist item not found.");
    }

    // ---------------------------------------------------------------
    // 4. Risk score calculation & 5. Alert generation
    // ---------------------------------------------------------------

    private static void calculateRiskMenu() {
        Farm farm = selectFarm();
        if (farm == null) return;

        double checklistCompliance = RiskCalculator.calculateChecklistCompliance(farm);
        double vaccinationRate = RiskCalculator.calculateVaccinationRate(farm);
        double riskScore = RiskCalculator.calculateRiskScore(farm);
        String riskLevel = RiskCalculator.getRiskLevel(riskScore);

        System.out.println("\n--- Risk Assessment: " + farm.getFarmName() + " ---");
        System.out.printf("Checklist Compliance : %.2f%%\n", checklistCompliance);
        System.out.printf("Vaccination Rate      : %.2f%%\n", vaccinationRate);
        System.out.printf("Risk Score            : %.2f / 100\n", riskScore);
        System.out.println("Risk Level            : " + riskLevel);

        List<Alert> newAlerts = system.generateAlertsForFarm(farm);
        if (newAlerts.isEmpty()) {
            System.out.println("\nNo new alerts. Farm biosecurity status looks good!");
        } else {
            System.out.println("\n" + newAlerts.size() + " alert(s) generated:");
            for (Alert a : newAlerts) {
                System.out.println(" - " + a.getMessage() + " [" + a.getSeverity() + "]");
            }
        }
    }

    // ---------------------------------------------------------------
    // 5. View alerts
    // ---------------------------------------------------------------

    private static void viewAlertsMenu() {
        System.out.println("\n--- All Alerts ---");
        List<Alert> alerts = system.getAllAlerts();
        if (alerts.isEmpty()) {
            System.out.println("No alerts generated yet. Run risk calculation for a farm first.");
            return;
        }
        for (Alert a : alerts) {
            System.out.println(a);
        }
    }

    // ---------------------------------------------------------------
    // 6. Farm report generation
    // ---------------------------------------------------------------

    private static void generateReportMenu() {
        Farm farm = selectFarm();
        if (farm == null) return;

        String report = system.generateFarmReport(farm);
        System.out.println("\n" + report);

        boolean saveToFile = readYesNo("Save this report to a text file? (y/n): ");
        if (saveToFile) {
            system.saveReportToFile(farm, report);
        }
    }

    // ---------------------------------------------------------------
    // Helper utilities
    // ---------------------------------------------------------------

    private static Farm selectFarm() {
        List<Farm> farms = system.getAllFarms();
        if (farms.isEmpty()) {
            System.out.println("No farms registered yet. Please register a farm first.");
            return null;
        }
        viewAllFarmsMenu();
        String id = readString("Enter Farm ID: ");
        Farm farm = system.findFarm(id);
        if (farm == null) {
            System.out.println("Farm not found.");
        }
        return farm;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }
}
