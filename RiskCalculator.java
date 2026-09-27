import java.util.List;

/**
 * Contains the biosecurity risk scoring logic used across the system.
 *
 * Risk score (0-100, higher = riskier) is computed from two factors:
 *   1. Checklist compliance rate (60% weight)
 *   2. Animal vaccination rate   (40% weight)
 *
 * riskScore = 100 - (0.6 * checklistCompliance + 0.4 * vaccinationRate)
 */
public class RiskCalculator {

    public static double calculateChecklistCompliance(Farm farm) {
        List<ChecklistItem> checklist = farm.getChecklist();
        if (checklist.isEmpty()) {
            return 0.0; // no checklist maintained at all -> worst case compliance
        }
        long completed = checklist.stream().filter(ChecklistItem::isCompleted).count();
        return (completed * 100.0) / checklist.size();
    }

    public static double calculateVaccinationRate(Farm farm) {
        List<Animal> animals = farm.getAnimals();
        if (animals.isEmpty()) {
            return 0.0; // no animals recorded -> cannot verify vaccination status
        }
        long vaccinated = animals.stream().filter(Animal::isVaccinated).count();
        return (vaccinated * 100.0) / animals.size();
    }

    public static double calculateRiskScore(Farm farm) {
        double checklistCompliance = calculateChecklistCompliance(farm);
        double vaccinationRate = calculateVaccinationRate(farm);
        double safetyScore = (0.6 * checklistCompliance) + (0.4 * vaccinationRate);
        double riskScore = 100.0 - safetyScore;
        return Math.round(riskScore * 100.0) / 100.0; // round to 2 decimals
    }

    public static String getRiskLevel(double riskScore) {
        if (riskScore < 30) {
            return "LOW";
        } else if (riskScore < 60) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }
}
